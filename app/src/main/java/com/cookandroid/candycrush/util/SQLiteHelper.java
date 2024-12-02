package com.cookandroid.candycrush.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "score.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "score_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SCORE = "score";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SCORE + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 점수 추가 메서드
    public void addScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, score);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // 상위 점수 조회 메서드
    public List<Integer> getTopScores(int limit) {
        List<Integer> topScores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SCORE + " FROM " + TABLE_NAME +
                " ORDER BY " + COLUMN_SCORE + " DESC LIMIT " + limit;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                topScores.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return topScores;
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        // 테이블의 모든 데이터를 삭제
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}
