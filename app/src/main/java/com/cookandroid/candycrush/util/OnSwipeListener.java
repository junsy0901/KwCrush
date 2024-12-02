package com.cookandroid.candycrush.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import com.cookandroid.candycrush.R;
import com.cookandroid.candycrush.view.PlayActivity;

public class OnSwipeListener implements View.OnTouchListener {

    private PlayActivity activity;
    private int row;
    private int col;
    private CandyManager candyManager;

    private float startX, startY; // 터치 시작 좌표
    private final int SWIPE_THRESHOLD = 100; // 스와이프 최소 거리

    public OnSwipeListener(PlayActivity activity, int row, int col, CandyManager candyManager) {
        this.activity = activity;
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
            if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                if (deltaX > 0) {
                    newCol = col + 1; // 오른쪽
                } else {
                    newCol = col - 1; // 왼쪽
                }
            }
        } else {
            if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
                if (deltaY > 0) {
                    newRow = row + 1; // 아래
                } else {
                    newRow = row - 1; // 위
                }
            }
        }

        // 범위를 벗어나면 스와이프 무시
        if (newRow < 0 || newRow >= activity.candyBoard.length ||
                newCol < 0 || newCol >= activity.candyBoard[0].length) {
            return;
        }

        playSound(activity.getApplicationContext(), R.raw.swipe);

        // 캔디 스왑을 CandyManager로 처리하고, 매칭을 확인
        candyManager.swapAndCheckMatch(row, col, newRow, newCol, activity.candyBoard);
    }

    // 효과음 재생 메서드
    private void playSound(Context context, int soundResourceId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundResourceId);
        mediaPlayer.start();

        // 재생이 끝나면 리소스를 해제
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
        });
    }
}
