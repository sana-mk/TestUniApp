package ir.shariaty.myjava;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import ir.shariaty.myjava.utils.TokenManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View bottomPane = findViewById(R.id.bottomPane);
        View logoImage = findViewById(R.id.logoImage);

        bottomPane.post(() -> {
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            bottomPane.setTranslationY(screenHeight);
            logoImage.setTranslationY(screenHeight);

            DecelerateInterpolator sharedInterpolator = new DecelerateInterpolator(1.5f);

            bottomPane.animate()
                    .translationY(0f)
                    .setDuration(800)
                    .setInterpolator(sharedInterpolator)
                    .start();

            logoImage.animate()
                    .translationY(0f)
                    .setDuration(800)
                    .setInterpolator(sharedInterpolator)
                    .withEndAction(() -> {
                        ObjectAnimator pulsate = ObjectAnimator.ofPropertyValuesHolder(
                                logoImage,
                                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.05f, 1.0f),
                                PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.05f, 1.0f)
                        );
                        pulsate.setDuration(1200);
                        pulsate.setRepeatCount(ObjectAnimator.INFINITE);
                        pulsate.start();
                    })
                    .start();
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            TokenManager tokenManager = new TokenManager(this);
            Intent intent;
            if (tokenManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, CoursesActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2800);
    }
}