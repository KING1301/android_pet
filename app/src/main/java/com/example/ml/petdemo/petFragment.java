package com.example.ml.petdemo;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class petFragment extends Fragment {
    private View view;
    private CardView cardpetname;
    private CardView cardpetyear;
    private CardView cardpetview;
    private TextView petname;
    private TextView petyear;
    private ImageView petview;
    private config petconfig;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.petinfo, container, false);
        cardpetname = (CardView) view.findViewById(R.id.cardpetname);
        cardpetyear = (CardView) view.findViewById(R.id.cardpetyear);
        petname = (TextView) view.findViewById(R.id.petname);
        petyear = (TextView) view.findViewById(R.id.petyear);
        petview = (ImageView) view.findViewById(R.id.petview);
        cardpetview = (CardView) view.findViewById(R.id.cardpetview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        petconfig = config.getpetconfig(getActivity());
        petname.setText(petconfig.getpetname());
        petyear.setText(petconfig.getpetyear());
        if (petconfig.gettype() == 0)
            petview.setImageResource(R.drawable.petanzai);
        else
            petview.setImageResource(R.drawable.petbear);

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
                        petconfig.setpetname(input.getText().toString());

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
                        petconfig.setpetyear(input.getText().toString());
                        petyear.setText(input.getText());
                    }
                });
                alert.show();

            }
        });


        cardpetview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert;

                alert = new AlertDialog.Builder(getActivity());
                View alartview = LayoutInflater.from(getActivity()).inflate(R.layout.setpettypeview, null);
                alert.setTitle("选择宠物模型");
                RadioGroup group = (RadioGroup) alartview.findViewById(R.id.radiogroup);
                Log.d("type", petconfig.gettype() + "");
                if (petconfig.gettype() == 0) {

                    group.check(R.id.pettype1);
                } else {
                    group.check(R.id.pettype2);

                    Log.d("checktype", 1 + "");

                }

                alert.setView(alartview);

                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup arg0, int arg1) {
                        int radioButtonId = arg0.getCheckedRadioButtonId();
                        //根据ID获取RadioButton的实例
                        if (R.id.pettype1 == radioButtonId)
                            petconfig.setpettype(0);
                        else
                            petconfig.setpettype(1);

                        if (petconfig.gettype() == 0)
                            petview.setImageResource(R.drawable.petanzai);
                        else
                            petview.setImageResource(R.drawable.petbear);

                        //启动petservice
                        Intent petintent = new Intent(getActivity(), petservice.class);
                        petintent.putExtra("pettype", petconfig.gettype());
                        getActivity().startService(petintent);
                    }
                });
                alert.show();

            }
        });
    }
}
