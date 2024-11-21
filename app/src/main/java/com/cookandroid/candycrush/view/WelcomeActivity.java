package com.cookandroid.candycrush.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.candycrush.MainActivity;
import com.cookandroid.candycrush.R;

public class WelcomeActivity extends AppCompatActivity {

    private static final int WELCOME_DISPLAY_LENGTH = 3000; // 3초 동안 환영 화면 표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // activity_welcome.xml 사용

        // 환영 화면이 표시된 후 MainActivity로 전환
        new Handler().postDelayed(this::startMainActivity, WELCOME_DISPLAY_LENGTH);
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish(); // 환영 화면 종료
    }
}
