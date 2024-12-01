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
    private TextView endGameStatusTextView; // 종료 상태를 표시할 TextView
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // UI 요소 연결
        scoreTextView = findViewById(R.id.scoreTextView);
        endGameStatusTextView = findViewById(R.id.endGameStatus); // 종료 상태 텍스트 뷰
        homeButton = findViewById(R.id.homeButton);

        // Intent에서 점수와 종료 타입 받아오기
        int score = getIntent().getIntExtra("score", 0);
        int gameEndType = getIntent().getIntExtra("gameEndType", 0); // 종료 타입 받기

        // 종료 타입에 따른 상태 메시지 설정
        String endMessage = "";
        switch (gameEndType) {
            case 1:
                endMessage = "Game Over\nNo moves left.";
                break;
            case 2:
                endMessage = "Game Over\nNo possible swaps.";
                break;
            default:
                endMessage = "Game ended unexpectedly.";
                break;
        }
        endGameStatusTextView.setText(endMessage); // 종료 상태 메시지 설정

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
