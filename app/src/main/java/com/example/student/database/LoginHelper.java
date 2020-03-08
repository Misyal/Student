package com.example.student.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LoginHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db"; // 数据库的名称
    private SQLiteDatabase mDB = null; // 数据库的实例

    public LoginHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(TAG,"OnCreate");
        //String drop_sql="DROP TABLE IF EXISTS"+TABLE_NAME+";";
        //Log.d(TAG,"drop_sql:"+drop_sql);
        //db.execSQL(drop_sql);
        String creat_sql= "CREATE TABLE IF NOT EXISTS Student(StudentNum VARCHAR PRIMARY KEY NOT NULL,StudentPassword VARCHAR NOT NULL);";
        //Log.d(TAG,"create_sql:"+creat_sql);
        db.execSQL(creat_sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //插入，用于注册
    public void Insert(SQLiteDatabase mDB,String StudentNum,String StudentPassword){
        ContentValues cv=new ContentValues();
        cv.put("StudentNum",StudentNum);
        cv.put("StudentPassword",StudentPassword);
        mDB.insert("Student",null,cv);
    }


    //查询学号，是否被注册
    public int qureynum(SQLiteDatabase mDB,String StudentNum){
        Cursor cursor=mDB.query("Student",new String[]{"StudentNum"},"StudentNum=?",new String[]{StudentNum},null,null,null);
        int result=-1;
        if (cursor.getCount()!=0){
            result=1;
            return result;
        }else {
            return result;
        }
    }

    //查询密码，用于登录验证
    public int qureypwd(SQLiteDatabase mDB,String StudentNum,String StudentPassword){
        Cursor course= mDB.query("Student",new String[]{"StudentPassword"},"StudentNum=?",new String[]{StudentNum},null,null,null);
        int result=-1;
        if(course.moveToNext()){
            String PW=course.getString(course.getColumnIndex("StudentPassword"));
            if(PW.equals(StudentPassword));
            result=1;
            return result;
        }else {
            return result;
        }
    }


    //用于更新密码
    public int Update(SQLiteDatabase mDB,String StudentNum,String StudentPassword){
       ContentValues cv=new ContentValues();
       cv.put("StudentPassword",StudentPassword);
       int result=mDB.update("Student",cv,"studentNum=?",new String[]{StudentNum});
       return result;
    }


/*

    public long  insert(UserInfo info){
        ContentValues cv=new ContentValues();
        cv.put("StudentNum",info.StudentNum);
        cv.put("StudentPassword",info.StudentPassword);
        long result=mDB.insert(TABLE_NAME,null,cv);
        //插入成功，返回插入行的ID
        return result;
    }


    public  int  update(UserInfo info ){
        String studentNum=info.StudentNum;
        ContentValues cv=new ContentValues();
        cv.put("StudentPassword",info.StudentPassword);
        int result=mDB.update("Student",cv,"studentNum=?",new String[]{studentNum});
        return result;
    }

    public int  qurey(UserInfo info){
        String password=info.StudentPassword;
        Cursor course= mDB.query("Student",new String[]{"StudentPassword"},"StudentNum=?",new String[]{info.StudentNum},null,null,null);
        int result=-1;
        if(course.getString(0)==password){
            return result=1;
        }else {
            return result;
        }
    }*/


}



