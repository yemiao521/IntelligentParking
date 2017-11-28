package com.example.foolishfan.IntelligentParking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foolishfan.IntelligentParking.Util.HttpJson;
import com.example.foolishfan.IntelligentParking.Util.QRcode;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static public boolean isLogin;//全局获取当前软件的登录状态
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private QRcode qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //顶部toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //获取登录状态
        SharedPreferences statusPreferences=getSharedPreferences("status",Context.MODE_PRIVATE);
        isLogin=statusPreferences.getBoolean("isLogin",false);

        //二维码初始化
        qr=new QRcode();

        //获取相关按钮
        Button addCar=(Button)findViewById(R.id.addCar);
        Button parkNearby=(Button)findViewById(R.id.parkNearby);
        Button wallet=(Button)findViewById(R.id.wallet);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);   //创建导航试图对象
        ImageView mImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.ivAvatar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);  //实现抽屉效果左滑拉出菜单栏


        //注册监听事件
        addCar.setOnClickListener(mainOnClick);
        parkNearby.setOnClickListener(mainOnClick);
        wallet.setOnClickListener(mainOnClick);
        mImageView.setOnClickListener(mainOnClick);
        navigationView.setNavigationItemSelectedListener(this);

        //实现侧边栏滑入滑出
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // 动态加载右键菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //实现切换网络连接
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.switchLocalhost:
                HttpJson.setWebsite("http://10.0.2.2/ParkingWeb/");
                Toast.makeText(MainActivity.this, "已设置为10.0.2.2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.switchServer:
                HttpJson.setWebsite("http://120.78.173.73/ParkingWeb/");
                Toast.makeText(MainActivity.this, "已设置为120.78.173.73", Toast.LENGTH_SHORT).show();
                break;
            case R.id.switchLAN:
                HttpJson.setWebsite("http://192.168.155.1/ParkingWeb/");
                Toast.makeText(MainActivity.this, "已设置为192.168.155.1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.switchScan:
                qr.scanQRcode(MainActivity.this,ScanActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        qr.setResult(requestCode, resultCode, data);
        if(qr.getResult()!= null) {
            if(qr.getResult().getContents() == null) {
                Toast.makeText(this, "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功，条码值: " + qr.getResult().getContents(), Toast.LENGTH_LONG).show();
                //rsView.setText(qr.getResult().getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //设置点击左边菜单栏每一个选项的回应方式
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_me://停车历史
                if(isLogin){
                    Intent intent=new Intent(MainActivity.this,ParkingHistory.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_message://我的车辆
                if(isLogin){
                    Intent intent=new Intent(MainActivity.this,UserCar.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_friend://消息中心
                if(isLogin){
                    Intent intent=new Intent(MainActivity.this,MessageCenter.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_notification://我的收藏
                if(isLogin){
                    Intent intent=new Intent(MainActivity.this,Collecting.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_manage://应用管理
                if(isLogin){
                    Intent intent=new Intent(MainActivity.this,ApplicationManagement.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_night://夜间模式
                break;
            case R.id.nav_suggestion://意见反馈
                if(isLogin){
                    Intent intent=new Intent(MainActivity.this,Suggestion.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_setting://设置
                Intent intent=new Intent(MainActivity.this,SoftwareSet.class);
                startActivity(intent);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    //页面点击事件
    View.OnClickListener mainOnClick= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainOnClick(v);
        }
    };

    //页面按钮点击事件的方法
    private void mainOnClick(View v){
        int id=v.getId();
        switch (id){
            case R.id.ivAvatar://点击头像的跳转事件
                if(isLogin){
                    Intent intent=new Intent(MainActivity.this,User.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                }
                break;
            case R.id.addCar://点击添加车辆的监听事件
                if(isLogin){
                    Intent intent=new Intent(MainActivity.this,AddUserCar.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.parkNearby://点击附近停车场的监听事件
                break;
            case R.id.wallet://点击我的钱包的监听事件
                if(isLogin) {
                    Intent intent = new Intent(MainActivity.this,Finance.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

