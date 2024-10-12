package com.klqmz.test;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.klqmz.test.Shizuku.UserService;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rikka.shizuku.Shizuku;

public class ShizukuActivity extends AppCompatActivity {
    private boolean shizukuServiceState = false;
    private Button apply_shizuku;
    private Button check_shizuku;
    private Button connect_shizuku;
    private Button checkconnect_shizuku;
    private Button execute_shizuku;
    private IUserService iUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shizuku);

        // 初始化按钮
        apply_shizuku = findViewById(R.id.shizuku_button_apply);
        check_shizuku = findViewById(R.id.shizuku_button_check);
        connect_shizuku = findViewById(R.id.shizuku_button_connect);
        checkconnect_shizuku = findViewById(R.id.shizuku_button_checkconnect);
        execute_shizuku = findViewById(R.id.shizuku_button_execute);

        // 设置 WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化 Shizuku
        initShizuku();

        // 添加事件
        addEvent();
    }

    // Shizuku 权限监听
    private void initShizuku() {
        // 添加权限申请监听
        Shizuku.addRequestPermissionResultListener(onRequestPermissionResultListener);

        // Shizuku 服务启动时调用该监听
        Shizuku.addBinderReceivedListenerSticky(onBinderReceivedListener);

        // Shizuku 服务终止时调用该监听
        Shizuku.addBinderDeadListener(onBinderDeadListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 移除权限申请监听
        Shizuku.removeRequestPermissionResultListener(onRequestPermissionResultListener);
        Shizuku.removeBinderReceivedListener(onBinderReceivedListener);
        Shizuku.removeBinderDeadListener(onBinderDeadListener);

        // 解绑 Shizuku 服务，防止崩溃，添加异常捕获
        try {
            Shizuku.unbindUserService(userServiceArgs, serviceConnection, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Shizuku.OnBinderReceivedListener onBinderReceivedListener = () -> {
        shizukuServiceState = true;
        //每次进入此Activity会提醒
        //Toast.makeText(this, "Shizuku服务已启动", Toast.LENGTH_SHORT).show();
    };

    private final Shizuku.OnBinderDeadListener onBinderDeadListener = () -> {
        shizukuServiceState = false;
        iUserService = null;
        Toast.makeText(this, "Shizuku服务被终止", Toast.LENGTH_SHORT).show();
    };

    private void addEvent() {
        // 检查权限
        check_shizuku.setOnClickListener(view -> {
            if (!shizukuServiceState) {
                Toast.makeText(this, "Shizuku未运行", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkPermission()) {
                Toast.makeText(this, "已拥有权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "未拥有权限", Toast.LENGTH_SHORT).show();
            }
        });

        // 申请权限
        apply_shizuku.setOnClickListener(view -> {
            if (!shizukuServiceState) {
                Toast.makeText(this, "Shizuku未运行", Toast.LENGTH_SHORT).show();
                return;
            }

            requestShizukuPermission();
        });

        // 连接Shizuku服务
        connect_shizuku.setOnClickListener(view -> {
            if (!shizukuServiceState) {
                Toast.makeText(this, "Shizuku未运行", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkPermission()) {
                Toast.makeText(this, "没有Shizuku权限", Toast.LENGTH_SHORT).show();
                return;
            }

            if (iUserService != null) {
                Toast.makeText(this, "已连接Shizuku服务", Toast.LENGTH_SHORT).show();
                return;
            }

            // 绑定Shizuku服务
            Toast.makeText(this,"已尝试连接用户服务",Toast.LENGTH_SHORT).show();
            Shizuku.bindUserService(userServiceArgs, serviceConnection);
        });

        // 检查Shizuku服务
        checkconnect_shizuku.setOnClickListener(view -> {
            if (iUserService == null) {
                Toast.makeText(this, "未连接Shizuku用户服务", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"已连接Shizuku用户服务",Toast.LENGTH_SHORT).show();
            }
        });

        // 执行命令
        execute_shizuku.setOnClickListener(view -> {
            if (iUserService == null) {
                Toast.makeText(this, "请先连接Shizuku服务", Toast.LENGTH_SHORT).show();
                return;
            }
            // 执行命令逻辑（注释部分可取消注释）
            /*
            String command = input_command.getText().toString().trim();

            if (TextUtils.isEmpty(command)) {
                Toast.makeText(this, "命令不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String result = exec(command);

                if (result == null) {
                    result = "返回结果为null";
                } else if (TextUtils.isEmpty(result.trim())) {
                    result = "返回结果为空";
                }

                execute_result.setText(result);
            } catch (Exception e) {
                execute_result.setText(e.toString());
                e.printStackTrace();
            }
            */
        });
    }

    private String exec(String command) throws RemoteException {
        // 检查是否存在包含任意内容的双引号
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(command);

        // 下面展示了两种不同的命令执行方法
        if (matcher.find()) {
            ArrayList<String> list = new ArrayList<>();
            Pattern pattern2 = Pattern.compile("\"([^\"]*)\"|(\\S+)");
            Matcher matcher2 = pattern2.matcher(command);

            while (matcher2.find()) {
                if (matcher2.group(1) != null) {
                    // 如果是引号包裹的内容，取group(1)
                    list.add(matcher2.group(1));
                } else {
                    // 否则取group(2)，即普通的单词
                    list.add(matcher2.group(2));
                }
            }

            // 这种方法可用于执行路径中带空格的命令，例如 ls /storage/0/emulated/temp dir/
            // 当然也可以执行不带空格的命令，实际上是要强于另一种执行方式的
            return iUserService.execArr(list.toArray(new String[0]));
        } else {
            // 这种方法仅用于执行路径中不包含空格的命令，例如 ls /storage/0/emulated/
            return iUserService.execLine(command);
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(ShizukuActivity.this, "Shizuku服务连接成功", Toast.LENGTH_SHORT).show();

            if (iBinder != null && iBinder.pingBinder()) {
                iUserService = IUserService.Stub.asInterface(iBinder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(ShizukuActivity.this, "已断开Shizuku服务", Toast.LENGTH_SHORT).show();
            iUserService = null;
        }
    };

    private final Shizuku.UserServiceArgs userServiceArgs =
            new Shizuku.UserServiceArgs(new ComponentName(BuildConfig.APPLICATION_ID, UserService.class.getName()))
                    .daemon(false)
                    .processNameSuffix("adb_service")
                    .debuggable(BuildConfig.DEBUG)
                    .version(BuildConfig.VERSION_CODE);

    /**
     * 动态申请Shizuku adb shell权限
     */
    private void requestShizukuPermission() {
        if (Shizuku.getVersion() < 11) {
            Toast.makeText(this, "当前Shizuku版本不支持动态申请权限", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkPermission()) {
            Toast.makeText(this, "已拥有权限，无需重复申请", Toast.LENGTH_SHORT).show();
            return;
        }

        // 动态申请权限
        Shizuku.requestPermission(5201314);
    }

    private final Shizuku.OnRequestPermissionResultListener onRequestPermissionResultListener = new Shizuku.OnRequestPermissionResultListener() {
        @Override
        public void onRequestPermissionResult(int requestCode, int grantResult) {
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            if (granted) {
                Toast.makeText(ShizukuActivity.this, "Shizuku授权成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShizukuActivity.this, "Shizuku授权失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 判断是否拥有shizuku adb shell权限
     */
    private boolean checkPermission() {
        return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
    }

}