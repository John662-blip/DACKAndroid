package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Fragment.Fr_edit_admin;
import com.example.doancuoiky.Fragment.Fr_history_user;
import com.example.doancuoiky.Fragment.Fr_home_user;
import com.example.doancuoiky.Fragment.Fr_profile_user;
import com.example.doancuoiky.Fragment.Fr_statist_admin;
import com.example.doancuoiky.Preferences.PreManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_Main_Admin extends AppCompatActivity {
    FrameLayout frag_main;
    BottomNavigationView bt_navigation;
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    Fragment editFragment = new Fr_edit_admin();
    Fragment statistFragment = new Fr_statist_admin();
    Fragment activeFragment = editFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        AnhXa();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, statistFragment, "2").hide(statistFragment)
                    .add(R.id.frameLayout, editFragment, "1")
                    .commit();
        }
        setContent();
    }

    private void AnhXa() {
        preManager = new PreManager(this);
        frag_main = findViewById(R.id.frameLayout);
        bt_navigation = findViewById(R.id.navigation);
    }

    private void setContent() {
        bt_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.nav_edit) {
                selectedFragment = editFragment;
            } else if (itemId == R.id.nav_statist) {
                selectedFragment = statistFragment;
            } else {
                preManager.logout();
                Intent intent = new Intent(Activity_Main_Admin.this, Activity_Login.class);
                startActivity(intent);
                finish();
                return false;
            }

            if (selectedFragment != activeFragment) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(activeFragment)
                        .show(selectedFragment)
                        .commit();
                activeFragment = selectedFragment;
            }
            return true;
        });

        // Show home fragment mặc định
        bt_navigation.setSelectedItemId(R.id.nav_home);
    }
}
