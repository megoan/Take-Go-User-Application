package com.example.fotij.userapplication.model.datasource;

import com.example.fotij.userapplication.model.entities.Branch;
import com.example.fotij.userapplication.model.entities.Car;
import com.example.fotij.userapplication.model.entities.CarModel;
import com.example.fotij.userapplication.model.entities.Client;
import com.example.fotij.userapplication.model.entities.Order;
import com.example.fotij.userapplication.model.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fotij on 23/12/2017.
 */
public abstract class DataSource {
    public static List<CarModel> carModelList;
    public static List<Client>clientList;
    public static List<Branch>branchList;
    public static List<Car>carList;
    public static List<Order>orderList;
    public static List<User>userList;

    public DataSource()
    {
        carList=new ArrayList<>();
        carModelList=new ArrayList<>();
        branchList=new ArrayList<>();
        orderList=new ArrayList<>();
        clientList=new ArrayList<>();
        userList=new ArrayList<>();
    }

    public abstract void initialize();
}
