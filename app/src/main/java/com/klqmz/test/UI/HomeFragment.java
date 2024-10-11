package com.klqmz.test.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.klqmz.test.BuildConfig;
import com.klqmz.test.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //设置构建时间
        TextView buildDateTextView = view.findViewById(R.id.home_buildtime_text);
        buildDateTextView.setText(BuildConfig.BUILD_DATE);  // 使用 BuildConfig 获取构建时间
        //设置版本名称
        TextView versionNameTextView = view.findViewById(R.id.home_vname);
        versionNameTextView.setText(BuildConfig.VERSION_NAME);  // 使用 BuildConfig 获取版本名称

        return view;
    }
}
