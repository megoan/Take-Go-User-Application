package com.example.fotij.userapplication.controller;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
import com.example.fotij.userapplication.model.datasource.MySqlDataSource;
import com.example.fotij.userapplication.model.entities.Branch;
import com.example.fotij.userapplication.model.entities.Car;
import com.example.fotij.userapplication.model.entities.CarModel;
import com.example.fotij.userapplication.model.entities.MyDate;
import com.example.fotij.userapplication.model.entities.Order;
import com.example.fotij.userapplication.model.entities.TakeNGoConst;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    static int clientID;
    BackEndFunc backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    MySqlDataSource mySqlDataSource;
    int i=0;

    LocationManager locationManagerInternet;
    LocationManager locationManagerGPS;
    LocationListener locationListener;
    public static boolean otherFragment=false;
    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_RQUEST = 7172;
    private boolean mRequestLocationUpdates = true;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    public static Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;


    static LinearLayout cheapLayout;

    static TextView name;
    static TextView model;
    static TextView mileage;
    static TextView one_kilometer;
    static TextView one_day;
    static TextView year;
    static TextView passengers;
    static TextView luggage;
    static TextView ac;
    static TextView engine;
    static TextView transmission;
    static RatingBar ratingBar;
    static TextView carCardRating;
    static TextView carCardNumberOfRatings;
    static ImageView image_car;
    LinearLayout linearLayout;
    public static LinearLayout loadingCarLayout;
    static ProgressBar progressBar;
    ImageView down_and_up;
    TextView more_less_text;
    public static Branch branch;
    static Activity activity;
    public static Car firstCar=new Car();
    Order order=new Order();
    public static Branch firstBranch=new Branch();
    static TextView address;
    ImageView imageView;
    Button orderNow;
     Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        intent=new Intent(this,MyService.class);
        cheapLayout=findViewById(R.id.cheapLayout);

        name = findViewById(R.id.nameinfo);
        model = findViewById(R.id.modelinfo);
        mileage = findViewById(R.id.mileageinfo);
        one_kilometer = findViewById(R.id.onekilometercostinfo);
        one_day = findViewById(R.id.onedaycostinfo);
        year = findViewById(R.id.yearinfo);
        passengers = findViewById(R.id.passengersinfo);
        luggage = findViewById(R.id.luggageinfo);
        ac = findViewById(R.id.acinfo);
        engine = findViewById(R.id.engineDisplacementinfo);
        transmission = findViewById(R.id.transmissioninfo);
        ratingBar = findViewById(R.id.carCardratingBar);
        carCardRating = findViewById(R.id.carCardRating);
        carCardNumberOfRatings = findViewById(R.id.carCardNumberOfRatings);
        image_car = findViewById(R.id.carimageView);
        linearLayout = findViewById(R.id.expandableView);
        progressBar = findViewById(R.id.downloadProgressBarCar);
        down_and_up = findViewById(R.id.down_and_up_button);
        more_less_text = findViewById(R.id.more_less_info_text);
        loadingCarLayout=findViewById(R.id.loadingCar);
        loadingCarLayout.setVisibility(View.VISIBLE);
        imageView=findViewById(R.id.imageviewmap);
        address=findViewById(R.id.textviewaddress);
        cheapLayout.setVisibility(View.GONE);
        orderNow=findViewById(R.id.orderNow);
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackGroundAddOrder().execute();
            }
        });
       if(branch==null){
           //cheapLayout.setVisibility(View.GONE);
       }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "geo: "+String.valueOf(branch.getMyAddress().getLatitude()) + "," + String.valueOf(branch.getMyAddress().getLongitude());
                Uri uri1=Uri.parse(uri);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri1);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        down_and_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout.getVisibility() == View.GONE) {
                    linearLayout.setVisibility(View.VISIBLE);
                    down_and_up.setImageResource(R.drawable.ic_up_arrow);
                    more_less_text.setText(getResources().getString(R.string.less_info));
                } else {
                    linearLayout.setVisibility(View.GONE);
                    down_and_up.setImageResource(R.drawable.ic_down_button);
                    more_less_text.setText(getResources().getString(R.string.more_info));
                }
            }
        });



        new GetFromWeb().execute();

        locationManagerInternet = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManagerGPS = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
            }
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };





        Intent intent = getIntent();
        clientID = intent.getIntExtra(TakeNGoConst.ClientConst.ID, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_info) {
            otherFragment=true;
            cheapLayout.setVisibility(View.GONE);
            setTitle("Info & contacts");
            AboutNContentFragment aboutNContentFragment = new AboutNContentFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, aboutNContentFragment, "fragment 1");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_Exit) {
            otherFragment=true;
            cheapLayout.setVisibility(View.GONE);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Exit")
                    .setMessage("Are you really want to exit?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        } else if (id == R.id.nav_car_selector) {
            otherFragment=true;
            cheapLayout.setVisibility(View.GONE);
            setTitle("Car Selector");
            CarChooserFragment carChooserFragment = new CarChooserFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, carChooserFragment, "fragment 2");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_order) {
            otherFragment=true;
            cheapLayout.setVisibility(View.GONE);
            setTitle("My Order");
            OrderFragment orderFragment = new OrderFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, orderFragment, "fragment 3");
            fragmentTransaction.commit();
        }
        else if(id==R.id.home){
            otherFragment=false;
            cheapLayout.setVisibility(View.VISIBLE);
            setTitle("Home Page");
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, homeFragment, "fragment 4");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Exit")
                    .setMessage("Are you really want to exit?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
           // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Log_out) {
            Intent intent=new Intent(this,LoginActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            finish();
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static int getclientID()
    {
        return clientID;
    }

    public class GetFromWeb extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            mySqlDataSource=new MySqlDataSource();
            return null;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                    }
                    //setGPS();

                }
            }
            break;
        }
    }
    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

    private boolean checkPlayServices() {
        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode)) {
                GooglePlayServicesUtil.getErrorDialog(resultcode, this, PLAY_SERVICES_RESOLUTION_RQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "this device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void getGoogleLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        CheapestCar.location=mLastLocation;
        i++;

        if(i==1)
        {
            //Intent intent=new Intent(this,MyService.class);
            startService(intent);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getGoogleLocation();
        if (mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        CheapestCar.location=mLastLocation;
        i++;
        if(i==1)
        {
            startService(intent);
        }

        //Toast.makeText(getApplicationContext(),"Latitude: "+mLastLocation.getLatitude()+" "+"Longtidude: "+mLastLocation.getLongitude(),Toast.LENGTH_SHORT).show();
        getGoogleLocation();
    }
    @Override
    protected void onStart() {
        switchOnGPS();
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();

        }

        // getGoogleLocation();
    }

    @Override
    protected void onStop() {

        if (googleApiClient!=null) {
            if(googleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                if (googleApiClient != null) {
                    googleApiClient.disconnect();
                }
            }
        }
        //Intent intent = new Intent(MainActivity.this, MyService.class);
        stopService(intent);
        CheapestCar.cheapestCar=new Car();
        CheapestCar.originalCarModel=new CarModel();
        //CheapestCar.location=null;
        CheapestCar.originalBranch=new Branch();
        i=0;
        super.onStop();
    }

    public void switchOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    mRequestLocationUpdates = true;

                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:{

                            break;
                        }
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            try {
                                resolvableApiException.startResolutionForResult(MainActivity.this, 11);

                            } catch (IntentSender.SendIntentException e1) {
                                e1.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //open setting and switch on GPS manually
                            break;
                    }
                }
            }
        });
        //Give permission to access GPS
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 11);

    }














    public static class MyReceiver extends BroadcastReceiver {
        boolean update=false;
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG", "recivead servise");
            update=false;
            updateView(CheapestCar.cheapestCar,CheapestCar.originalCarModel,CheapestCar.originalBranch);
        }
    }

    private static void updateView(Car car, CarModel carModel, Branch branchShow) {
        if (otherFragment==false) {
            cheapLayout.setVisibility(View.VISIBLE);

        }

        branch=branchShow;
        address.setText(branch.getMyAddress().getAddressName());

        name.setText(carModel.getCompanyName());
        model.setText(carModel.getCarModelName());
        mileage.setText(String.valueOf(car.getMileage()));
        one_kilometer.setText(String.valueOf(car.getOneKilometerCost()));
        one_day.setText(String.valueOf(car.getOneDayCost()));
        year.setText(String.valueOf(car.getYear()));
        passengers.setText(String.valueOf(carModel.getPassengers()));
        luggage.setText(String.valueOf(carModel.getLuggage()));
        ac.setText(String.valueOf(carModel.isAc()));
        engine.setText(String.valueOf(carModel.getEngineDisplacement()));
        transmission.setText(String.valueOf(carModel.getTransmission()));
        ratingBar.setRating((float) car.getRating());
        carCardRating.setText(String.valueOf(car.getRating()));
        carCardNumberOfRatings.setText(String.valueOf(car.getNumOfRatings()));

        //loadingCarLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(activity)
                .load(car.getImgURL())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.default_car_image)
                .placeholder(R.drawable.default_car_image)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        loadingCarLayout.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        loadingCarLayout.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(image_car);
    }




    public class BackGroundAddOrder extends AsyncTask<Void, Void, Void> {
        boolean hasorder;

        @Override
        protected Void doInBackground(Void... voids) {
            hasorder = backEndFunc.isThereAnOrder(clientID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!hasorder) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Order");
                builder.setMessage("Are you sure you want to make an order?" + "\n" + "Car ID: "
                        + firstCar.getCarNum() + "\n" + "Mileage: " + firstCar.getMileage() + "\n" + "One day cost: " + firstCar.getOneDayCost());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();

                        int month=calendar.get(Calendar.MONTH);
                        MyDate date = new MyDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
                        order = new Order(clientID, true, firstCar.getCarNum(), date.dd_mm_yyyy(),
                                "", firstCar.getMileage(), -1, false, -1, -1, -1);
                        firstCar.setInUse(true);
                        new BackGroundNewOrder().execute();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                //carChooserFragment.makeAnOrder(car);
            } else
                Toast.makeText(MainActivity.this, "One order per client only", Toast.LENGTH_SHORT).show();


        }
    }
    public class BackGroundNewOrder extends AsyncTask<Void, Void, Void> {
        Branch branch;

        @Override
        protected Void doInBackground(Void... voids) {
            backEndFunc.addOrder(order);
            backEndFunc.updateCar(firstCar);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "New Order Added", Toast.LENGTH_SHORT).show();

            //carChooserFragment.showBranchDetails(branch);
        }
    }

}
