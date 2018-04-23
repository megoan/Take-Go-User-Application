package com.example.fotij.userapplication.controller;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fotij.userapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutNContentFragment extends Fragment {


    public AboutNContentFragment() {
        // Required empty public constructor
    }

    ImageView email;
    ImageView phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_ncontent, container, false);
        email = view.findViewById(R.id.emailimageview);
        phone = view.findViewById(R.id.phoneimageview);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0525629459"));
                startActivity(intent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(intent.EXTRA_EMAIL,"yosef.mehrzadi@gmail.com");
                intent.putExtra(intent.EXTRA_SUBJECT,"Hello TakeNGo");
                //intent.putExtra(intent.EXTRA_TEXT,"Hello TakeNGo");
                intent.setType("application/octet-stream");
                Intent chooser=intent.createChooser(intent,"Send Email");
                startActivity(chooser);
            }
        });
        return view;
    }
}
