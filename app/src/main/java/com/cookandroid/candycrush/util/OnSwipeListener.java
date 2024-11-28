package com.cookandroid.candycrush.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.cookandroid.candycrush.view.PlayActivity;

public class OnSwipeListener implements View.OnTouchListener {

    private PlayActivity activity;
    private ImageView candy;
    private int row;
    private int col;
    private CandyManager candyManager;

    private float startX, startY; // 터치 시작 좌표
    private final int SWIPE_THRESHOLD = 100; // 스와이프 최소 거리

    public OnSwipeListener(PlayActivity activity, ImageView candy, int row, int col, CandyManager candyManager) {
        this.activity = activity;
        this.candy = candy;
        this.row = row;
        this.col = col;
        this.candyManager = candyManager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX(); // 터치 시작 X 좌표
                startY = event.getY(); // 터치 시작 Y 좌표
                return true;

            case MotionEvent.ACTION_UP:
                float endX = event.getX(); // 터치 종료 X 좌표
                float endY = event.getY(); // 터치 종료 Y 좌표
                handleSwipe(endX - startX, endY - startY); // 스와이프 처리
                return true;

            default:
                return false;
        }
    }

    private void handleSwipe(float deltaX, float deltaY) {
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // 가로 스와이프
            if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                if (deltaX > 0) {
                    // 오른쪽 스와이프
                    candyManager.swapCandies(row, col, row, col + 1, activity.candyBoard);
                } else {
                    // 왼쪽 스와이프
                    candyManager.swapCandies(row, col, row, col - 1, activity.candyBoard);
                }
            }
        } else {
            // 세로 스와이프
            if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                if (deltaY > 0) {
                    // 아래쪽 스와이프
                    candyManager.swapCandies(row, col, row + 1, col, activity.candyBoard);
                } else {
                    // 위쪽 스와이프
                    candyManager.swapCandies(row, col, row - 1, col, activity.candyBoard);
                }
            }
        }
    }
}
