package com.example.fotij.userapplication.controller;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fotij.userapplication.R;
import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;
import com.example.fotij.userapplication.model.entities.Car;
import com.example.fotij.userapplication.model.entities.MyDate;
import com.example.fotij.userapplication.model.entities.Order;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {


    public OrderFragment() {
        // Required empty public constructor
    }

    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    TextView clientid;
    TextView carnum;
    TextView startDate;
    TextView endDate;
    TextView mileageStart;
    TextView litersFillTextView;
    EditText mileageEnd;
    CheckBox gasRefill;
    EditText litersFilled;
    TextView payment;
    Button closeorder;
    RatingBar ratingBar;
    int clientID = MainActivity.getclientID();
    Order myorder;
    LinearLayout hasorder;
    ConstraintLayout noorder;
    Car mycar;
    View view;

    String startDateString;
    String endDateString;
    double mileageStartValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        super.onCreate(savedInstanceState);
        new GetOrderFromWeb().execute();

        view = inflater.inflate(R.layout.fragment_order, container, false);
        hasorder = view.findViewById(R.id.hasorder);
        noorder = view.findViewById(R.id.noorder);
        ratingBar=view.findViewById(R.id.clientRating);
        carnum = view.findViewById(R.id.carNumShow);
        litersFillTextView = view.findViewById(R.id.litersFilled);
        litersFillTextView.setVisibility(View.GONE);
        startDate = view.findViewById(R.id.dateStartShow);
        closeorder = view.findViewById(R.id.closeorderbutton);
        endDate = view.findViewById(R.id.dateEndShow);
        Calendar calendar = Calendar.getInstance();
        MyDate date = new MyDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
        endDate.setText(date.dd_mm_yyyy());
        endDateString = date.dd_mm_yyyy();
        mileageStart = view.findViewById(R.id.mileageStartShow);
        mileageEnd = view.findViewById(R.id.mileageEndShow);
        mileageEnd.setText("");
        gasRefill = view.findViewById(R.id.gasRefillShow);
        litersFilled = view.findViewById(R.id.litersFilledShow);
        litersFilled.setText("");
        litersFilled.setVisibility(View.GONE);
        payment = view.findViewById(R.id.paymentShow);
        payment.setText("");

        mileageEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                boolean gasrefill = gasRefill.isChecked();
                Double litersfilled = null;
                if (litersFilled.getText().toString().length() > 0) {
                    litersfilled = Double.valueOf(litersFilled.getText().toString());
                }
                long resultdate = 0;
                try {
                    resultdate = daysBetween(convertStringToCalender(startDateString), convertStringToCalender(endDateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                double mileagend = 0;
                if (s.toString().length() > 0) {
                    mileagend += Double.parseDouble(s.toString());
                }
                double resultpayment = 0;
                if (mileagend > mileageStartValue) {
                    resultpayment = resultdate * mycar.getOneDayCost() + (mileagend - mileageStartValue) * mycar.getOneKilometerCost();
                }
                if (gasrefill && litersfilled != null) {
                    resultpayment -= litersfilled;
                }
                payment.setText(String.valueOf(resultpayment));

            }
        });

        litersFilled.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean gasrefill = gasRefill.isChecked();

                Double mileageEndValue = null;
                if (mileageEnd.getText().toString().length() > 0) {
                    mileageEndValue = Double.valueOf(mileageEnd.getText().toString());
                }
                long resultdate = 0;
                try {
                    resultdate = daysBetween(convertStringToCalender(startDateString), convertStringToCalender(endDateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                double resultpayment = 0;
                if (mileageEndValue != null && mileageEndValue > mileageStartValue) {
                    resultpayment = resultdate * mycar.getOneDayCost() + (mileageEndValue - mileageStartValue) * mycar.getOneKilometerCost();
                }
                if (gasrefill) {
                    double literspay = 0;
                    if (s.toString().length() > 0) {
                        literspay = Double.valueOf(s.toString());
                    }
                    resultpayment -= literspay;
                }
                payment.setText(String.valueOf(resultpayment));
            }
        });
        closeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payment2 = payment.getText().toString();
                if (payment2 == null || payment2.length() == 0 || Double.parseDouble(payment2) < 0) {
                    inputWarningDialog("must enter valid values!!!");
                    return;
                }

                boolean gasrefill = gasRefill.isChecked();
                String litersfilledstring = litersFilled.getText().toString();
                if (gasrefill && (litersfilledstring == null || litersfilledstring.length() == 0)) {
                    inputWarningDialog("must enter value for liters filled");
                    return;
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("Close Order")
                        .setMessage("Are you really want to close the order?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myorder.setDateEnd(String.valueOf(endDateString));
                                myorder.setGasRefill(gasRefill.isChecked());
                                if (litersFilled.getText().toString().compareTo("") != 0)
                                    myorder.setLitersFilled(Integer.valueOf(litersFilled.getText().toString()));
                                else
                                    myorder.setLitersFilled(0);
                                myorder.setMileageEnd(Double.valueOf(mileageEnd.getText().toString()));
                                myorder.setOrderOpen(false);
                                myorder.setPayment(Float.valueOf(payment.getText().toString()));
                                double rating=mycar.getRating();
                                int numOfRating=mycar.getNumOfRatings();
                                double clientRating=ratingBar.getRating();
                                double newRating=(rating*numOfRating+clientRating)/(numOfRating+1);
                                mycar.setRating(newRating);
                                mycar.setNumOfRatings(numOfRating+1);
                                mycar.setInUse(false);
                                mycar.setMileage(Double.valueOf(mileageEnd.getText().toString()));
                                new UpdateAndCloseOrderAsync().execute();
                            }
                        }).create().show();


            }
        });
        gasRefill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    litersFilled.setVisibility(View.VISIBLE);
                    litersFillTextView.setVisibility(View.VISIBLE);
                } else {
                    litersFilled.setVisibility(View.GONE);
                    litersFillTextView.setVisibility(View.GONE);
                }
                String literf = null;
                if (isChecked == false) {
                    literf = litersFilled.getText().toString();
                }
                if (literf != null && literf.length() > 0) {
                    double liters = Double.parseDouble(literf);
                    String payR = payment.getText().toString();
                    if (payR != null && payR.length() > 0) {
                        double pay = Double.parseDouble(payR);
                        payment.setText(String.valueOf(pay + liters));
                    }
                }
                litersFilled.setText("");
            }
        });
        return view;
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    public Calendar convertStringToCalender(String mDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = sdf.parse(mDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public void inputWarningDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invalid input!");
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class GetOrderFromWeb extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            myorder = backEndFunc.getOrder(clientID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (myorder != null) {
                hasorder.setVisibility(View.VISIBLE);
                ;
                mileageStartValue = myorder.getMileageStart();
                startDateString = myorder.getDateStart();
                new GetCarOfOrderFromWeb().execute();
                startDate.setText(myorder.getDateStart());
                mileageStart.setText(String.valueOf(myorder.getMileageStart()));
            }
            else
                noorder.setVisibility(View.VISIBLE);
        }
    }

    public class GetCarOfOrderFromWeb extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            mycar = backEndFunc.getCar(myorder.getCarNum());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carnum.setText(String.valueOf(myorder.getCarNum()));
        }
    }

    public class UpdateAndCloseOrderAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            backEndFunc.updateOrder(myorder);
            backEndFunc.updateCar(mycar);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "Order has been closed", Toast.LENGTH_SHORT).show();
            LinearLayout hasorder = view.findViewById(R.id.hasorder);
            hasorder.setVisibility(View.INVISIBLE);
            ConstraintLayout noorder = view.findViewById(R.id.noorder);
            noorder.setVisibility(View.VISIBLE);
        }
    }
}


