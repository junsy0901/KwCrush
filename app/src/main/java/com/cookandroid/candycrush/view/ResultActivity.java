package com.cookandroid.candycrush.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.candycrush.MainActivity;
import com.cookandroid.candycrush.R;

public class ResultActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreTextView = findViewById(R.id.scoreTextView);
        homeButton = findViewById(R.id.homeButton);

        // Intent에서 점수 받아오기
        int score = getIntent().getIntExtra("score", 0);
        scoreTextView.setText("Your Score: " + score);

        // 홈 버튼 클릭 리스너 설정
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 홈 화면으로 돌아가기
                Intent intent = new Intent(ResultActivity.this, MainActivity.class); // MainActivity는 홈 화면 액티비티
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            }
        });
    }
}
