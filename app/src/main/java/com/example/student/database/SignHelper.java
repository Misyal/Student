package com.example.student.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SignHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db"; // 数据库的名称

    public SignHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void qureycourse(SQLiteDatabase mCu){

    }
}
