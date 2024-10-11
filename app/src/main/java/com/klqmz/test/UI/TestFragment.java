package com.klqmz.test.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.klqmz.test.JumpActivity;
import com.klqmz.test.R;
import com.klqmz.test.ShizukuActivity;

public class TestFragment extends Fragment {

    public TestFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        // 获取按钮并设置点击事件
        Button shizukuButton = view.findViewById(R.id.test_shizuku);
        shizukuButton.setOnClickListener(this::switchToShizukuActivity);

        Button jumpButton = view.findViewById(R.id.test_jump);
        jumpButton.setOnClickListener(this::switchToJumpActivity);
        return view;
    }

    public void switchToShizukuActivity(View view){
        Intent intent = new Intent(getContext(), ShizukuActivity.class);
        startActivity(intent);
    }
    public void switchToJumpActivity(View view){
        Intent intent = new Intent(getContext(), JumpActivity.class);
        startActivity(intent);
    }
}

