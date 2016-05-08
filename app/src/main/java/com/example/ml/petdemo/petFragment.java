package com.example.ml.petdemo;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.TextView;

public class petFragment extends Fragment {
    private View view;
    private CardView cardpetname;
    private CardView cardpetyear;
    private TextView petname;
    private TextView petyear;
    private   SharedPreferences petpref;
    private   SharedPreferences.Editor petprefedit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.petinfo, container, false);
        cardpetname=(CardView)view.findViewById(R.id.cardpetname);
        cardpetyear=(CardView)view.findViewById(R.id.cardpetyear);
        petname=(TextView)view.findViewById(R.id.petname);
        petyear=(TextView)view.findViewById(R.id.petyear);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        petpref = getActivity().getSharedPreferences("petpref",getActivity().MODE_PRIVATE);
        petprefedit=petpref.edit();
        Log.i("petname",petpref.getString("petname","petname"));
        petname.setText(petpref.getString("petname","petname"));
        petyear.setText(petpref.getString("petyear","petyear"));

        cardpetname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert;

                alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("输入pet名称");

                final EditText input = new EditText(getActivity());

                input.setText(petname.getText());

                alert.setView(input);
                alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        petprefedit.putString("petname",input.getText().toString());
                        petprefedit.commit();
                        Log.i("pref",petpref.getString("petname"," "));
                        petname.setText(input.getText());
                    }
                });
                alert.show();

            }
        });


        cardpetyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert;

                alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("输入pet年龄");

                final EditText input = new EditText(getActivity());


                input.setText(petyear.getText());

                alert.setView(input);
                alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        petprefedit.putString("petyear",input.getText().toString());
                        petprefedit.commit();
                        petyear.setText(input.getText());
                    }
                });
                alert.show();

            }
        });
    }
}
