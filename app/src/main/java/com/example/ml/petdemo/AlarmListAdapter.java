package com.example.ml.petdemo;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AlarmListAdapter extends BaseAdapter {

    public static final String ALARM_FIELDS[] = {Database.COLUMN_ALARM_ACTIVE,
            Database.COLUMN_ALARM_TIME, Database.COLUMN_ALARM_DAYS};
    private MainActivity alarmActivity;
    private List<Alarm> alarms = new ArrayList<Alarm>();

    public AlarmListAdapter(MainActivity alarmActivity) {
        this.alarmActivity = alarmActivity;
        Database.init(alarmActivity);
        alarms = Database.getAll();
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.e("getView", "getview");
        if (null == view)
            view = LayoutInflater.from(alarmActivity).inflate(
                    R.layout.alarm_list_element, null);

        Alarm alarm = (Alarm) getItem(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox_alarm_active);
        checkBox.setChecked(alarm.getAlarmActive());
        checkBox.setTag(position);
        checkBox.setOnClickListener(alarmActivity);

        TextView alarmTimeView = (TextView) view
                .findViewById(R.id.textView_alarm_time);
        alarmTimeView.setText(alarm.getAlarmTimeString());


        TextView alarmDaysView = (TextView) view
                .findViewById(R.id.textView_alarm_name);
        alarmDaysView.setText(alarm.getAlarmName());


        return view;
    }

    public List<Alarm> getMathAlarms() {
        return alarms;
    }

    public void setMathAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

}

