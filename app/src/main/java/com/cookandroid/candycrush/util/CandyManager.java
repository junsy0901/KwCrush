package com.cookandroid.candycrush.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.cookandroid.candycrush.view.PlayActivity;

import java.util.List;

public class CandyManager {

    private PlayActivity playActivity;

    public CandyManager(PlayActivity playActivity) {
        this.playActivity = playActivity;
    }

    // 캔디 교환
    public void swapCandies(int firstIndex, int secondIndex, List<ImageView> candyList) {
        if (firstIndex < 0 || secondIndex < 0 || firstIndex >= candyList.size() || secondIndex >= candyList.size()) {
            return;
        }

        Drawable firstDrawable = candyList.get(firstIndex).getDrawable();
        Object firstTag = candyList.get(firstIndex).getTag();

        candyList.get(firstIndex).setImageDrawable(candyList.get(secondIndex).getDrawable());
        candyList.get(firstIndex).setTag(candyList.get(secondIndex).getTag());

        candyList.get(secondIndex).setImageDrawable(firstDrawable);
        candyList.get(secondIndex).setTag(firstTag);

        // 매칭 검사
        checkMatches(candyList);
    }

    // 매칭 검사
    private void checkMatches(List<ImageView> candyList) {
        checkRowForMatches(candyList);
        checkColumnForMatches(candyList);
    }

    private void checkRowForMatches(List<ImageView> candyList) {
        int gridSize = playActivity.getNoOfBlock();
        for (int i = 0; i < gridSize * gridSize; i++) {
            if (i % gridSize <= gridSize - 3) {
                int candy1 = (int) candyList.get(i).getTag();
                int candy2 = (int) candyList.get(i + 1).getTag();
                int candy3 = (int) candyList.get(i + 2).getTag();

                if (candy1 == candy2 && candy2 == candy3) {
                    removeCandy(candyList, i, i + 1, i + 2);
                }
            }
        }
    }

    private void checkColumnForMatches(List<ImageView> candyList) {
        int gridSize = playActivity.getNoOfBlock();
        for (int i = 0; i < gridSize * (gridSize - 2); i++) {
            int candy1 = (int) candyList.get(i).getTag();
            int candy2 = (int) candyList.get(i + gridSize).getTag();
            int candy3 = (int) candyList.get(i + 2 * gridSize).getTag();

            if (candy1 == candy2 && candy2 == candy3) {
                removeCandy(candyList, i, i + gridSize, i + 2 * gridSize);
            }
        }
    }

    private void removeCandy(List<ImageView> candyList, int... indices) {
        for (int index : indices) {
            candyList.get(index).setImageResource(android.R.color.transparent); // 캔디 제거
            candyList.get(index).setTag(null); // 태그 초기화
        }
        playActivity.updateScore(indices.length); // 점수 업데이트
    }


    // 캔디 채우기
    public void fillCandies(List<ImageView> candyList) {
        int gridSize = playActivity.getNoOfBlock();
        for (int i = 0; i < candyList.size(); i++) {
            if (candyList.get(i).getTag() == null) {
                // 빈 자리에 새 캔디를 채운다
                int randomCandy = playActivity.getRandomCandyResource();
                candyList.get(i).setImageResource(randomCandy);
                candyList.get(i).setTag(randomCandy);
            }
        }
    }
}
