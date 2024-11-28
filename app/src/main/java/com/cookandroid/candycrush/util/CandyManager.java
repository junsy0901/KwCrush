package com.cookandroid.candycrush.util;

import android.widget.ImageView;

import com.cookandroid.candycrush.view.PlayActivity;

import java.util.HashSet;
import java.util.Set;

public class CandyManager {

    private PlayActivity playActivity;

    public CandyManager(PlayActivity playActivity) {
        this.playActivity = playActivity;
    }

    public void findAndProcessMatches(ImageView[][] candyBoard) {
        resolveMatches(candyBoard);
    }

    public void swapCandies(int row1, int col1, int row2, int col2, ImageView[][] candyBoard) {
        // 범위를 벗어난 경우 무시
        if (row2 < 0 || col2 < 0 || row2 >= candyBoard.length || col2 >= candyBoard[0].length) {
            return;
        }

        // 캔디 교환
        ImageView candy1 = candyBoard[row1][col1];
        ImageView candy2 = candyBoard[row2][col2];

        Object tag1 = candy1.getTag();
        Object tag2 = candy2.getTag();

        candy1.setImageDrawable(candy2.getDrawable());
        candy1.setTag(tag2);

        candy2.setImageDrawable(candy1.getDrawable());
        candy2.setTag(tag1);
    }

    public boolean checkMatch(int row, int col, ImageView[][] candyBoard) {
        Object targetTag = candyBoard[row][col].getTag();
        if (targetTag == null) {
            return false;
        }

        // 가로 방향 매칭 확인
        int horizontalCount = 1;
        for (int i = col - 1; i >= 0; i--) {
            if (candyBoard[row][i].getTag() != null && candyBoard[row][i].getTag().equals(targetTag)) {
                horizontalCount++;
            } else {
                break;
            }
        }
        for (int i = col + 1; i < candyBoard[row].length; i++) {
            if (candyBoard[row][i].getTag() != null && candyBoard[row][i].getTag().equals(targetTag)) {
                horizontalCount++;
            } else {
                break;
            }
        }

        // 세로 방향 매칭 확인
        int verticalCount = 1;
        for (int i = row - 1; i >= 0; i--) {
            if (candyBoard[i][col].getTag() != null && candyBoard[i][col].getTag().equals(targetTag)) {
                verticalCount++;
            } else {
                break;
            }
        }
        for (int i = row + 1; i < candyBoard.length; i++) {
            if (candyBoard[i][col].getTag() != null && candyBoard[i][col].getTag().equals(targetTag)) {
                verticalCount++;
            } else {
                break;
            }
        }

        return horizontalCount >= 3 || verticalCount >= 3;
    }

    public void resolveMatches(ImageView[][] candyBoard) {
        boolean hasMatch;

        do {
            hasMatch = checkAndRemoveMatches(candyBoard);
            if (hasMatch) {
                applyGravity(candyBoard);
            }
        } while (hasMatch);
    }

    private boolean checkAndRemoveMatches(ImageView[][] candyBoard) {
        Set<int[]> matches = new HashSet<>();
        boolean hasMatch = false;

        // 가로 매칭 검사
        for (int row = 0; row < candyBoard.length; row++) {
            int matchCount = 1;
            for (int col = 1; col < candyBoard[row].length; col++) {
                if (candyBoard[row][col].getTag() != null &&
                        candyBoard[row][col].getTag().equals(candyBoard[row][col - 1].getTag())) {
                    matchCount++;
                } else {
                    if (matchCount >= 3) {
                        // 매칭된 좌표 저장
                        for (int i = 0; i < matchCount; i++) {
                            matches.add(new int[]{row, col - i - 1});
                        }
                        hasMatch = true;
                    }
                    matchCount = 1; // 매칭 길이 초기화
                }
            }
            // 마지막 연속 매칭 확인
            if (matchCount >= 3) {
                for (int i = 0; i < matchCount; i++) {
                    matches.add(new int[]{row, candyBoard[row].length - i - 1});
                }
                hasMatch = true;
            }
        }

        // 세로 매칭 검사
        for (int col = 0; col < candyBoard[0].length; col++) {
            int matchCount = 1;
            for (int row = 1; row < candyBoard.length; row++) {
                if (candyBoard[row][col].getTag() != null &&
                        candyBoard[row][col].getTag().equals(candyBoard[row - 1][col].getTag())) {
                    matchCount++;
                } else {
                    if (matchCount >= 3) {
                        // 매칭된 좌표 저장
                        for (int i = 0; i < matchCount; i++) {
                            matches.add(new int[]{row - i - 1, col});
                        }
                        hasMatch = true;
                    }
                    matchCount = 1; // 매칭 길이 초기화
                }
            }
            // 마지막 연속 매칭 확인
            if (matchCount >= 3) {
                for (int i = 0; i < matchCount; i++) {
                    matches.add(new int[]{candyBoard.length - i - 1, col});
                }
                hasMatch = true;
            }
        }

        // 매칭된 캔디 제거
        if (!matches.isEmpty()) {
            for (int[] match : matches) {
                int row = match[0];
                int col = match[1];
                candyBoard[row][col].setImageResource(android.R.color.transparent);
                candyBoard[row][col].setTag(null); // 빈 공간 처리
            }

            // 점수 추가
            playActivity.updateScore(matches.size() * 10); // 매칭된 캔디 개수에 비례하여 점수 추가
        }

        return hasMatch;
    }

    private void applyGravity(ImageView[][] candyBoard) {
        for (int col = 0; col < candyBoard[0].length; col++) {
            for (int row = candyBoard.length - 1; row > 0; row--) {
                if (candyBoard[row][col].getTag() == null) {
                    for (int upperRow = row - 1; upperRow >= 0; upperRow--) {
                        if (candyBoard[upperRow][col].getTag() != null) {
                            candyBoard[row][col].setImageDrawable(candyBoard[upperRow][col].getDrawable());
                            candyBoard[row][col].setTag(candyBoard[upperRow][col].getTag());

                            candyBoard[upperRow][col].setImageResource(android.R.color.transparent);
                            candyBoard[upperRow][col].setTag(null);
                            break;
                        }
                    }
                }
            }
        }

        for (int col = 0; col < candyBoard[0].length; col++) {
            for (int row = 0; row < candyBoard.length; row++) {
                if (candyBoard[row][col].getTag() == null) {
                    int randomCandy = playActivity.getRandomCandyResource();
                    candyBoard[row][col].setImageResource(randomCandy);
                    candyBoard[row][col].setTag(randomCandy);
                }
            }
        }
    }
}
