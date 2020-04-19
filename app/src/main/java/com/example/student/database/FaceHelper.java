package com.example.student.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

class Face {
    private Connection conn;
    private Statement sta;
    private ResultSet rs;

    public void FaceInsert(){
        try {
            conn=Connect.getConn();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
//public class FaceHelper extends SQLiteOpenHelper {
//
//
//    private static final String DB_NAME = "user.db"; // 数据库的名称
//
//    public FaceHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
//    {
//        super(context, DB_NAME, null, 1);
//    }
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String creatsql="CREATE TABLE IF NOT EXISTS face(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,StuNum VARCHAR NOT NULL,FaceExists VARCHAR NOT NULL)";
//        db.execSQL(creatsql);
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//    public void Faceinsert(SQLiteDatabase db,String StuNum){
//        String isql="INSERT INTO face VALUES('"+StuNum+"','是')";
//        db.execSQL(isql);
//
//    }
//}
