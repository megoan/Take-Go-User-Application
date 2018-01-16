package com.example.fotij.userapplication.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.fotij.userapplication.R;
import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;
import com.example.fotij.userapplication.model.entities.MyDate;
import com.example.fotij.userapplication.model.entities.Order;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;

import java.util.Calendar;
import java.util.Date;


public class OrderActivity extends AppCompatActivity {
    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_LIST);
    TextView clientid;
    TextView carnum;
    TextView startdate;
    TextView enddate;
    TextView mileageStart;
    TextView mileageEnd;
    CheckBox gasRefill;
    TextView litersFilled;
    TextView payment;
    Button order;
    int clientID = MainActivity.getclientID();
    Order myorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myorder = backEndFunc.getOrder(clientID);
        if (myorder != null) {
            setContentView(R.layout.activity_order);
           /* Date currentTime = Calendar.getInstance().getTime();
            Intent intent = getIntent();
            carnum = findViewById(R.id.carNumShow);
            startdate = findViewById(R.id.dateStartShow);
            enddate = findViewById(R.id.dateEndShow);
            mileageStart = findViewById(R.id.mileageStartShow);
            mileageEnd = findViewById(R.id.mileageEndShow);
            gasRefill = findViewById(R.id.gasRefillShow);
            litersFilled = findViewById(R.id.litersFilledShow);
            payment = findViewById(R.id.paymentShow);
            //litersFilled.setVisibility(View.INVISIBLE);

            Calendar calendar = Calendar.getInstance();
            MyDate date = new MyDate(calendar.get(Calendar.DAY_OF_MONTH), MyDate.convertNumberToMonth(calendar.get(Calendar.MONTH)), calendar.get(Calendar.YEAR));
            enddate.setText(date.toString());

            carnum.setText(String.valueOf(intent.getStringExtra(TakeNGoConst.CarConst.CARNUM)));
            startdate.setText(intent.getStringExtra("start date"));
            mileageStart.setText(intent.getStringExtra("mileage start"));
            //enddate
            gasRefill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gasRefill.isChecked())
                        litersFilled.setVisibility(View.VISIBLE);
                    else
                        litersFilled.setVisibility(View.INVISIBLE);
                }
            });
            order = findViewById(R.id.orderbutton);
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int milaeageend = Integer.valueOf(mileageEnd.getText().toString());
                    int litersfilled = 0;
                    if (gasRefill.isChecked())
                        litersfilled = Integer.valueOf(litersFilled.getText().toString());
                    payment.setText(String.valueOf(carnum));
                }
            });*/
        } else {
            setContentView(R.layout.no_order_layout);
        }

        //startdate=findViewById(R.id.dateStartShow);


        // int id=intent.getIntExtra(TakeNGoConst.CarConst.CARNUM,0);
        //carnum.setText(String.valueOf(id));
        // startdate.setText((int) currentTime.getTime());
    }
}
