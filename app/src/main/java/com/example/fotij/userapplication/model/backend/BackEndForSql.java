package com.example.fotij.userapplication.model.backend;

/**
 * Created by fotij on 23/12/2017.
 */

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.fotij.userapplication.model.datasource.ListDataSource;
import com.example.fotij.userapplication.model.datasource.PHPtools;
import com.example.fotij.userapplication.model.entities.Branch;
import com.example.fotij.userapplication.model.entities.Car;
import com.example.fotij.userapplication.model.entities.CarModel;
import com.example.fotij.userapplication.model.entities.Client;
import com.example.fotij.userapplication.model.entities.Order;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;
import com.example.fotij.userapplication.model.entities.User;


public class BackEndForSql implements BackEndFunc {
    private static final String WEB_URL = "http://ymehrzad.vlab.jct.ac.il";

    @Override
    public Updates addClient(Client client) {
        ContentValues contentValues = TakeNGoConst.ClientToContentValues(client);
        try {
            String result = PHPtools.POST(WEB_URL + "/addnewclient.php", contentValues);
            if(result.contains("Duplicate entry"))return Updates.DUPLICATE;
            result=result.substring(0,result.length()-1);
            long id = Long.parseLong(result);
            if (id > 0)
                return Updates.NOTHING;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return Updates.ERROR;
        }
        return Updates.ERROR;
    }

    @Override
    public Updates addUser(User user) {
        ContentValues contentValues = TakeNGoConst.UserToContentValues(user);
        try {
            String result = PHPtools.POST(WEB_URL + "/addnewuser.php", contentValues);
            if(result.contains("Duplicate entry"))return Updates.DUPLICATE;
            result=result.substring(1);
            long id = Long.parseLong(result);
            if (id >= 0)
                return Updates.NOTHING;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return Updates.ERROR;
        }
        return Updates.ERROR;
    }


    @Override
    public boolean updateClient(Client client) {
        ContentValues contentValues = TakeNGoConst.ClientToContentValues(client);
        try {
            String result = PHPtools.POST(WEB_URL + "/updateclient.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public Updates updateCar(Car car) {
        ContentValues contentValues = TakeNGoConst.CarToContentValues(car);
        try {
            String result = PHPtools.POST(WEB_URL + "/updatecar.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return Updates.NOTHING;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return Updates.ERROR;
        }
        return Updates.ERROR;
    }

    @Override
    public boolean deleteClient(int clientID) {
        ContentValues contentValues = TakeNGoConst.ClientIdToContentValues(clientID);
        try {
            String result = PHPtools.POST(WEB_URL + "/deleteclient.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return true;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return false;
        }
        return false;
    }

    @Override
    public User getUser(String username) {
        List<User> result = new ArrayList<>();
        try {
            ContentValues contentValuesid = TakeNGoConst.UserIdToContentValues(username);
            String str = PHPtools.POST(WEB_URL + "/findoneuser.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("User");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                User user = TakeNGoConst.ContentValuesToUser(contentValues);
                result.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.get(0);
    }

    @Override
    public Client getClient(int id) {
        List<Client> result = new ArrayList<Client>();
        try {
            ContentValues contentValuesid = TakeNGoConst.ClientIdToContentValues(id);
            String str = PHPtools.POST(WEB_URL + "/findoneclient.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Client");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Client client = TakeNGoConst.ContentValuesToClient(contentValues);
                result.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get(0);
    }

    @Override
    public Car getCar(int carNumber) {
        List<Car> result = new ArrayList<Car>();
        try {
            ContentValues contentValuesid = TakeNGoConst.CarIdToContentValues(carNumber);
            String str = PHPtools.POST(WEB_URL + "/findonecar.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Car");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Car car = TakeNGoConst.ContentValuesToCar(contentValues);
                result.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.get(0);
    }

    @Override
    public Branch getBranch(int branchNumber) {
        List<Branch> result = new ArrayList<Branch>();
        try {
            ContentValues contentValuesid = TakeNGoConst.BranchIdToContentValues(branchNumber);
            String str = PHPtools.POST(WEB_URL + "/findonebranch.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Branch");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Branch branch = TakeNGoConst.ContentValuesToBranch(contentValues);
                result.add(branch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.get(0);
    }

    @Override
    public ArrayList<CarModel> getAllCarModels() {
        List<CarModel> result = new ArrayList<CarModel>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallcarmodels.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("CarModels");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                CarModel carModel = TakeNGoConst.ContentValuesToCarModel(contentValues);
                result.add(carModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<CarModel>) result;
    }

    @Override
    public ArrayList<Client> getAllClients() {
        List<Client> result = new ArrayList<Client>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallclients.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Clients");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Client client = TakeNGoConst.ContentValuesToClient(contentValues);
                result.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (ArrayList<Client>) result;
    }

    @Override
    public ArrayList<Branch> getAllBranches() {
        List<Branch> result = new ArrayList<Branch>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallbranches.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Branches");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Branch branch = TakeNGoConst.ContentValuesToBranch(contentValues);
                result.add(branch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<Branch>) result;
    }

    @Override
    public ArrayList<Car> getAllCars() {
        List<Car> result = new ArrayList<Car>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallcars.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Cars");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Car car = TakeNGoConst.ContentValuesToCar(contentValues);
                result.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<Car>) result;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        List<User> result = new ArrayList<User>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallusers.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Users");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                User user = TakeNGoConst.ContentValuesToUser(contentValues);
                result.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<User>) result;
    }


    @Override
    public Client getclientbyusername(String username) {
        List<Client> result = new ArrayList<Client>();
        try {
            ContentValues contentValuesid = TakeNGoConst.UserIdToContentValues(username);
            String str = PHPtools.POST(WEB_URL + "/clientbyusername.php", contentValuesid);
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Client");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Client client = TakeNGoConst.ContentValuesToClient(contentValues);
                result.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.get(0);
    }


    @Override
    public int checkclient(String username, String password) {
        User user= getUser(username);
       if (user!=null) {
           if (user.getPassword().equals(password))
               return (getclientbyusername(username)).getId();
           return 0;
       }
       return -1;
    }

    @Override
    public ArrayList<Order> getAllOrders() {
        List<Order> result = new ArrayList<>();
        try {
            String str = PHPtools.GET(WEB_URL + "/findallorders.php");
            if (str.compareTo("0 results") ==0)
                throw new Exception("str");
            JSONArray array = new JSONObject(str).getJSONArray("Orders");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Order order = TakeNGoConst.ContentValuesToOrder(contentValues);
                result.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<Order>) result;
    }

    @Override
    public Updates addOrder(Order order) {
        ContentValues contentValues = TakeNGoConst.OrderToContentValues(order,false);
        try {
            String result = PHPtools.POST(WEB_URL + "/addneworder.php", contentValues);
            if(result.contains("Duplicate entry"))return Updates.DUPLICATE;
            long id = Long.parseLong(result);
            if (id > 0)
                return Updates.NOTHING;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return Updates.ERROR;
        }
        return Updates.ERROR;
    }

    @Override
    public boolean deleteOrder(int orderID) {
        return false;
    }

    @Override
    public Order getOrder(int clientID) {
        List<Order> result = new ArrayList<>();
        try {
            ContentValues contentValuesid = TakeNGoConst.OrderClientToContentValues(clientID);
            String str = PHPtools.POST(WEB_URL + "/findoneorder.php", contentValuesid);
            if (str.compareTo("0 results  ") ==0)
                return null;
            JSONArray array = new JSONObject(str).getJSONArray("Order");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues contentValues = PHPtools.JsonToContentValues(jsonObject);
                Order order = TakeNGoConst.ContentValuesToOrder(contentValues);
                result.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.get(0);
    }

    @Override
    public boolean isThereAnOrder(int clientID) {
        ArrayList<Order> orderArrayList=getAllOrders();
        for (Order order : orderArrayList) {
            if (order.getClientNum() == clientID && order.isOrderOpen())
                return true;
        }
        return false;
    }

    @Override
    public Updates updateOrder(Order order) {
        ContentValues contentValues = TakeNGoConst.OrderToContentValues(order,true);
        try {
            String result = PHPtools.POST(WEB_URL + "/updateorder.php", contentValues);
            if (result.compareTo("DONE") ==0)
                return Updates.NOTHING;
        } catch (IOException e) {
            //TODO implement the exception!!!
            //printLog("addStudent Exception:\n" + e);
            return Updates.ERROR;
        }
        return Updates.ERROR;
    }



}

