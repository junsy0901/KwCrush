package com.cookandroid.candycrush.view;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.candycrush.R;
import com.cookandroid.candycrush.util.CandyManager;
import com.cookandroid.candycrush.util.OnSwipeListener;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    public ImageView[][] candyBoard;
    private int gridSize = 7; // 보드 크기
    private int score = 0; // 점수
    private TextView scoreView;
    private Random random = new Random();

    private int[] candyResources = {
            R.drawable.bluecandy,
            R.drawable.greencandy,
            R.drawable.orangecandy,
            R.drawable.purplecandy,
            R.drawable.redcandy,
            R.drawable.yellowcandy
    };

    private CandyManager candyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        scoreView = findViewById(R.id.score);
        candyBoard = new ImageView[gridSize][gridSize];
        candyManager = new CandyManager(this);

        createBoard();

        GridLayout board = findViewById(R.id.board);
        adjustBoardSize(board);

        // 초기 매칭 검사 및 점수 처리
        candyManager.findAndProcessMatches(candyBoard);
    }

    private void adjustBoardSize(GridLayout board) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        // GridLayout 높이를 XML에서 정의한 비율에 맞게 설정
        int screenHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.7);
        int candySize = Math.min(screenWidth, screenHeight) / gridSize;

        // 캔디 크기를 고정
        ViewGroup.LayoutParams params = board.getLayoutParams();
        params.width = candySize * gridSize;
        params.height = candySize * gridSize;
        board.setLayoutParams(params);
    }

    private void createBoard() {
        GridLayout board = findViewById(R.id.board);
        board.setRowCount(gridSize);
        board.setColumnCount(gridSize);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.7);
        int candySize = Math.min(screenWidth, screenHeight) / gridSize;

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                ImageView candy = new ImageView(this);
                int randomCandy = candyResources[random.nextInt(candyResources.length)];
                candy.setImageResource(randomCandy);
                candy.setTag(randomCandy);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = candySize; // 캔디 크기 설정
                params.height = candySize;
                params.setMargins(2, 2, 2, 2); // 캔디 간격 추가
                candy.setLayoutParams(params);

                candy.setScaleType(ImageView.ScaleType.FIT_CENTER); // 비율 유지
                candyBoard[row][col] = candy; // 캔디 보드 배열에 저장
                board.addView(candy);

                // 스와이프 이벤트 연결
                candy.setOnTouchListener(new OnSwipeListener(this, candy, row, col, candyManager));
            }
        }
    }

    public int getNoOfBlock() {
        return gridSize;
    }

    public void updateScore(int points) {
        score += points;
        scoreView.setText(String.valueOf(score));
    }

    public int getRandomCandyResource() {
        return candyResources[random.nextInt(candyResources.length)];
    }
}
