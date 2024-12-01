package com.cookandroid.candycrush.util;

import android.widget.ImageView;
import android.widget.Toast;

import com.cookandroid.candycrush.view.PlayActivity;
import java.util.ArrayList;

public class CandyManager {

    private PlayActivity activity;
    private ArrayList<int[]> matchedCandies = new ArrayList<>();

    public CandyManager(PlayActivity activity) {
        this.activity = activity;
    }

    // 두 캔디 스왑 후 매칭 여부 확인
    public void swapAndCheckMatch(int fromRow, int fromCol, int toRow, int toCol, ImageView[][] candyBoard) {
        swapCandies(fromRow, fromCol, toRow, toCol, candyBoard);
        boolean matchFound = processMatches(candyBoard);
        if (!matchFound) {
            // 매칭이 없으면 다시 스왑
            swapCandies(fromRow, fromCol, toRow, toCol, candyBoard);
            Toast.makeText(activity, "매치안됨", Toast.LENGTH_SHORT).show();
        }
        else{
            int num = 1;
            activity.updateMoveCount();
            while(matchFound){
                matchFound = processMatches(candyBoard);
                Toast.makeText(activity, num+"콤보!" , Toast.LENGTH_SHORT).show();
                num++;
            }
        }
        checkGameOver(candyBoard);
    }

    // 캔디 스와이프
    public void swapCandies(int fromRow, int fromCol, int toRow, int toCol, ImageView[][] candyBoard) {
        int tempCandy = (int) candyBoard[fromRow][fromCol].getTag();
        candyBoard[fromRow][fromCol].setTag(candyBoard[toRow][toCol].getTag());
        candyBoard[toRow][toCol].setTag(tempCandy);

        candyBoard[fromRow][fromCol].setImageResource((int) candyBoard[fromRow][fromCol].getTag());
        candyBoard[toRow][toCol].setImageResource((int) candyBoard[toRow][toCol].getTag());
    }

    // 매칭된 캔디들 처리
    public boolean processMatches(ImageView[][] candyBoard) {
        matchedCandies = new ArrayList<>();
        boolean matchFound = false;

        // 가로, 세로 매칭 확인
        for (int row = 0; row < candyBoard.length; row++) {
            for (int col = 0; col < candyBoard[row].length; col++) {
                if (checkMatch(row, col, candyBoard)) {
                    matchedCandies.add(new int[]{row, col});
                    matchFound = true;
                }
            }
        }

        // 매칭된 캔디들 삭제하고 점수 업데이트
        for (int[] candyPos : matchedCandies) {
            int row = candyPos[0];
            int col = candyPos[1];
            candyBoard[row][col].setImageResource(0);
            candyBoard[row][col].setTag(0);

            // 캔디가 떨어지게 하기
            dropCandies(candyBoard);

            activity.updateScore(1);  // 점수 업데이트
        }

        return matchFound;
    }

    // 캔디 매칭 여부 확인 (가로, 세로)
    private boolean checkMatch(int row, int col, ImageView[][] candyBoard) {
        return (checkHorizontalMatch(row, col, candyBoard) || checkVerticalMatch(row, col, candyBoard));
    }

    // 가로 매칭 확인
    private boolean checkHorizontalMatch(int row, int col, ImageView[][] candyBoard) {
        int matchCount = 1;
        int candyType = (int) candyBoard[row][col].getTag();
        int left = col - 1;
        while (left >= 0 && (int) candyBoard[row][left].getTag() == candyType) {
            matchCount++;
            left--;
        }

        int right = col + 1;
        while (right < candyBoard[row].length && (int) candyBoard[row][right].getTag() == candyType) {
            matchCount++;
            right++;
        }

        return matchCount >= 3;
    }

    // 세로 매칭 확인
    private boolean checkVerticalMatch(int row, int col, ImageView[][] candyBoard) {
        int matchCount = 1;
        int candyType = (int) candyBoard[row][col].getTag();
        int up = row - 1;
        while (up >= 0 && (int) candyBoard[up][col].getTag() == candyType) {
            matchCount++;
            up--;
        }

        int down = row + 1;
        while (down < candyBoard.length && (int) candyBoard[down][col].getTag() == candyType) {
            matchCount++;
            down++;
        }

        return matchCount >= 3;
    }

    // 캔디 떨어지기 (빈칸 메꾸기)
    public void dropCandies(ImageView[][] candyBoard) {
        // 각 열에 대해 빈 칸을 채우는 로직
        for (int col = 0; col < candyBoard[0].length; col++) {
            // 열에서 아래로부터 위로 스캔하여 빈칸을 찾음
            for (int row = candyBoard.length - 1; row >= 0; row--) {
                // 빈 칸이면
                if ((int) candyBoard[row][col].getTag() == 0) { // 또는 getTag() == null
                    // 위에 있는 캔디를 아래로 내려오는 로직
                    for (int aboveRow = row - 1; aboveRow >= 0; aboveRow--) {
                        if ((int) candyBoard[aboveRow][col].getTag() != 0) { // 빈칸이 아닌 캔디를 찾음
                            // 위에 있는 캔디를 현재 빈칸으로 이동
                            swapCandies(aboveRow, col, row, col, candyBoard);
                            break; // 한 번만 내려오므로 break
                        }
                    }

                    // 만약 위에서 내려오는 캔디가 없다면, 새로운 캔디를 생성
                    if ((int) candyBoard[0][col].getTag() == 0) {
                        int randomCandyResource = activity.getRandomCandyResource();
                        candyBoard[0][col].setImageResource(randomCandyResource);
                        candyBoard[0][col].setTag(randomCandyResource);
                    }
                }
            }
        }
    }
    public void checkGameOver(ImageView[][] candyBoard) {
        int rows = candyBoard.length;
        int cols = candyBoard[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // 오른쪽으로 스와이프 가능 여부 확인
                if (col + 1 < cols) {
                    if (canSwapAndMatch(row, col, row, col + 1, candyBoard)) {
                        return; // 변화 가능하면 메서드 종료
                    }
                }
                // 아래로 스와이프 가능 여부 확인
                if (row + 1 < rows) {
                    if (canSwapAndMatch(row, col, row + 1, col, candyBoard)) {
                        return; // 변화 가능하면 메서드 종료
                    }
                }
            }
        }

        Toast.makeText(activity, "더 이상 움직일 수 없습니다!", Toast.LENGTH_SHORT).show();

        // 모든 스와이프 가능성을 확인한 후에도 변화가 없으면 게임 종료
        activity.endGame(2);
    }

    // 특정 스와이프가 매칭을 만드는지 확인하는 헬퍼 메서드
    private boolean canSwapAndMatch(int fromRow, int fromCol, int toRow, int toCol, ImageView[][] candyBoard) {
        // 보드 복사본 생성
        ImageView[][] boardCopy = copyCandyBoard(candyBoard);

        // 스와이프 시뮬레이션
        swapCandies(fromRow, fromCol, toRow, toCol, boardCopy);

        // 매칭 여부 확인
        return checkMatch(fromRow, fromCol, boardCopy) || checkMatch(toRow, toCol, boardCopy);
    }


    // 보드 복사 메서드
    private ImageView[][] copyCandyBoard(ImageView[][] originalBoard) {
        int rows = originalBoard.length;
        int cols = originalBoard[0].length;

        ImageView[][] copy = new ImageView[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                copy[row][col] = new ImageView(originalBoard[row][col].getContext());
                copy[row][col].setTag(originalBoard[row][col].getTag());
            }
        }
        return copy;
    }



}

