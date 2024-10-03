package com.klqmz.test;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import rikka.shizuku.Shizuku;

public class ShizukuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shizuku);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 添加权限申请监听
        Shizuku.addRequestPermissionResultListener(onRequestPermissionResultListener);
    }

    @Override
    protected void onDestroy() {
        //Activity销毁时移除Shizuku监听权限
        Shizuku.removeRequestPermissionResultListener(onRequestPermissionResultListener);
        super.onDestroy();
    }
    //Shizuku授权监听
    private final Shizuku.OnRequestPermissionResultListener onRequestPermissionResultListener = (requestCode, grantResult) -> {
        boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
        if (granted) {
            Toast.makeText(ShizukuActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ShizukuActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }
    };
    //Shizuku是否启动
    private boolean checkRunning() {
        try {
            Shizuku.checkSelfPermission();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
    //Shizuku是否授权
    private boolean checkPermission() {
        if(checkRunning()) {
            return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
        }else{
            return false;
        }
    }
    //申请Shizuku权限
    private void requestShizukuPermission() {
        if (checkPermission()) {
            Toast.makeText(this, "已获得Shizuku权限", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Shizuku.isPreV11()) {
            Toast.makeText(this, "当前Shizuku版本过低", Toast.LENGTH_SHORT).show();
            return;
        }

        // 动态申请权限
        Shizuku.requestPermission(1);
    }


    //按钮包装
    public void checkShizuku(View view){
        if(checkPermission()){
            Toast.makeText(this,"当前已获得Shizuku权限",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"当前未获得Shizuku权限",Toast.LENGTH_SHORT).show();
        }
    }
    public void requestShizuku(View view){
        if(checkRunning()){
            requestShizukuPermission();
        }else{
            Toast.makeText(this, "Shizuku未运行", Toast.LENGTH_SHORT).show();
        }
    }
}