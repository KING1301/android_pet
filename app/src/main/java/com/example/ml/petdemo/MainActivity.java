package com.example.ml.petdemo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.ActionBarDrawerToggle;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{

    private Toolbar mToolbar;
    private NavigationView mNavigationView;//左侧拖出菜单控件
    private DrawerLayout mDrawerLayout;    //DrawerLayout控件
    private TabLayout mTabLayout;    //Tab选项卡
    private ViewPager mViewPager;    //ViewPager控件实现滑动切换标签页
    private AlarmListAdapter alarmListAdapter;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
        Log.i("update","更新activity");
        startService(new Intent(this,petservice.class));
        Log.i("petservice","service悬浮窗启动");
    }

    private void initView() {
        //加载布局和初始化空间
        mToolbar = (Toolbar) this.findViewById(R.id.tool_bar);
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        mTabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) this.findViewById(R.id.view_pager);

        //初始化ToolBar
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.drawer_open,R.string.drawer_open);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);//设置NavigationView添加item的监听事件




        List<String> Tabtitles = new ArrayList<>(); //初始化TabLayout的title数据集
        Tabtitles.add("宠物信息");//显示宠物信息页面
        Tabtitles.add("收集箱");//定时代办事件提醒
        Tabtitles.add("待定");
        mTabLayout.addTab(mTabLayout.newTab().setText(Tabtitles.get(0)));//设置TabLayout标题
       mTabLayout.addTab(mTabLayout.newTab().setText(Tabtitles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(Tabtitles.get(2)));

       fragments = new ArrayList<>();//初始化ViewPager的数据集
        fragments.add(new petFragment());
        fragments.add(new timeFragment());
        fragments.add(new BlankFragment());



        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments,Tabtitles); //创建ViewPager的adapter适配器
        mViewPager.setAdapter(adapter);   //给mViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager); //将mTabLayout和mViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(adapter);//给TabLayout设置适配器
    }

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) { //点击NavigationView中定义的menu item时触发反应

            switch (menuItem.getItemId()) {
                case R.id.menu_info_details:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.menu_share:
                    break;
            }
            //关闭DrawerLayout回到主界面选中的tab的fragment页
            mDrawerLayout.closeDrawer(mNavigationView);
            return false;
        };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//创建标题栏左上角nenu菜单

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info_details:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.menu_share:
                mViewPager.setCurrentItem(1);
                break;
           // case R.id.menu_agenda:
             //   mViewPager.setCurrentItem(2);
            //    break;
            case android.R.id.home:
                //主界面左上角的icon点击反应
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }






    @Override
    public void onClick(View v) {
        timeFragment tm=(timeFragment)fragments.get(1);
        alarmListAdapter=tm.getAlarmListAdapter();
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
