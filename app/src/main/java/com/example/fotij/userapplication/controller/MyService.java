package com.example.fotij.userapplication.controller;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class MyService extends IntentService {
    boolean isRun = false;
    boolean update = false;
    ArrayList<Branch> branches = new ArrayList<>();
    ArrayList<Car> choosedcars = new ArrayList<>();
    ArrayList<Car> cars = new ArrayList<>();
    ArrayList<CarModel> carModels = new ArrayList<>();
    Branch branchShow = new Branch();
    Car finalCar = new Car();
    CarModel finalCarModel = new CarModel();


    CarModel firstCarModel = new CarModel();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyService(String name) {
        super(name);
    }

    public MyService(String name, boolean isRun) {
        super(name);
        this.isRun = isRun;
    }

    public MyService() {
        super("mySevise");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        isRun = true;
        Toast.makeText(MyService.this,"service started",Toast.LENGTH_LONG).show();
        //return START_NOT_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        if (CheapestCar.location!=null &&foundCheaperCar())
                            sendBroadcast(new Intent(getBaseContext(), MainActivity.MyReceiver.class));
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    private boolean foundCheaperCar() {

        boolean foundNewCar=false;
        BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
        branches = backEndFunc.getAllBranches();

        if (branches.size() > 0) {
            int i = 0;
            for (; i < branches.size(); i++) {
                if (branches.get(i).isInUse()) break;
            }
            branchShow = branches.get(i);
            if (branchShow.getBranchNum() != CheapestCar.originalBranch.getBranchNum() || branchShow.getMyAddress().getAddressName() != CheapestCar.originalBranch.getMyAddress().getAddressName()) {
                CheapestCar.originalBranch = branchShow;

                cars = backEndFunc.getAllCars();
                int m=9;
                m++;
                carModels = backEndFunc.getAllCarModels();
                choosedcars.clear();
                for (Car car2 : cars) {
                    if (CheapestCar.originalBranch.getCarIds().contains(car2.getCarNum()) && !car2.isInUse())
                        choosedcars.add(car2);
                }
                sortCarsByPrice();
                if (choosedcars.size() > 0) {
                    finalCar = choosedcars.get(0);
                    if (CheapestCar.cheapestCar.getCarNum() != finalCar.getCarNum()) {
                        foundNewCar=true;
                        if (!Car.compareTwoCars(CheapestCar.cheapestCar, finalCar)) {
                            CheapestCar.cheapestCar = finalCar;
                            carModels = backEndFunc.getAllCarModels();
                            for (CarModel carModel : carModels) {
                                if (finalCar.getCarModel() == carModel.getCarModelCode()) {
                                    CheapestCar.originalCarModel = carModel;
                                }
                            }
                        }

                    }
                }
            }
        }

        return foundNewCar;
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

    private void sortCarsByPrice() {
        Collections.sort(choosedcars, new Comparator<Car>() {
            public int compare(Car o1, Car o2) {
                return new Double(o1.getOneDayCost() + o1.getOneKilometerCost()).compareTo(new Double(o2.getOneDayCost() + o2.getOneKilometerCost()));
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

    @Override
    public void onDestroy() {
        isRun=false;
        Toast.makeText(MyService.this,"service stopped",Toast.LENGTH_LONG).show();
        super.onDestroy();

    }
}

