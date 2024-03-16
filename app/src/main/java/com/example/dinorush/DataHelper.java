package com.example.dinorush;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper {
    private static final String DATABASE_NAME = "user_data.db";
    private static final String TABLE_NAME = "data";
    private static final int DATABASE_VERSION = 1;

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_RECORD = "record";
    private static final String COLUMN_COINS = "coins";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_RECORD = 1;
    private static final int NUM_COLUMN_COINS = 2;

    private SQLiteDatabase db;

    public DataHelper(Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
    }

    public int updateRecord(int record) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_RECORD, record);
        return db.update(TABLE_NAME, cv, null,null);
    }
    public int update(int record){
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_RECORD, record);
        return db.update(TABLE_NAME, cv, null,null);
    }
    public int updateCoins(int coins){
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_COINS, coins);
        return db.update(TABLE_NAME, cv, null,null);

    }
    public int selectCoins() {
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(NUM_COLUMN_COINS);
    }

    public int select() {
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(NUM_COLUMN_RECORD);
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECORD + " INTEGER,"+ COLUMN_COINS+" INTEGER); ";
            db.execSQL(query);
            query = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_RECORD + ") VALUES (0)";
            db.execSQL(query);
            query = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_COINS + ") VALUES (0)";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
