package ir.shariaty.myjava;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public abstract class BaseActivity extends AppCompatActivity {

    protected void setupHeader(boolean showBack) {
        View headerView = findViewById(R.id.headerView);
        if (headerView == null) return;

        View btnBack = headerView.findViewById(R.id.btnBack);
        if (btnBack == null) return;

        if (showBack) {
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(v ->
                    getOnBackPressedDispatcher().onBackPressed()
            );
        } else {
            btnBack.setVisibility(View.GONE);
        }
    }


    protected void setupHeader(View.OnClickListener backListener) {
        View headerView = findViewById(R.id.headerView);
        if (headerView == null) return;

        View btnBack = headerView.findViewById(R.id.btnBack);
        if (btnBack == null) return;

        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(backListener);


    }

// Bottom Navigation

    protected static final int NO_SELECTED_ITEM = -1;

    protected void setupBottomNav(int selectedItemId) {

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavView);

        if (bottomNav == null) {
            bottomNav = findViewById(R.id.bottomNav);
        }

        if (bottomNav == null) {
            return;
        }

        if (selectedItemId != NO_SELECTED_ITEM) {

            bottomNav.setSelectedItemId(selectedItemId);

        } else {

            for (int i = 0; i < bottomNav.getMenu().size(); i++) {
                bottomNav.getMenu().getItem(i).setChecked(false);
            }
        }

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == selectedItemId && isCurrentActivity(id)) {
                return true;
            }

            else if (id == R.id.nav_home) {

                navigateToHome();
                return true;

            }

            else if (id == R.id.nav_account) {

                navigateToAccount();
                return true;

            }

            else if (id == R.id.nav_settings) {

                navigateToSettings();
                return true;

            }

            return false;
        });

    }

    protected void setupBottomNav() {
        setupBottomNav(NO_SELECTED_ITEM);
    }

    protected void navigateToHome() {
        // بعدا فعال شود زمانی که صفحه خانه ساخته شد
//        Intent intent = new Intent(this, HomeActivity.class);
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        startActivity(intent);
    }

    protected void navigateToAccount() {

        // بعدا فعال شود زمانی که صفحه پروفایل ساخته شد
//       Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

        Toast.makeText(this,
                "پروفایل",
                Toast.LENGTH_SHORT).show();
    }

    protected void navigateToSettings() {

        // بعدا فعال شود زمانی که صفحه تنظیمات ساخته شد
        // Intent intent = new Intent(this, SettingsActivity.class);
        // startActivity(intent);

        Toast.makeText(this,
                "تنظیمات",
                Toast.LENGTH_SHORT).show();
    }


    private boolean isCurrentActivity(int navItemId) {

//            if (navItemId == R.id.nav_home &&
//                    this instanceof HomeActivity) {
//                return true;
//            }

    /*
    if (navItemId == R.id.nav_account &&
            this instanceof ProfileActivity) {
        return true;
    }

    if (navItemId == R.id.nav_settings &&
            this instanceof SettingsActivity) {
        return true;
    }
    */

        return false;
    }
}
