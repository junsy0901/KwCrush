package com.cookandroid.candycrush.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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
    private int movecount;
    private TextView scoreView;
    private TextView countView;
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
        countView = findViewById(R.id.movecount);
        candyBoard = new ImageView[gridSize][gridSize];
        candyManager = new CandyManager(this);

        createBoard();

        GridLayout board = findViewById(R.id.board);
        adjustBoardSize(board);

        // 스와이프 리스너 설정
        setSwipeListener();
    }

    private void adjustBoardSize(GridLayout board) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.7);
        int candySize = Math.min(screenWidth, screenHeight) / gridSize;

        ViewGroup.LayoutParams params = board.getLayoutParams();
        params.width = candySize * gridSize;
        params.height = candySize * gridSize;
        board.setLayoutParams(params);
    }

    private void createBoard() {
        movecount = 15;
        GridLayout board = findViewById(R.id.board);
        board.setRowCount(gridSize);
        board.setColumnCount(gridSize);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        int cellWidth = screenWidth / gridSize;

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                // 캔디 생성
                ImageView candy = new ImageView(this);
                int randomCandy = candyResources[random.nextInt(candyResources.length)];
                candy.setImageResource(randomCandy);
                candy.setTag(randomCandy);

                // 캔디 비율 유지 및 잘리지 않게 설정
                candy.setScaleType(ImageView.ScaleType.FIT_CENTER);

                // 레이아웃 파라미터 생성
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cellWidth;  // 셀 너비
                params.rowSpec = GridLayout.spec(row);  // 행 위치
                params.columnSpec = GridLayout.spec(col);  // 열 위치
                params.setGravity(Gravity.FILL); // 셀 크기를 완전히 채우도록 설정
                candy.setLayoutParams(params);

                // 캔디 보드에 추가
                candyBoard[row][col] = candy;
                board.addView(candy);
            }
        }
        setSwipeListener();
        boolean matchFound = true;
        while(matchFound){
            matchFound = candyManager.processMatches(candyBoard);
        }
    }

    // 스와이프 리스너 설정
    private void setSwipeListener() {
        // 각 캔디에 대해 개별적으로 OnSwipeListener를 설정
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                ImageView candy = candyBoard[row][col];
                // 각 캔디에 대해 OnSwipeListener를 생성하고, 해당 캔디의 위치를 전달
                candy.setOnTouchListener(new OnSwipeListener(this, row, col, candyManager));
            }
        }
    }


    // 점수 업데이트
    public void updateScore(int points) {
        score += points;
        scoreView.setText(String.valueOf(score));
    }

    public void updateMoveCount() {
        movecount--;
        countView.setText(String.valueOf(movecount));

        if (movecount <= 0) {
            endGame();
        }
    }


    // 랜덤 캔디 리소스 반환
    public int getRandomCandyResource() {
        return candyResources[random.nextInt(candyResources.length)];
    }

    // 게임 종료 처리
    public void endGame() {
        Intent intent = new Intent(PlayActivity.this, ResultActivity.class);
        // 점수를 Intent에 담아 전달
        intent.putExtra("score", score);
        startActivity(intent);
        finish();  // 현재 Activity 종료
    }


    public int getNoOfBlock() {
        return gridSize;
    }


}
