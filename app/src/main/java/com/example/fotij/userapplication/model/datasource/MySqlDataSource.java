package com.example.fotij.userapplication.model.datasource;

import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;

/**
 * Created by fotij on 23/12/2017.
 */

public class MySqlDataSource extends DataSource {
    public MySqlDataSource() {
        super();
        initialize();
    }

    @Override
    public void initialize() {
        BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
        //clientList = backEndFunc.getAllClients();
        //carModelList=backEndFunc.getAllCarModels();
        //carList = backEndFunc.getAllCars();
       // userList=backEndFunc.getAllUsers();
        //carModelList = backEndFunc.getAllCarModels();
       // branchList = backEndFunc.getAllBranches();
    }
}

