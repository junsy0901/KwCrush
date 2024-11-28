package com.cookandroid.candycrush.util;

import android.graphics.drawable.Drawable;
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
        int newRow = row;
        int newCol = col;

        // 스와이프 방향에 따른 새로운 위치 계산
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // 가로 스와이프
            if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                if (deltaX > 0) {
                    // 오른쪽 스와이프
                    newCol = col + 1;
                } else {
                    // 왼쪽 스와이프
                    newCol = col - 1;
                }
            }
        } else {
            // 세로 스와이프
            if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                if (deltaY > 0) {
                    // 아래쪽 스와이프
                    newRow = row + 1;
                } else {
                    // 위쪽 스와이프
                    newRow = row - 1;
                }
            }
        }

        // 범위를 벗어나면 스와이프 무시
        if (newRow < 0 || newRow >= activity.candyBoard.length ||
                newCol < 0 || newCol >= activity.candyBoard[0].length) {
            return;
        }

        // 캔디 상태 저장
        Object originalTag1 = activity.candyBoard[row][col].getTag();
        Object originalTag2 = activity.candyBoard[newRow][newCol].getTag();
        Drawable originalDrawable1 = activity.candyBoard[row][col].getDrawable();
        Drawable originalDrawable2 = activity.candyBoard[newRow][newCol].getDrawable();

        // 캔디 스왑
        candyManager.swapCandies(row, col, newRow, newCol, activity.candyBoard);

        // 매칭 여부 확인
        boolean isMatched = candyManager.checkMatch(newRow, newCol, activity.candyBoard) ||
                candyManager.checkMatch(row, col, activity.candyBoard);

        if (!isMatched) {
            // 매칭되지 않으면 원래 상태로 복구
            activity.candyBoard[row][col].setTag(originalTag1);
            activity.candyBoard[row][col].setImageDrawable(originalDrawable1);
            activity.candyBoard[newRow][newCol].setTag(originalTag2);
            activity.candyBoard[newRow][newCol].setImageDrawable(originalDrawable2);
        } else {
            // 매칭된 경우 처리
            candyManager.resolveMatches(activity.candyBoard);
        }
    }
}
