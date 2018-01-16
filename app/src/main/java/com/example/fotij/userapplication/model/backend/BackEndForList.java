package com.example.fotij.userapplication.model.backend;

import android.content.ContentValues;

import com.example.fotij.userapplication.model.datasource.ListDataSource;
import com.example.fotij.userapplication.model.datasource.MySqlDataSource;
import com.example.fotij.userapplication.model.datasource.PHPtools;
import com.example.fotij.userapplication.model.entities.Branch;
import com.example.fotij.userapplication.model.entities.Car;
import com.example.fotij.userapplication.model.entities.CarModel;
import com.example.fotij.userapplication.model.entities.Client;
import com.example.fotij.userapplication.model.entities.Order;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;
import com.example.fotij.userapplication.model.entities.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fotij on 23/12/2017.
 */

public class BackEndForList implements BackEndFunc {
    static int orderid = 0;
    private static final String WEB_URL = "http://ymehrzad.vlab.jct.ac.il";



    @Override
    public boolean updateClient(Client client) {
        for (int i = 0; i < ListDataSource.clientList.size(); i++) {
            if (ListDataSource.clientList.get(i).getId() == client.getId()) {
                ListDataSource.clientList.set(i, client);
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean deleteClient(int clientID) {
        Client clientTmp = null;
        for (Client client : ListDataSource.clientList
                ) {
            if (client.getId() == clientID) {
                clientTmp = client;
                break;
            }
        }
        return ListDataSource.clientList.remove(clientTmp);
    }

    @Override
    public Client getClient(int id) {
        for (Client client : ListDataSource.clientList
                ) {
            if (id == client.getId()) return client;
        }
        return null;
    }

    @Override
    public Car getCar(int carNumber) {
        for (Car car : ListDataSource.carList
                ) {
            if (carNumber == car.getCarNum())
                return car;
        }
        return null;
    }

    @Override
    public Branch getBranch(int branchNumber) {
        for (Branch branch : ListDataSource.branchList
                ) {
            if (branchNumber == branch.getBranchNum())
                return branch;
        }
        return null;
    }

    @Override
    public ArrayList<CarModel> getAllCarModels() {
        return (ArrayList<CarModel>) MySqlDataSource.carModelList;
        //return (ArrayList<CarModel>) ListDataSource.carModelList;
    }

    @Override
    public ArrayList<Client> getAllClients() {
        return (ArrayList<Client>) MySqlDataSource.clientList;
        //return (ArrayList<Client>) ListDataSource.clientList;
    }

    @Override
    public int checkclient(String username, String password) {
        /*for (Client client : ListDataSource.clientList) {
            if (username.compareTo(client.getUsername()) == 0 && password.compareTo(client.getPassword()) == 0) {
                return client.getId();
            }
            if (username.compareTo(client.getUsername()) == 0 && password.compareTo(client.getPassword()) != 0) {
                return 0;
            }
        }*/
        return -1;
    }

    @Override
    public ArrayList<Order> getAllOrders() {
        return (ArrayList<Order>) ListDataSource.orderList;
    }

    @Override
    public Updates addOrder(Order order) {
        return null;
    }


    @Override
    public boolean deleteOrder(int orderID) {
        Order orderTmp = null;
        for (Order order : ListDataSource.orderList) {
            if (order.getClientNum() == orderID) {
                orderTmp = order;
                break;
            }
        }
        return ListDataSource.orderList.remove(orderTmp);
    }

    @Override
    public Order getOrder(int clientID) {
        for (Order order : ListDataSource.orderList) {
            if (order.getClientNum() == clientID && order.isOrderOpen())
                return order;
        }
        return null;
    }

    @Override
    public boolean isThereAnOrder(int clientID) {
        for (Order order : ListDataSource.orderList) {
            if (order.getClientNum() == clientID && order.isOrderOpen())
                return true;
        }
        return false;
    }

    @Override
    public Updates updateOrder(Order order) {
        return null;
    }


    @Override
    public ArrayList<User> getAllUsers() {
        return (ArrayList<User>) MySqlDataSource.userList;
        //return (ArrayList<CarModel>) ListDataSource.carModelList;
    }

    @Override
    public Updates addUser(User user) {
        ContentValues contentValues = TakeNGoConst.UserToContentValues(user);
        try {
            String result = PHPtools.POST(WEB_URL + "/addnewuser.php", contentValues);
            if (result.contains("Duplicate entry")) return Updates.DUPLICATE;
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
    public Client getclientbyusername(String username) {
        return null;
    }

    @Override
    public Updates updateCar(Car car) {
        return null;
    }

    @Override
    public Updates addClient(Client client) {
        return null;
    }

    @Override
    public ArrayList<Branch> getAllBranches() {
        return (ArrayList<Branch>) MySqlDataSource.branchList;
        //return (ArrayList<Branch>) ListDataSource.branchList;
    }

    @Override
    public ArrayList<Car> getAllCars() {
        return (ArrayList<Car>) MySqlDataSource.carList;
        // return (ArrayList<Car>) ListDataSource.carList;
    }


}