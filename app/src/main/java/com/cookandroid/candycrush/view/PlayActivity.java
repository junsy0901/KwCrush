package com.cookandroid.candycrush.view;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.candycrush.R;
import com.cookandroid.candycrush.util.CandyManager;
import com.cookandroid.candycrush.util.OnSwipeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    public List<ImageView> candyList;
    private int gridSize = 7;
    private int score = 0;
    private TextView scoreView;
    private Random random = new Random();
    private int selectedCandyIndex = -1;

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
        candyList = new ArrayList<>();
        candyManager = new CandyManager(this);
        createBoard();
    }

    private void createBoard() {
        GridLayout board = findViewById(R.id.board);
        board.setRowCount(gridSize);
        board.setColumnCount(gridSize);

        for (int i = 0; i < gridSize * gridSize; i++) {
            ImageView candy = new ImageView(this);
            int randomCandy = candyResources[random.nextInt(candyResources.length)];
            candy.setImageResource(randomCandy);
            candy.setTag(randomCandy);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            int size = getResources().getDisplayMetrics().widthPixels / gridSize;
            params.width = size;
            params.height = size;
            params.setMargins(4, 4, 4, 4);
            candy.setLayoutParams(params);

            final int index = i;
            candyList.add(candy);
            board.addView(candy);

            // OnSwipeListener 연결
            new OnSwipeListener(this, candy, index, candyManager);
        }
    }

    public int getSelectedCandyIndex() {
        return selectedCandyIndex;
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
