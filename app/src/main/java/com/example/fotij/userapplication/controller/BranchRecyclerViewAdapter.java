package com.example.fotij.userapplication.controller;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fotij.userapplication.R;
import com.example.fotij.userapplication.model.backend.BackEndFunc;
import com.example.fotij.userapplication.model.backend.DataSourceType;
import com.example.fotij.userapplication.model.backend.FactoryMethod;
import com.example.fotij.userapplication.model.backend.SelectedDataSource;
import com.example.fotij.userapplication.model.entities.Branch;

import java.util.ArrayList;

/**
 * Created by fotij on 24/12/2017.
 */

public class BranchRecyclerViewAdapter extends RecyclerView.Adapter<BranchRecyclerViewAdapter.ViewHolder> implements Filterable{
    BackEndFunc backEndFunc= FactoryMethod.getBackEndFunc(DataSourceType.DATA_INTERNET);
    public ArrayList<Branch> objects;
    private Context mContext;
    private CarChooserFragment carChooserFragment;
    MyFilter myFilter;
    Branch branch;

    public BranchRecyclerViewAdapter(ArrayList<Branch> objects, Context context, CarChooserFragment carChooserFragment)
    {
        this.objects=objects;
        this.mContext=context;
        this.carChooserFragment=carChooserFragment;
        //carChooserFragment.getView().setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public BranchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_layout,parent,false);
        return new BranchRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BranchRecyclerViewAdapter.ViewHolder holder, final int position) {
        branch = objects.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branch = objects.get(position);
                carChooserFragment.showBranchDetails(branch);
            }
        });
        holder.address.setText(branch.getMyAddress().getAddressName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch = objects.get(position);
                String uri = "http://maps.google.com/maps?q="+branch.getMyAddress().getAddressName();
                Uri uri1=Uri.parse(uri);
                carChooserFragment.showMap(uri1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    static  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView address;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageviewmap);
            address=itemView.findViewById(R.id.textviewaddress);
        }
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null) myFilter = new MyFilter();
        return myFilter;
    }


    public class MyFilter extends Filter{
        FilterResults results;
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            results = new FilterResults();
            ArrayList<Branch> branchArrayList=backEndFunc.getAllBranches();
            if (charSequence == null || charSequence.length() == 0) {
                results.values = branchArrayList;
                results.count = branchArrayList.size();
            } else {
                ArrayList<Branch> filteredBranch = new ArrayList<Branch>();
                for (Branch branch : branchArrayList) {
                    String s = (branch.getMyAddress().getAddressName());
                    if (s.toLowerCase().startsWith(charSequence.toString().toLowerCase())|| s.toLowerCase().contains(charSequence.toString().toLowerCase()) || charSequence.toString().toLowerCase().toLowerCase().contains(branch.getMyAddress().getLocality().toLowerCase())) {
                        filteredBranch.add(branch);
                    }
                }
                results.values = filteredBranch;
                results.count = filteredBranch.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            objects = new ArrayList<Branch>((ArrayList<Branch>) results.values);
            notifyDataSetChanged();
        }
    }
}
