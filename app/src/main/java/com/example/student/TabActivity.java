package com.example.student;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.student.Fragment.MainPageFragment;
import com.example.student.Fragment.ScanFragment;
import com.example.student.Fragment.SettingFragment;

import java.util.Set;


public class TabActivity extends AppCompatActivity implements View.OnClickListener {
    private String StuNum;
    private LinearLayout MainPage;
    private LinearLayout Scan;
    private LinearLayout Setting;
    private MainPageFragment mainPageFragment;
    private ScanFragment scanFragment;
    private SettingFragment settingFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        MainPage = findViewById(R.id.mainpage);
        Scan = findViewById(R.id.scan);
        Setting = findViewById(R.id.setting);
        tv = findViewById(R.id.title);
        MainPage.setOnClickListener(this);
        Scan.setOnClickListener(this);
        Setting.setOnClickListener(this);
        StuNum = getIntent().getStringExtra("学号");


        //页面继承类Fragment
        mainPageFragment = new MainPageFragment();
        scanFragment = new ScanFragment();
        settingFragment = new SettingFragment();


        //把学号传给每一个Fragment
        Bundle bundle = new Bundle();
        bundle.putString("学号", StuNum);
        mainPageFragment.setArguments(bundle);
        scanFragment.setArguments(bundle);
        settingFragment.setArguments(bundle);

        //默认首页
        replaceFragment(mainPageFragment);
        tv.setText("首页");
    }


    public void onClick(View v) {
        if (v.getId() == R.id.mainpage) {
            tv.setText("首页");
            replaceFragment(mainPageFragment);

        } else if (v.getId() == R.id.scan) {
            tv.setText("扫描");
            replaceFragment(scanFragment);
        } else if (v.getId() == R.id.setting) {
            tv.setText("我的");
            replaceFragment(settingFragment);
        }

    }

    //向Fragment中填充布局文件
    private void replaceFragment(Fragment fragment) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragShow, fragment);
        ft.commit();
    }

}

