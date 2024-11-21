package com.cookandroid.candycrush.util;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.cookandroid.candycrush.util.CandyManager;
import com.cookandroid.candycrush.view.PlayActivity;

public class OnSwipeListener {

    private GestureDetector gestureDetector;
    private PlayActivity playActivity;
    private int candyIndex;
    private CandyManager candyManager;

    public OnSwipeListener(PlayActivity activity, View candyView, int index, CandyManager manager) {
        this.playActivity = activity;
        this.candyIndex = index;
        this.candyManager = manager;

        gestureDetector = new GestureDetector(candyView.getContext(), new GestureListener());
        candyView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float xDiff = e2.getX() - e1.getX();
            float yDiff = e2.getY() - e1.getY();

            try {
                if (Math.abs(xDiff) > Math.abs(yDiff)) {
                    if (Math.abs(xDiff) > 50 && Math.abs(velocityX) > 50) {
                        if (xDiff > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(yDiff) > 50 && Math.abs(velocityY) > 50) {
                        if (yDiff > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        private void onSwipeRight() {
            if (candyIndex % playActivity.getNoOfBlock() < playActivity.getNoOfBlock() - 1) {
                candyManager.swapCandies(candyIndex, candyIndex + 1, playActivity.candyList);
            }
        }

        private void onSwipeLeft() {
            if (candyIndex % playActivity.getNoOfBlock() > 0) {
                candyManager.swapCandies(candyIndex, candyIndex - 1, playActivity.candyList);
            }
        }

        private void onSwipeUp() {
            if (candyIndex >= playActivity.getNoOfBlock()) {
                candyManager.swapCandies(candyIndex, candyIndex - playActivity.getNoOfBlock(), playActivity.candyList);
            }
        }

        private void onSwipeDown() {
            if (candyIndex < playActivity.getNoOfBlock() * (playActivity.getNoOfBlock() - 1)) {
                candyManager.swapCandies(candyIndex, candyIndex + playActivity.getNoOfBlock(), playActivity.candyList);
            }
        }
    }
}
