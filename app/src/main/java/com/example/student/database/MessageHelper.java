package com.example.student.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MessageHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "user.db"; // 数据库的名称
    private static final String mMh=null;


    public MessageHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context,DB_NAME , null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creat_sql="CREATE TABLE IF NOT EXISTS StudentUser"
                +"(StudentNum VARCHAR PRIMARY KEY NOT NULL,"
                +"StudentName VARCHAR NOT NULL,"
                +"StudentClass VARCHAR NOT NULL,"
                +"StudentSex VARCHAR NOT NULL,"
                +"StudentCollege VARCHAR NOT NULL,"
                +"StudentProfession VARCHAR NOT NULL"
                +");";
        db.execSQL(creat_sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
