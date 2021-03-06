package com.example.ml.petdemo;


import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class petservice extends Service {

    private static final String TAG = "petfloatservice";
    protected MyReceiver mReceiver = new MyReceiver();
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private RelativeLayout mRelativeLayout;
    private ImageView mFloatView;
    private TextView mTextview;
    private AnimationDrawable animationDrawable;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private int startx = 0;
    private int starty = 0;
    private int type;
    /*
    *用于定时刷新界面
     */
    private STATE state = STATE.STOP;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 1000 * 5);// 间隔120秒
        }

        int update() {

            if (mRelativeLayout == null)
                return 0;
            Log.i("time", "update");
            mTextview.setTextColor(getResources().getColor(R.color.color_0));
            if (wmParams.x == 0 || wmParams.x == screenWidth) {
                return 0;
            }

            switch (state) {
                case STOP:
                    state = STATE.values()[1];
                    break;
                case NORMAL0:
                    mTextview.setText(state.toString());
                    mTextview.setVisibility(View.VISIBLE);
                    state = STATE.values()[2];
                    if (starty <= screenHeight / 3) {
                        if (type == 0)
                            mFloatView.setImageResource(R.drawable.o);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_m);
                    } else if (starty <= 2 * screenHeight / 3) {
                        //mFloatView.setImageResource(R.drawable.top_normal0);
                        //animationDrawable = (AnimationDrawable) mFloatView.getDrawable();
                        //animationDrawable.stop();
                        //animationDrawable.start();
                        if (type == 0)
                            mFloatView.setImageResource(R.drawable.tk);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_sk);
                    } else {
                        if (type == 0)
                            mFloatView.setImageResource(R.drawable.tb);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_tb);

                    }


                    break;
                case NORMAL1:
                    mTextview.setText(state.toString());
                    mTextview.setVisibility(View.VISIBLE);
                    state = STATE.values()[3];
                    //mFloatView.setImageResource(R.drawable.o);
                    if (starty <= screenHeight / 3) {
                        if ((type == 0))
                            mFloatView.setImageResource(R.drawable.w);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_w);
                    } else if (starty <= 2 * screenHeight / 3) {
                        if (type == 0)
                            mFloatView.setImageResource(R.drawable.uk);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_sk1);

                    } else {
                        if (type == 0)
                            mFloatView.setImageResource(R.drawable.tb);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_tb1);
                    }
                    break;
                case NORMAL2:
                    mTextview.setText(state.toString());
                    mTextview.setVisibility(View.VISIBLE);
                    state = STATE.values()[3];
                    if (starty <= screenHeight / 3) {
                        if (type == 0)
                            mFloatView.setImageResource(R.drawable.n);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_sk);
                    } else if (starty <= 2 * screenHeight / 3) {
                        if (type == 0)
                            mFloatView.setImageResource(R.drawable.wk);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_sk2);
                    } else {
                        if (type == 0)
                            mFloatView.setImageResource(R.drawable.tb);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_tb2);
                    }
                    break;

            }
            return 0;


        }
    };

    @Override
    public void onCreate() {

        super.onCreate();
        //createFloatView();
        handler.postDelayed(runnable, 1000 * 6);
        if (mReceiver == null) mReceiver = new MyReceiver();
        registerReceiver(mReceiver, new IntentFilter("WECHAT_NOTICE"));
        Toast toast = Toast.makeText(getApplicationContext(), "悬浮窗不显示请打开应用的悬浮窗权限", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("onstart", "onstart");
        int newtype = intent.getIntExtra("pettype", 0);
        if (mRelativeLayout == null) {
            type = newtype;
            createFloatView(type);
            Log.i("onstart", "初始化floatview");
        } else {
            if (type != newtype) {
                Log.i("onstart", "刷新floatview");
                type = newtype;
                resetpet(type);
            }


        }
        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createFloatView(final int pettype) {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        wmParams.windowAnimations = android.R.style.Animation_Translucent;

        mRelativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.petview, null);
        mFloatView = (ImageView) mRelativeLayout.findViewById(R.id.floatview);
        mTextview = (TextView) mRelativeLayout.findViewById(R.id.pettitle);
        if (pettype == 0)
            mFloatView.setImageResource(R.drawable.assist_anzai_left_green);//安仔贴边图片
        else
            mFloatView.setImageResource(R.drawable.petbear_left);//模型2贴边图片
        mFloatView.setBackgroundColor(Color.TRANSPARENT);


        //添加mFloatLayout
        mWindowManager.addView(mRelativeLayout, wmParams);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;


        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                state = STATE.STOP;
                mTextview.setVisibility(View.GONE);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        startx = (int) event.getRawX();
                        starty = (int) event.getRawY();
                        Log.w("startx", startx + "");
                        if (startx < screenWidth / 6) {
                            if (pettype == 0)
                                mFloatView.setImageResource(R.drawable.assist_anzai_pressed_left_green);
                            else
                                mFloatView.setImageResource(R.drawable.petbear_left);
                        } else if (screenWidth - startx < screenWidth / 6) {
                            if (pettype == 0)
                                mFloatView.setImageResource(R.drawable.assist_anzai_pressed_right_green);
                            else
                                mFloatView.setImageResource(R.drawable.petbear_right);

                        }

                        Log.i("event", "down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int newx = (int) event.getRawX();
                        int newy = (int) event.getRawY();
                        if (pettype == 0)
                            mFloatView.setImageResource(R.drawable.l);
                        else
                            mFloatView.setImageResource(R.drawable.petbear_wk);
                        int dx = newx - startx;
                        int dy = newy - starty;
                        wmParams.x += dx;
                        wmParams.y += dy;
                        // 更新
                        mWindowManager.updateViewLayout(mRelativeLayout, wmParams);
                        // 对初始坐标重新赋值
                        startx = (int) event.getRawX();
                        starty = (int) event.getRawY();

                        Log.i("event", "move");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("event", "up");
                        /*
                        *横坐标距离屏幕6/1时贴边
                         */
                        if (startx <= screenWidth / 6) {
                            wmParams.x = 0;
                            mWindowManager.updateViewLayout(mRelativeLayout, wmParams);
                            if (pettype == 0)
                                mFloatView.setImageResource(R.drawable.assist_anzai_left_green);
                            else
                                mFloatView.setImageResource(R.drawable.petbear_left);


                        } else if (startx >= 5 * screenWidth / 6) {
                            wmParams.x = screenWidth;
                            mWindowManager.updateViewLayout(mRelativeLayout, wmParams);
                            if (pettype == 0)
                                mFloatView.setImageResource(R.drawable.assist_anzai_right_green);
                            else
                                mFloatView.setImageResource(R.drawable.petbear_right);

                        } else if (starty <= screenHeight / 3) {
                            if (pettype == 0)
                                mFloatView.setImageResource(R.drawable.m);
                            else
                                mFloatView.setImageResource(R.drawable.petbear_m);


                        } else if (starty <= 2 * screenHeight / 3) {
                            if (pettype == 0)
                                mFloatView.setImageResource(R.drawable.sk);
                            else
                                mFloatView.setImageResource(R.drawable.petbear_sk);


                        } else {
                            if (pettype == 0)
                                mFloatView.setImageResource(R.drawable.tb);
                            else
                                mFloatView.setImageResource(R.drawable.petbear_tb);
                        }


                        break;


                }


                //return false;
                return true;

            }
        });

        /*监听信息点击*/
        mTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == STATE.WECHAT_NOTICE) {

                    Intent intent = new Intent("WECHAT_CLICK");
                    // intent.putExtras(mNotification.extras);
                    sendBroadcast(intent);
                    Log.d("点击消息", "点击消息广播");
                    //点击后取消显示
                    state = STATE.STOP;
                    mTextview.setVisibility(View.GONE);


                } else {
                    Log.d("无通知", "未显示微信通知");
                }

            }
        });
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.i("destory", "petserver");
        handler.removeCallbacks(runnable); //停止刷新
        unregisterReceiver(mReceiver);
        if (mRelativeLayout != null)
            mWindowManager.removeView(mRelativeLayout);
        mWindowManager = null;
        wmParams = null;

    }

    public void resetpet(int type) {
        if (mRelativeLayout != null)
            mWindowManager.removeView(mRelativeLayout);
        mWindowManager = null;
        wmParams = null;
        createFloatView(type);
    }

    public enum STATE {
        STOP,
        NORMAL0,
        NORMAL1,
        NORMAL2,
        WECHAT_NOTICE;

        @Override
        public String toString() {
            switch (this.ordinal()) {
                case 0:
                    return "";
                case 1:
                    return "抱抱我";
                case 2:
                    return "摸摸我";
                case 3:
                    return "睡睡我";
            }
            return super.toString();
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                Bundle extras = intent.getExtras();
                String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
                // int notificationIcon = extras.getInt(Notification.EXTRA_SMALL_ICON);
                //  Bitmap notificationLargeIcon = ((Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON));
                CharSequence notificationText = extras.getCharSequence(Notification.EXTRA_TEXT);
                //CharSequence notificationSubText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
                state = STATE.WECHAT_NOTICE;
                mTextview.setVisibility(View.VISIBLE);
                if (type == 0)
                    mFloatView.setImageResource(R.drawable.wk);
                else
                    mFloatView.setImageResource(R.drawable.petbear_wk);
                mTextview.setText(notificationTitle + "\n" + notificationText);

                // if (notificationLargeIcon != null) {
                //     largeIcon.setImageBitmap(notificationLargeIcon);
                // }
            }

        }
    }


}
