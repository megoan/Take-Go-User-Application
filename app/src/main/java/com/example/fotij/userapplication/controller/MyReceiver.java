package com.example.fotij.userapplication.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;
import com.example.fotij.userapplication.model.entities.Branch;
import com.example.fotij.userapplication.model.entities.Car;
import com.example.fotij.userapplication.model.entities.CarModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by fotij on 11/01/2018.
 */

public class MyReceiver extends BroadcastReceiver {
    boolean update=false;
    ArrayList<Branch> branches = new ArrayList<>();
    ArrayList<Car> choosedcars = new ArrayList<>();
    ArrayList<Car> cars = new ArrayList<>();
    ArrayList<CarModel> carModels = new ArrayList<>();
    Branch branchShow = new Branch();
    Car finalCar=new Car();
    CarModel finalCarModel=new CarModel();

    Branch firstBranch=new Branch();
    Car firstCar=new Car();
    CarModel firstCarModel=new CarModel();
    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LOG", "recivead servise");
        update=false;
      //  updateView(CheapestCar.cheapestCar,CheapestCar.originalCarModel,CheapestCar.originalBranch);
        //new GetAllBranchFromWeb().execute();

    }

    public class GetAllBranchFromWeb extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            branches = backEndFunc.getAllBranches();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sortBranchByDistance();
            if (branches.size() > 0) {
                branchShow = branches.get(0);
                if(branchShow.getBranchNum()!=firstBranch.getBranchNum()){
                    firstBranch=branchShow;
                    new  GetAllCarsFromWeb().execute();
                }

            }
        }
    }

    public class GetAllCarsFromWeb extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            cars = backEndFunc.getAllCars();
            carModels = backEndFunc.getAllCarModels();
            choosedcars.clear();
            for (Car car : cars) {
                if (branchShow.getCarIds().contains(car.getCarNum()) && !car.isInUse())
                    choosedcars.add(car);
            }
            sortCarsByPrice();
            if(choosedcars.size()>0)
            {
                finalCar=choosedcars.get(0);
                new GetAllCarModelsFromWeb().execute();

        }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            }

    }


    public class GetAllCarModelsFromWeb extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            carModels = backEndFunc.getAllCarModels();
                    for(CarModel carModel: carModels)
                    {
                        if(finalCar.getCarModel()==carModel.getCarModelCode())
                        {
                            finalCarModel=carModel;
                        }
                    }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                //updateView(finalCar,finalCarModel,branchShow);
        }

    }

    private void sortBranchByDistance() {
        if (MainActivity.mLastLocation == null) {

        } else {
            final double myLocationLatitude = MainActivity.mLastLocation.getLatitude();
            final double myLocationLongitude = MainActivity.mLastLocation.getLongitude();
            Collections.sort(branches, new Comparator<Branch>() {
                public int compare(Branch o1, Branch o2) {
                    return new Double(meterDistanceBetweenPoints(o1.getMyAddress().getLatitude(), o1.getMyAddress().getLongitude(), myLocationLatitude, myLocationLongitude)).compareTo(new Double(meterDistanceBetweenPoints(o2.getMyAddress().getLatitude(), o2.getMyAddress().getLongitude(), myLocationLatitude, myLocationLongitude)));
                }
            });

        }

    }

    private void sortCarsByPrice(){
        Collections.sort(choosedcars, new Comparator<Car>() {
            public int compare(Car o1, Car o2) {
                return new Double(o1.getOneDayCost()+o1.getOneKilometerCost()).compareTo(new Double(o2.getOneDayCost()+o2.getOneKilometerCost()));
            }
        });
    }

    public double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (double) (180.f / Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }
}
