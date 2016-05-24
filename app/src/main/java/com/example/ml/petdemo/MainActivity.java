package com.example.ml.petdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar mToolbar;
    private NavigationView mNavigationView;//左侧拖出菜单控件
    private DrawerLayout mDrawerLayout;    //DrawerLayout控件
    private TabLayout mTabLayout;    //Tab选项卡
    private ViewPager mViewPager;    //ViewPager控件实现滑动切换标签页
    private ImageView drawerimage;
    private AlarmListAdapter alarmListAdapter;
    private List<Fragment> fragments;
    private config petconfig;
    /* 上一次按返回按键的时间 */
    private long preBackPressTime;
    /* 按返回按键的次数 */
    private long pressTimes;


    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        return isRunning;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        petconfig = config.getpetconfig(this);
        initView();

        Log.i("update", "更新activity");


        //启动petservice
        Intent petintent = new Intent(this, petservice.class);
        petintent.putExtra("pettype", petconfig.gettype());
        startService(petintent);


        Log.i("petservice", "service悬浮窗启动");
        //检测通知栏监控服务是否启动，没有提示用户设置
        if (!isServiceRunning(this, "com.example.ml.petdemo.NotificationListener")) {
            Snackbar.make(mViewPager, "服务无法启动", Snackbar.LENGTH_LONG)
                    .setAction("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                            startActivity(intent);
                        }
                    })
                    .show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        //加载布局和初始化空间
        mToolbar = (Toolbar) this.findViewById(R.id.tool_bar);
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        mTabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) this.findViewById(R.id.view_pager);
        //设置NavigationView抽屉图片
        View headerView = mNavigationView.getHeaderView(0);
        drawerimage = (ImageView) headerView.findViewById(R.id.drawer_image);
        if (petconfig.gettype() == 0)
            drawerimage.setImageResource(R.drawable.petanzai);
        else
            drawerimage.setImageResource(R.drawable.petbear);

        //初始化ToolBar
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_open);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);//设置NavigationView添加item的监听事件


        List<String> Tabtitles = new ArrayList<>(); //初始化TabLayout的title数据集
        Tabtitles.add("宠物信息");//显示宠物信息页面
        Tabtitles.add("收集箱");//定时代办事件提醒

        mTabLayout.addTab(mTabLayout.newTab().setText(Tabtitles.get(0)));//设置TabLayout标题
        mTabLayout.addTab(mTabLayout.newTab().setText(Tabtitles.get(1)));


        fragments = new ArrayList<>();//初始化ViewPager的数据集
        fragments.add(new petFragment());
        fragments.add(new timeFragment());


        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, Tabtitles); //创建ViewPager的adapter适配器
        mViewPager.setAdapter(adapter);   //给mViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager); //将mTabLayout和mViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(adapter);//给TabLayout设置适配器
    }

    //点击NavigationView中菜单项处理
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.newalart:
                startActivity(new Intent(this, AlarmPreferencesActivity.class));
                break;
            case R.id.settingfloat:
                //  Intent intentfloat = new Intent("android.settings.ACTION_SYSTEM_ALERT_WINDOW_SETTINGS");
                //  startActivity(intentfloat);
                break;
            case R.id.settingnotice:
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
        }
        //关闭DrawerLayout回到主界面选中的tab的fragment页
        mDrawerLayout.closeDrawer(mNavigationView);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//创建菜单

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //主界面左上角的icon点击打开侧边栏
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.newalart:
                startActivity(new Intent(this, AlarmPreferencesActivity.class));
                break;
            case R.id.settingfloat:
                //Intent intentfloat = new Intent("android.settings.ACTION_SYSTEM_ALERT_WINDOW_SETTINGS");
                // startActivity(intentfloat);
                break;
            case R.id.settingnotice:
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("test0", "onbackpressed");

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Log.d("test1", "onbackpressed");
            if (onBackPressed_is_true()) {
                Log.d("test", "onbackpressed");
                super.onBackPressed();

            }

        }
    }

    public boolean onBackPressed_is_true() {
        long cBackPressTime = SystemClock.uptimeMillis();
        if (cBackPressTime - preBackPressTime < 2000) {
            pressTimes++;
            if (pressTimes >= 2) {
                return true;
            }
        } else {
            pressTimes = 1;
            Snackbar.make(mTabLayout, "再次点击退出", Snackbar.LENGTH_SHORT)
                    .show();
        }
        preBackPressTime = cBackPressTime;
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ondestory", "activitydestory");
    }

    @Override
    public void onClick(View v) {
        timeFragment tm = (timeFragment) fragments.get(1);
        alarmListAdapter = tm.getAlarmListAdapter();
        if (v.getId() == R.id.checkBox_alarm_active) {
            CheckBox checkBox = (CheckBox) v;
            Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox.getTag());
            alarm.setAlarmActive(checkBox.isChecked());
            Database.update(alarm);
            callMathAlarmScheduleService();
            if (checkBox.isChecked()) {
                Toast.makeText(this, alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

}
