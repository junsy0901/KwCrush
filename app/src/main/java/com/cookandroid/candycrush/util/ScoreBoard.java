package com.cookandroid.candycrush.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {
    private SQLiteHelper dbHelper;

    public ScoreBoard(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void addScore(int score) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_SCORE, score);
        db.insert(SQLiteHelper.TABLE_NAME, null, values);
        db.close();
    }

    public List<Integer> getTopScores(int limit) {
        List<Integer> scores = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(SQLiteHelper.TABLE_NAME,
                new String[]{SQLiteHelper.COLUMN_SCORE},
                null, null, null, null,
                SQLiteHelper.COLUMN_SCORE + " DESC",
                String.valueOf(limit));

        if (cursor.moveToFirst()) {
            do {
                scores.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scores;
    }
}

