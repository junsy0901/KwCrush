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
        boolean hasMatch;

        do {
            hasMatch = checkAndRemoveMatches(candyBoard);
            if (hasMatch) {
                applyGravity(candyBoard);
            }
        } while (hasMatch);
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

        // 매칭 검사 및 처리
        findAndProcessMatches(candyBoard);
    }

    private boolean checkAndRemoveMatches(ImageView[][] candyBoard) {
        Set<int[]> matches = new HashSet<>();
        boolean hasMatch = false;

        // 가로 매칭 검사
        for (int row = 0; row < candyBoard.length; row++) {
            int startCol = 0;
            while (startCol < candyBoard[row].length) {
                int matchCount = 1;
                for (int col = startCol + 1; col < candyBoard[row].length; col++) {
                    if (candyBoard[row][startCol].getTag() != null &&
                            candyBoard[row][startCol].getTag().equals(candyBoard[row][col].getTag())) {
                        matchCount++;
                    } else {
                        break;
                    }
                }

                if (matchCount >= 3) {
                    for (int i = 0; i < matchCount; i++) {
                        matches.add(new int[]{row, startCol + i});
                    }
                    hasMatch = true;
                }

                startCol += matchCount;
            }
        }

        // 세로 매칭 검사
        for (int col = 0; col < candyBoard[0].length; col++) {
            int startRow = 0;
            while (startRow < candyBoard.length) {
                int matchCount = 1;
                for (int row = startRow + 1; row < candyBoard.length; row++) {
                    if (candyBoard[startRow][col].getTag() != null &&
                            candyBoard[startRow][col].getTag().equals(candyBoard[row][col].getTag())) {
                        matchCount++;
                    } else {
                        break;
                    }
                }

                if (matchCount >= 3) {
                    for (int i = 0; i < matchCount; i++) {
                        matches.add(new int[]{startRow + i, col});
                    }
                    hasMatch = true;
                }

                startRow += matchCount;
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
                if (candyBoard[row][col].getTag() == null) { // 빈 공간이면
                    for (int upperRow = row - 1; upperRow >= 0; upperRow--) {
                        if (candyBoard[upperRow][col].getTag() != null) { // 위에 있는 캔디 찾기
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

        // 최상단에 새로운 캔디 생성
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
