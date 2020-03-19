package com.example.student.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.student.Info.StudentInfo;


public class MessageHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "user.db"; // 数据库的名称


    public MessageHelper( Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context,DB_NAME, null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creat_sql2="CREATE TABLE IF NOT EXISTS StudentUser"
                +"(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                +"StudentNum VARCHAR  NOT NULL,"
                +"StudentName VARCHAR NOT NULL,"
                +"StudentSex VARCHAR NOT NULL,"
                +"StudentClass VARCHAR NOT NULL,"
                +"StudentCollege VARCHAR NOT NULL,"
                +"StudentProfession VARCHAR NOT NULL);";
        db.execSQL(creat_sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //插入个人信息
    public  long Minsert(SQLiteDatabase mMh,StudentInfo info){
        ContentValues cv=new ContentValues();
        cv.put("StudentNum",info.StudentNum);
        cv.put("StudentName",info.StudentName);
        cv.put("StudentSex",info.StudentSex);
        cv.put("StudentClass",info.StudentClass);
        cv.put("StudentCollege",info.StudentCollege);
        cv.put("StudentProfession",info.StudentProfession);

        Cursor cursor=mMh.query("StudentUser",new String[]{"StudentNum"},"StudentNum=?",new String[]{info.StudentNum},null,null,null);
        long Result = -1;
        if(cursor.getCount()==0){
            Result=mMh.insert("StudentUser",null,cv);
        }else {
            Result=mMh.update("StudentUser",cv,"StudentNum=?",new String[]{info.StudentNum});
        }
        return Result;

    }

    //查询信息是否存在，如果存在则从库中查询信息自动填写
    public StudentInfo Mqurey(SQLiteDatabase mMh, String StuNum){
       Cursor cursor=mMh.query("StudentUser",null,"StudentNum=?",new String[]{StuNum},null,null,null);
       StudentInfo info=new StudentInfo();
      while (cursor.moveToNext()){
       info.StudentName=cursor.getString(2);
       info.StudentSex=cursor.getString(3);
       info.StudentClass=cursor.getString(4);
       info.StudentCollege=cursor.getString(5);
       info.StudentProfession=cursor.getString(6);
      }
        return info;
    }


}
