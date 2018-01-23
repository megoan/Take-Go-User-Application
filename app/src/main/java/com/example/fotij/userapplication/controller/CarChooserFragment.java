package com.example.fotij.userapplication.controller;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.example.fotij.userapplication.model.entities.TakeNGoConst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarChooserFragment extends Fragment {

    private RecyclerView recyclerViewBranch;
    private RecyclerView recyclerViewCar;
    private BackEndFunc backEndFunc;
    public BranchRecyclerViewAdapter branchAdapter;
    public CarsInBranchViewAdapter caradApter;
    RecyclerView.LayoutManager BranchLayoutManager;
    RecyclerView.LayoutManager CarLayoutManager;
    ArrayList<Branch> branches;
    ArrayList<Car> cars;
    ArrayList<CarModel> carModels;
    ArrayList<Car> choosedcars;
    Branch branchShow;
    View view;
    NestedScrollView scrollView_branch;
    LinearLayout linearLayout_close_open;
    LinearLayout branchDetails;
    ImageView close_open;
    TextView branchname;

    ImageView branchimage;
    ProgressBar branchprogressbar;
    TextView branchaddress;
    TextView revenue;
    TextView numofcars;
    TextView established;
    TextView parkingspots;
    LinearLayout progressBar;
    String filter2;
    SearchView searchView;
    Button sort;
    Button filter;
    LinearLayout search_sort_filter_layout;
    String[] branchesCities;
    boolean[] branchesCitiesChecked;
    ArrayList<String>branchesCitiesSet=new ArrayList<>();
    Set<String> b;
    public Map<String,Boolean>branchesCom=new HashMap<String, Boolean>();

    public CarChooserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car_chooser, container, false);
        init();
        return view;
    }

    private void init() {
        backEndFunc = FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
        progressBar=view.findViewById(R.id.loadingBranches);
        new GetAllBranchFromWeb().execute();
        //
        search_sort_filter_layout = view.findViewById(R.id.search_sort_filter);
        sort = view.findViewById(R.id.sort);
        filter = view.findViewById(R.id.filter);
        searchView = view.findViewById(R.id.search);


        recyclerViewBranch = view.findViewById(R.id.branchrecyclerview);
        recyclerViewCar = view.findViewById(R.id.carinuseview);
        scrollView_branch = view.findViewById(R.id.branchlistscroolview);
        linearLayout_close_open = view.findViewById(R.id.open_close_list_linear);
        branchDetails = view.findViewById(R.id.branchDetails);
        close_open = view.findViewById(R.id.open_close_list);
        branchname = view.findViewById(R.id.branchname);

        linearLayout_close_open.setVisibility(View.GONE);

        scrollView_branch.setVisibility(View.VISIBLE);
        /*close_open.setVisibility(View.GONE);
        branchname.setVisibility(View.GONE);*/
        //

        branchimage = view.findViewById(R.id.branchimage);
        branchprogressbar = view.findViewById(R.id.downloadProgressBar);
        branchaddress = view.findViewById(R.id.cardBranchCity);
        revenue = view.findViewById(R.id.cardBranchRevenue);
        established = view.findViewById(R.id.EstablisheddateDate);
        parkingspots = view.findViewById(R.id.parkingspotsnumber);
        numofcars = view.findViewById(R.id.cardBranchCarNum);


        BranchLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        CarLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewBranch.setLayoutManager(BranchLayoutManager);
        recyclerViewCar.setLayoutManager(CarLayoutManager);


        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        close_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_sort_filter_layout.setVisibility(View.VISIBLE);
                linearLayout_close_open.setVisibility(View.GONE);
                scrollView_branch.setVisibility(View.VISIBLE);
                branchDetails.setVisibility(View.GONE);
                caradApter = new CarsInBranchViewAdapter(new ArrayList<Car>(), getActivity(), CarChooserFragment.this, new ArrayList<CarModel>());
                recyclerViewCar.setAdapter(caradApter);
            }
        });
        //

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                branchAdapter.getFilter().filter(newText);
                branchAdapter.notifyDataSetChanged();
                return false;
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sort by:");

                // add a radio button list
                String[] options = {"Number of open cars", "Distance"};
                int checkedItem = 0; // cow
                builder.setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user checked an item
                    }
                });
                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        if (checkedItem == "Number of open cars") {
                            sortBranchByNumberOfCars();
                        } else if (checkedItem == "Distance") {
                            sortBranchByDistance();
                        }


                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter2 = "";
                updateAbstractFilter(branchesCom, branchesCitiesSet);
                branchesCities = new String[branchesCom.size()];
                branchesCitiesChecked = new boolean[branchesCom.size()];
                int i = 0;
                for (Map.Entry<String, Boolean> entry : branchesCom.entrySet()
                        ) {
                    branchesCities[i] = entry.getKey();
                    branchesCitiesChecked[i] = entry.getValue();
                    i++;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Filter by:");

                // add a checkbox button list
                builder.setMultiChoiceItems(branchesCities, branchesCitiesChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Update the current focused item's checked status
                        branchesCitiesChecked[which] = isChecked;
                        // Get the current focused item
                        String currentItem = branchesCities[which];
                        // Notify the current action
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < branchesCities.length; i++) {
                            branchesCom.put(branchesCities[i], branchesCitiesChecked[i]);
                            if (branchesCitiesChecked[i] == true) {
                                filter2 += branchesCities[i];
                            }
                        }
                        if (filter.length() == 0) filter2 = "you got no cars dude";
                        branchAdapter.getFilter().filter(filter2);
                        branchAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void sortBranchByNumberOfCars() {
        Collections.sort(branches, new Comparator<Branch>() {
            public int compare(Branch o1, Branch o2) {

                return String.valueOf(o1.getCarIds().size()).compareTo(String.valueOf(o2.getCarIds().size()));
            }
        });
        branchAdapter.objects = branches;
        branchAdapter.notifyDataSetChanged();
    }

    private void sortBranchByDistance() {

    }

    public void showBranchDetails(Branch branch) {
        choosedcars = new ArrayList<>();
        branchShow = branch;
        search_sort_filter_layout.setVisibility(View.GONE);
        branchprogressbar.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
                .load(branchShow.getImgURL())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.rental)
                .placeholder(R.drawable.rental)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        branchprogressbar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        branchprogressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(branchimage);
        numofcars.setText(String.valueOf(branchShow.getCarIds().size()));
        branchaddress.setText(branchShow.getMyAddress().getLocality() + " ," + branchShow.getMyAddress().getCountry());
        revenue.setText(String.valueOf(branchShow.getBranchRevenue()));
        established.setText(String.valueOf(branchShow.getEstablishedDate()));
        parkingspots.setText(String.valueOf(branchShow.getParkingSpotsNum()));

        new GetAllCarAndModelFromWeb().execute();
        branchDetails.setVisibility(View.VISIBLE);
        linearLayout_close_open.setVisibility(View.VISIBLE);
        scrollView_branch.setVisibility(View.GONE);
        branchname.setText(branchShow.getMyAddress().getAddressName());
    }

    public void makeAnOrder(Car car) {
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        intent.putExtra(TakeNGoConst.CarConst.CARNUM, car.getCarNum());
        intent.putExtra(TakeNGoConst.CarConst.MILEAGE, car.getMileage());
        intent.putExtra(TakeNGoConst.CarConst.ONEKILOMETERCOST, car.getOneKilometerCost());
        startActivity(intent);
    }

    public class GetAllBranchFromWeb extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            branches = backEndFunc.getAllBranches();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            branchAdapter = new BranchRecyclerViewAdapter(branches, getActivity(), CarChooserFragment.this);
            recyclerViewBranch.setAdapter(branchAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }

    public class GetAllCarAndModelFromWeb extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            cars = backEndFunc.getAllCars();
            carModels = backEndFunc.getAllCarModels();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (Car car : cars) {
                if (branchShow.getCarIds().contains(car.getCarNum()) && !car.isInUse())
                    choosedcars.add(car);
            }
            caradApter = new CarsInBranchViewAdapter(choosedcars, getActivity(), CarChooserFragment.this, carModels);
            recyclerViewCar.setAdapter(caradApter);
        }
    }

    public void updateAbstractFilter(Map<String, Boolean> hashMap, ArrayList<String> set) {

        set.clear();


        for (Branch branch : branches
                ) {
            branchesCitiesSet.add(branch.getMyAddress().getLocality());
        }



        b = new LinkedHashSet<>(set);
        set.clear();
        set.addAll(b);

        for (int i = 0; i < set.size(); i++) {
            if (!hashMap.containsKey(set.get(i))) {
                hashMap.put(set.get(i), true);
            }
        }
        boolean check;
        ArrayList<String> r = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : hashMap.entrySet()
                ) {
            check = false;
            for (int j = 0; j < set.size(); j++) {
                if (entry.getKey() == set.get(j)) {
                    check = true;
                }
            }
            if (check == false) {
                r.add(entry.getKey());
            }
        }
        for (String s : r
                ) {
            hashMap.remove(s);
        }
    }
    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
