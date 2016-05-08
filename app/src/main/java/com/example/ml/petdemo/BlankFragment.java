package com.example.ml.petdemo;

import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ml.petdemo.Alarm;

import java.util.ArrayList;
import java.util.List;


public class BlankFragment extends Fragment {
    private View mParentView;
    private FloatingActionButton Fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_blank, container, false);
        Fab=(FloatingActionButton)mParentView.findViewById(R.id.fab1);
        return mParentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Ring","setRing");
                //打开系统铃声设置
                new Alarm();
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                //设置铃声类型和title
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹铃铃声");
                //当设置完成之后返回到当前的Activity
                startActivity(intent);
            }
        });

    }
}
