package com.example.ml.petdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class AlarmAlertActivity extends Activity implements View.OnClickListener {

    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private StringBuilder answerBuilder = new StringBuilder();

    private MathProblem mathProblem;
    private Vibrator vibrator;

    private boolean alarmActive;

    private TextView problemView;//问题文本
    private TextView answerView;//回答文本
    private String answerString;
    /** 生成问题并相应的初始化界面*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.mathalart_activity);

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");

        this.setTitle(alarm.getAlarmName());


        mathProblem = new MathProblem();


        answerString = String.valueOf(mathProblem.getAnswer());
        if (answerString.endsWith(".0")) {
            answerString = answerString.substring(0, answerString.length() - 2);
        }

        problemView = (TextView) findViewById(R.id.textView1);
        problemView.setText(mathProblem.toString());

        answerView = (TextView) findViewById(R.id.textView2);
        answerView.setText("= ?");

        ((Button) findViewById(R.id.Button0)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button1)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button2)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button3)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button4)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button5)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button6)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button7)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button8)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button9)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button_clear)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button_decimal)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button_minus)).setOnClickListener(this);

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(getClass().getSimpleName(), "Incoming call: "
                                + incomingNumber);
                        try {
                            mediaPlayer.pause();
                        } catch (IllegalStateException e) {

                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(getClass().getSimpleName(), "Call State Idle");
                        try {
                            mediaPlayer.start();
                        } catch (IllegalStateException e) {

                        }
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);

        startAlarm();

    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }
    /**设置闹铃提醒*/
    private void startAlarm() {

       if (alarm.getAlarmRing() != "") {
            mediaPlayer = new MediaPlayer();
            if (alarm.getVibrate()) {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = { 1000, 200, 200, 200 };
                vibrator.vibrate(pattern, 0);
            }
            try {
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(this,
                        Uri.parse(alarm.getAlarmRing()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                mediaPlayer.release();
                alarmActive = false;
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        StaticWakeLock.lockOff(this);
    }

    @Override
    protected void onDestroy() {
        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.stop();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {

        }
        super.onDestroy();
    }
    /**点击事件监听 获取输入的计算结果并与正确的计算结果比较*/
    @Override
    public void onClick(View v) {
        if (!alarmActive)
            return;
        String button = (String) v.getTag();
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);//设置触屏震动强度
        if (button.equalsIgnoreCase("clear")) {
            if (answerBuilder.length() > 0) {
                answerBuilder.setLength(answerBuilder.length() - 1);
                answerView.setText(answerBuilder.toString());
            }
        } else if (button.equalsIgnoreCase(".")) {
            if (!answerBuilder.toString().contains(button)) {
                if (answerBuilder.length() == 0)
                    answerBuilder.append(0);
                answerBuilder.append(button);
                answerView.setText(answerBuilder.toString());
            }
        } else if (button.equalsIgnoreCase("-")) {
            if (answerBuilder.length() == 0) {
                answerBuilder.append(button);
                answerView.setText(answerBuilder.toString());
            }
        } else {
            answerBuilder.append(button);
            answerView.setText(answerBuilder.toString());
            if (isAnswerCorrect()) {
                alarmActive = false;
                if (vibrator != null)
                    vibrator.cancel();
                try {
                    mediaPlayer.stop();
                } catch (IllegalStateException ise) {

                }
                try {
                    mediaPlayer.release();
                } catch (Exception e) {

                }
                this.finish();
            }
        }
        if (answerView.getText().length() >= answerString.length()
                && !isAnswerCorrect()) {
            answerView.setTextColor(Color.RED);
        } else {
            answerView.setTextColor(Color.BLACK);
        }
    }
    /**校验计算结果是否正确*/
    public boolean isAnswerCorrect() {
        boolean correct = false;
        try {
            correct = mathProblem.getAnswer() == Float.parseFloat(answerBuilder
                    .toString());
        } catch (NumberFormatException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return correct;
    }

}

