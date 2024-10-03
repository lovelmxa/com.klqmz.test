package com.klqmz.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.klqmz.test.UI.HomeFragment;
import com.klqmz.test.UI.TestFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<Integer, Fragment> fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_nav);

        // 初始化 Fragment 映射
        initFragmentMap();

        // 默认加载 HomeFragment
        loadFragment(fragmentMap.get(R.id.navigation_home));

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = fragmentMap.get(item.getItemId());

            // 替换当前的 Fragment
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void initFragmentMap() {
        // 使用 Map 关联 MenuItem ID 和 Fragment
        fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.navigation_home, new HomeFragment());
        fragmentMap.put(R.id.navigation_test, new TestFragment());
        // 可以继续添加更多 Fragment
    }

    private void loadFragment(Fragment fragment) {
        // 使用 FragmentManager 替换 Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}