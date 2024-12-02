package com.cookandroid.candycrush.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.cookandroid.candycrush.MainActivity;
import com.cookandroid.candycrush.R;
import com.cookandroid.candycrush.util.SQLiteHelper;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView endGameStatusTextView; // 종료 상태를 표시할 TextView
    private Button homeButton;
    private GridLayout topScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // UI 요소 연결
        scoreTextView = findViewById(R.id.scoreTextView);
        endGameStatusTextView = findViewById(R.id.endGameStatus); // 종료 상태 텍스트 뷰
        homeButton = findViewById(R.id.homeButton);
        topScores = findViewById(R.id.topScores);

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

        scoreTextView.setText("Current Score:" + score); // 점수를 UI에 표시

        // SQLiteHelper 초기화 및 점수 저장
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        dbHelper.addScore(score); // 점수 저장

        // 상위 점수 가져오기 및 GridLayout에 표시
        List<Integer> topScores = dbHelper.getTopScores(5);
        displayTopScores(topScores);

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

    private void displayTopScores(List<Integer> scores) {
        topScores.removeAllViews(); // 기존 뷰 제거

        Typeface customFont = ResourcesCompat.getFont(this, R.font.maplestory_bold);

        for (int i = 0; i < scores.size(); i++) {
            // 순위 텍스트 뷰
            TextView rankTextView = new TextView(this);
            rankTextView.setText((i + 1) + "위");
            rankTextView.setTextSize(18);
            rankTextView.setTypeface(customFont);
            rankTextView.setPadding(8, 8, 8, 8);

            // 점수 텍스트 뷰
            TextView scoreTextView = new TextView(this);
            scoreTextView.setText(String.valueOf(scores.get(i)));
            scoreTextView.setTextSize(18);
            scoreTextView.setTypeface(customFont);
            scoreTextView.setPadding(8, 8, 8, 8);

            // GridLayout에 추가
            topScores.addView(rankTextView);
            topScores.addView(scoreTextView);
        }
    }
}
