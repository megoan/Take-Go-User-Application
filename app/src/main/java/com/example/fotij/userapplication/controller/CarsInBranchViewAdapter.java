package com.example.fotij.userapplication.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.fotij.userapplication.R;
import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;
import com.example.fotij.userapplication.model.backend.SelectedDataSource;
import com.example.fotij.userapplication.model.entities.Branch;
import com.example.fotij.userapplication.model.entities.Car;
import com.example.fotij.userapplication.model.entities.CarModel;
import com.example.fotij.userapplication.model.entities.MyDate;
import com.example.fotij.userapplication.model.entities.Order;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by fotij on 27/12/2017.
 */

public class CarsInBranchViewAdapter extends RecyclerView.Adapter<CarsInBranchViewAdapter.ViewHolder> {

    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    public ArrayList<Car> objects;
    private Context mContext;
    private CarChooserFragment carChooserFragment;
    Order order;
    int branchid;
    Car car;
    int clientid = MainActivity.getclientID();
    ArrayList<CarModel> carModels;

    public CarsInBranchViewAdapter(ArrayList<Car> objects, Context context, CarChooserFragment carChooserFragment, ArrayList<CarModel> carModels) {
        this.objects = objects;
        this.mContext = context;
        this.carChooserFragment = carChooserFragment;
        this.carModels = carModels;
    }

    @Override
    public CarsInBranchViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_card_layout, parent, false);
        return new CarsInBranchViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CarsInBranchViewAdapter.ViewHolder holder, final int position) {
        car = objects.get(position);
        CarModel carModel = new CarModel();
        branchid = car.getBranchNum();
        if (car != null) {
            int carmodelid = car.getCarModel();
            for (CarModel carmodel : carModels) {
                if (carmodel.getCarModelCode() == carmodelid) {
                    carModel = carmodel;
                    break;
                }
            }
            holder.name.setText(carModel.getCompanyName());
            holder.model.setText(carModel.getCarModelName());
            holder.mileage.setText(String.valueOf(car.getMileage()));
            holder.one_kilometer.setText(String.valueOf(car.getOneKilometerCost()));
            holder.one_day.setText(String.valueOf(car.getOneDayCost()));
            holder.year.setText(String.valueOf(car.getYear()));
            holder.passengers.setText(String.valueOf(carModel.getPassengers()));
            holder.luggage.setText(String.valueOf(carModel.getLuggage()));
            holder.ac.setText(String.valueOf(carModel.isAc()));
            holder.engine.setText(String.valueOf(carModel.getEngineDisplacement()));
            holder.transmission.setText(String.valueOf(carModel.getTransmission()));
            holder.ratingBar.setRating((float) car.getRating());
            holder.carCardRating.setText(String.valueOf(car.getRating()));
            holder.carCardNumberOfRatings.setText(String.valueOf(car.getNumOfRatings()));


            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(car.getImgURL())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.default_car_image)
                    .placeholder(R.drawable.default_car_image)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false; // important to return false so the error placeholder can be placed
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.image_car);
        }

        holder.down_and_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.linearLayout.getVisibility() == View.GONE) {
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.down_and_up.setImageResource(R.drawable.ic_up_arrow);
                    holder.more_less_text.setText(mContext.getResources().getString(R.string.less_info));
                } else {
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.down_and_up.setImageResource(R.drawable.ic_down_button);
                    holder.more_less_text.setText(mContext.getResources().getString(R.string.more_info));
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car=objects.get(position);
                new BackGroundAddOrder().execute();

            }

        });

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView model;
        TextView mileage;
        TextView one_kilometer;
        TextView one_day;
        TextView year;
        TextView passengers;
        TextView luggage;
        TextView ac;
        TextView engine;
        TextView transmission;
        RatingBar ratingBar;
        TextView carCardRating;
        TextView carCardNumberOfRatings;
        ImageView image_car;
        LinearLayout linearLayout;
        ProgressBar progressBar;
        ImageView down_and_up;
        TextView more_less_text;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameinfo);
            model = itemView.findViewById(R.id.modelinfo);
            mileage = itemView.findViewById(R.id.mileageinfo);
            one_kilometer = itemView.findViewById(R.id.onekilometercostinfo);
            one_day = itemView.findViewById(R.id.onedaycostinfo);
            year = itemView.findViewById(R.id.yearinfo);
            passengers = itemView.findViewById(R.id.passengersinfo);
            luggage = itemView.findViewById(R.id.luggageinfo);
            ac = itemView.findViewById(R.id.acinfo);
            engine = itemView.findViewById(R.id.engineDisplacementinfo);
            transmission = itemView.findViewById(R.id.transmissioninfo);
            ratingBar = itemView.findViewById(R.id.carCardratingBar);
            carCardRating = itemView.findViewById(R.id.carCardRating);
            carCardNumberOfRatings = itemView.findViewById(R.id.carCardNumberOfRatings);
            image_car = itemView.findViewById(R.id.carimageView);
            linearLayout = itemView.findViewById(R.id.expandableView);
            progressBar = itemView.findViewById(R.id.downloadProgressBarCar);
            down_and_up = itemView.findViewById(R.id.down_and_up_button);
            more_less_text = itemView.findViewById(R.id.more_less_info_text);
        }
    }

    public class BackGroundNewOrder extends AsyncTask<Void, Void, Void> {
        Branch branch;

        @Override
        protected Void doInBackground(Void... voids) {
            backEndFunc.addOrder(order);
            backEndFunc.updateCar(car);
            objects = backEndFunc.getAllCars();
            branch = backEndFunc.getBranch(branchid);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(mContext, "New Order Added", Toast.LENGTH_SHORT).show();
            ArrayList<Car> choosedcars = new ArrayList<>();
            for (Car car : objects) {
                if (branch.getCarIds().contains(car.getCarNum()) && !car.isInUse())
                    choosedcars.add(car);
            }
            objects=choosedcars;
            notifyDataSetChanged();
            //carChooserFragment.showBranchDetails(branch);
        }
    }


    public class BackGroundAddOrder extends AsyncTask<Void, Void, Void> {
        boolean hasorder;

        @Override
        protected Void doInBackground(Void... voids) {
            hasorder = backEndFunc.isThereAnOrder(clientid);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!hasorder) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Order");
                builder.setMessage("Are you sure you want to make an order?" + "\n" + "Car ID: "
                        + car.getCarNum() + "\n" + "Mileage: " + car.getMileage() + "\n" + "One day cost: " + car.getOneDayCost());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();

                        int month=calendar.get(Calendar.MONTH);
                        MyDate date = new MyDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
                        order = new Order(clientid, true, car.getCarNum(), date.dd_mm_yyyy(),
                                "", car.getMileage(), -1, false, -1, -1, -1);
                        car.setInUse(true);
                        new BackGroundNewOrder().execute();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                //carChooserFragment.makeAnOrder(car);
            } else
                Toast.makeText(mContext, "One order per client only", Toast.LENGTH_SHORT).show();


        }
    }


}




