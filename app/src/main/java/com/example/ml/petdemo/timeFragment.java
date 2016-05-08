package com.example.ml.petdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;




public class timeFragment extends Fragment  {

    private  View view;
    private ListView mathAlarmListView;
    private AlarmListAdapter alarmListAdapter;
    private android.support.design.widget.FloatingActionButton fab;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //反射加载事件提醒布局
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.alarm_list,container,false);
        mathAlarmListView = (ListView) view.findViewById(R.id.list);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        alarmListAdapter = new AlarmListAdapter((MainActivity) getActivity());
        mathAlarmListView.setAdapter(alarmListAdapter);


        mathAlarmListView.setLongClickable(true);
        mathAlarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                final Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("删除闹钟");
                dialog.setMessage("删除闹钟项?");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Database.init(getActivity());
                        Database.deleteEntry(alarm);
                        ((MainActivity)getActivity()).callMathAlarmScheduleService();

                        updateAlarmList();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;
            }
        });


        ((MainActivity)getActivity()).callMathAlarmScheduleService();


        mathAlarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), AlarmPreferencesActivity.class);
                intent.putExtra("alarm", alarm);
                startActivity(intent);
            }

        });




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AlarmPreferencesActivity.class));
            }
        });
    }



    @Override
    public void onPause() {
        Database.deactivate();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("update list","update list");
        updateAlarmList();
    }

    public void updateAlarmList(){
        Database.init(getActivity());
        final List<Alarm> alarms = Database.getAll();
        alarmListAdapter.setMathAlarms(alarms);

                alarmListAdapter.notifyDataSetChanged();
    }

    public AlarmListAdapter getAlarmListAdapter()
    {
        return alarmListAdapter;
    }


}
