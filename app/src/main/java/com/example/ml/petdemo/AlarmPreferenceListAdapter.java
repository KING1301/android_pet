package com.example.ml.petdemo;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.example.ml.petdemo.AlarmPreference.Type;

public class AlarmPreferenceListAdapter extends BaseAdapter {


    private Context context;
    private Alarm alarm;
    private List<AlarmPreference> preferences = new ArrayList<AlarmPreference>();
    private final String[] repeatDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


    public AlarmPreferenceListAdapter(Context context, Alarm alarm) {
        setContext(context);
        /**构建prefer*/
        setMathAlarm(alarm);
    }

    @Override
    public int getCount() {
        return preferences.size();
    }

    @Override
    public Object getItem(int position) {
        return preferences.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlarmPreference alarmPreference = (AlarmPreference) getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        switch (alarmPreference.getType()) {
            case BOOLEAN:
                //13.04 if(null == convertView || convertView.getId() != android.R.layout.simple_list_item_checked)
                convertView = layoutInflater.inflate(android.R.layout.simple_list_item_checked, null);

                CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);
                checkedTextView.setText(alarmPreference.getTitle());
                checkedTextView.setChecked((Boolean) alarmPreference.getValue());
                break;
            case STRING:
            case LIST:
            case MULTIPLE_LIST:
            case TIME:
            default:
                convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);
                TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
                text1.setTextSize(18);
                text1.setText(alarmPreference.getTitle());
                TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
                text2.setText(alarmPreference.getSummary());
                break;
        }

        return convertView;
    }

    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
        preferences.clear();
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_ACTIVE, "Active", null, null, alarm.getAlarmActive(), Type.BOOLEAN));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_NAME, "Label", alarm.getAlarmName(), null, alarm.getAlarmName(), Type.STRING));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TIME, "Set time", alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), Type.TIME));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_REPEAT, "Repeat", alarm.getRepeatDaysString(), repeatDays, alarm.getDays(), Type.MULTIPLE_LIST));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_VIBRATE, "Vibrate", null, null, alarm.getVibrate(), Type.BOOLEAN));
        Log.w("ring",alarm.getAlarmRing());
        Uri alarmToneUri = Uri.parse(alarm.getAlarmRing());
        Ringtone alarmRing = RingtoneManager.getRingtone(getContext(), alarmToneUri);

            preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_RING, "Ringtone", alarmRing.getTitle(getContext()),null, alarm.getAlarmRing(), Type.LIST));




    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getRepeatDays() {
        return repeatDays;
    }

}


