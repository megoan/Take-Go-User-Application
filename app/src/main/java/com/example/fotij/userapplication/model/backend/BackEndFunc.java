package com.example.fotij.userapplication.model.backend;

import com.example.fotij.userapplication.model.entities.Branch;
import com.example.fotij.userapplication.model.entities.Car;
import com.example.fotij.userapplication.model.entities.CarModel;
import com.example.fotij.userapplication.model.entities.Client;
import com.example.fotij.userapplication.model.entities.Order;
import com.example.fotij.userapplication.model.entities.User;

import java.util.ArrayList;

/**
 * Created by fotij on 23/12/2017.
 */

public interface BackEndFunc {

    public boolean updateClient(Client client);

    public boolean deleteClient(int clientID);

    public Client getClient(int id);

   // public int addClient(Client client);

    public Car getCar(int carNumber);

    public Branch getBranch(int branchNumber);

    public ArrayList<CarModel> getAllCarModels();

    public ArrayList<Client> getAllClients();

    public ArrayList<Branch> getAllBranches();

    public ArrayList<Car> getAllCars();

    public int checkclient(String username, String password);

    public ArrayList<Order> getAllOrders();

    public Updates addOrder(Order order);

    public boolean deleteOrder(int orderID);

    public Order getOrder(int clientID);

    public boolean isThereAnOrder(int clientID);

    public Updates updateOrder(Order order);

    public ArrayList<User> getAllUsers();

    public Updates addUser(User user);

    public User getUser(String username);

   public Client getclientbyusername(String username);

    public Updates updateCar(Car car);
   public Updates addClient(Client client);

  //  public Updates updateCar(Car car);




     /*   public boolean addCarModel(CarModel carModel);
    public boolean addCar(Car car);
    public boolean addCar(Car car, int branchID);
    public boolean addBranch(Branch branch);*/
   /* public boolean updateCarModel(CarModel carModel);

    public void updateCar(Car car,int originalCarModel);
    public boolean updateBranch(Branch branch);*/


    // public boolean deleteCarModel(int carModelID);
    //public boolean deleteCar(int carID);
    // public boolean deleteBranch(int branchID);


  /*  public CarModel getCarModel(int carModelNumber);
    public Car getCar(int carNumber);
    */
    // public boolean removeCarFromBranch(int carID, int branch);
    // public boolean addCarToBranch(int carID, int branch);


}
