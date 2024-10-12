package com.klqmz.test.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.klqmz.test.BuildConfig;
import com.klqmz.test.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 设置构建时间
        TextView buildDateTextView = view.findViewById(R.id.home_buildtime_text);
        buildDateTextView.setText(BuildConfig.BUILD_DATE);  // 使用 BuildConfig 获取构建时间

        // 设置版本名称
        TextView versionNameTextView = view.findViewById(R.id.home_vname);
        versionNameTextView.setText(BuildConfig.VERSION_NAME);  // 使用 BuildConfig 获取版本名称

        // 加群按钮
        ImageButton joinButton = view.findViewById(R.id.home_joinQQGroup);
        joinButton.setOnClickListener(this::joinGroup);

        return view;
    }

    private void joinGroup(View view) {
        joinQQGroup("qOtNUzJvzEk6FcJFGjAKCSpjoILbOMTv");
    }

    public void joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(getContext(),"跳转失败",Toast.LENGTH_SHORT).show();
        }
    }

}
