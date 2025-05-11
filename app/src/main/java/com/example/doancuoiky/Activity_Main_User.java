package com.example.doancuoiky;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Fragment.Fr_history_user;
import com.example.doancuoiky.Fragment.Fr_home_user;
import com.example.doancuoiky.Fragment.Fr_profile_user;
import com.example.doancuoiky.Preferences.PreManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_Main_User extends AppCompatActivity {
    FrameLayout frag_main;
    BottomNavigationView bt_navigation;
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

    // Khai báo 3 Fragment
    Fragment homeFragment = new Fr_home_user();
    Fragment historyFragment = new Fr_history_user();
    Fragment profileFragment = new Fr_profile_user();
    Fragment activeFragment = homeFragment; // Fragment hiện tại

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        AnhXa();

        // Thêm tất cả Fragment một lần
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, profileFragment, "3").hide(profileFragment)
                .add(R.id.frameLayout, historyFragment, "2").hide(historyFragment)
                .add(R.id.frameLayout, homeFragment, "1")
                .commit();

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

            // Reset icon
            bt_navigation.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.baseline_home_24);
            bt_navigation.getMenu().findItem(R.id.nav_history).setIcon(R.drawable.history);
            bt_navigation.getMenu().findItem(R.id.nav_profile).setIcon(R.drawable.user_solid);

            Fragment selectedFragment = null;

            if (itemId == R.id.nav_home) {
                item.setIcon(R.drawable.baseline_home_24_bold);
                selectedFragment = homeFragment;

            } else if (itemId == R.id.nav_history) {
                item.setIcon(R.drawable.history_bold);
                selectedFragment = historyFragment;

            } else if (itemId == R.id.nav_profile) {
                item.setIcon(R.drawable.user_solid_bold);
                selectedFragment = profileFragment;
            }

            if (selectedFragment == activeFragment) {
                Fragment newFragment = null;

                if (selectedFragment instanceof Fr_home_user) {
                    newFragment = new Fr_home_user();
                    homeFragment = newFragment;
                } else if (selectedFragment instanceof Fr_history_user) {
                    newFragment = new Fr_history_user();
                    historyFragment = newFragment;
                } else if (selectedFragment instanceof Fr_profile_user) {
                    newFragment = new Fr_profile_user();
                    profileFragment = newFragment;
                }

                if (newFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(selectedFragment)
                            .add(R.id.frameLayout, newFragment)
                            .commit();
                    activeFragment = newFragment;
                }
            } else {
                // Chuyển sang fragment khác
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(activeFragment)
                        .show(selectedFragment)
                        .commit();
                activeFragment = selectedFragment;
                if (selectedFragment instanceof Fr_home_user) {
                    ((Fr_home_user) selectedFragment).LoadAPI_IMAGE();
                }
            }

            return true;
        });

        // Show home fragment mặc định
        bt_navigation.setSelectedItemId(R.id.nav_home);
    }
    public void recreateAllFragments() {
        getSupportFragmentManager().beginTransaction()
                .remove(homeFragment)
                .remove(historyFragment)
                .remove(profileFragment)
                .commitNow();

        homeFragment = new Fr_home_user();
        historyFragment = new Fr_history_user();
        profileFragment = new Fr_profile_user();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, profileFragment, "3").hide(profileFragment)
                .add(R.id.frameLayout, historyFragment, "2").hide(historyFragment)
                .add(R.id.frameLayout, homeFragment, "1")
                .commit();

        activeFragment = homeFragment;
    }

    public String getCurrentFragmentTag() {
        if (activeFragment instanceof Fr_home_user) return "Home";
        if (activeFragment instanceof Fr_history_user) return "History";
        if (activeFragment instanceof Fr_profile_user) return "Profile";
        return "Unknown";
    }


}
