package com.cookandroid.candycrush.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.candycrush.MainActivity;
import com.cookandroid.candycrush.R;
import com.cookandroid.candycrush.util.ScoreBoard;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private ScoreBoard scoreBoard;
    private TextView currentScoreView;
    private GridLayout topScores;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        currentScoreView = findViewById(R.id.currentScore);
        topScores = findViewById(R.id.topScores);
        scoreBoard = new ScoreBoard(this);
        homeButton = findViewById(R.id.homeButton);

        // Intent에서 점수 받아오기
        int currentScore = getIntent().getIntExtra("score", 0);
        currentScoreView.setText("Your Score: " + currentScore);
        scoreBoard.addScore(currentScore);

        // 상위 점수 표시
        List<Integer> topScores = scoreBoard.getTopScores(5);
        addScoresToGrid(topScores);

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

    // 그리드에 점수 추가
    private void addScoresToGrid(List<Integer> scores) {
        topScores.removeAllViews(); // 기존 데이터 초기화
        for (int i = 0; i < scores.size(); i++) {
            // 순위 TextView
            TextView rankView = new TextView(this);
            rankView.setText((i + 1) + "위");
            rankView.setPadding(8, 8, 8, 8);
            rankView.setTextSize(16);

            // 점수 TextView
            TextView scoreView = new TextView(this);
            scoreView.setText(String.valueOf(scores.get(i)));
            scoreView.setPadding(8, 8, 8, 8);
            scoreView.setTextSize(16);

            // GridLayout에 추가
            topScores.addView(rankView);
            topScores.addView(scoreView);
        }
    }
}
