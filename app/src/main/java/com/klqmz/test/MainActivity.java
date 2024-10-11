package com.klqmz.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.klqmz.test.UI.HomeFragment;
import com.klqmz.test.UI.TestFragment;

public class MainActivity extends AppCompatActivity {

    private final Fragment homeFragment = new HomeFragment();
    private final Fragment testFragment = new TestFragment();
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_nav);

        // 初始化加载HomeFragment
        if (savedInstanceState == null) {
            currentFragment = homeFragment;
            loadFragment(currentFragment, false);
        }

        // 底部菜单栏切换
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                selectedFragment = homeFragment;
            } else if (itemId == R.id.navigation_test) {
                selectedFragment = testFragment;
            }

            if (selectedFragment != null && selectedFragment != currentFragment) {
                switchFragment(selectedFragment);
            }
            return true;
        });
    }

    // 切换Fragment
    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 设置动画效果
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        // 如果切换的Fragment没有加载则开始加载
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.fragment_container, targetFragment);
        }

        // 隐藏当前的Fragment并显示切换Fragment
        transaction.hide(currentFragment).show(targetFragment);

        // 提交
        transaction.commit();

        // 更新当前Fragment
        currentFragment = targetFragment;
    }

    // 初始加载Fragment的方法
    private void loadFragment(Fragment fragment, boolean withAnimation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (withAnimation) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        }

        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
