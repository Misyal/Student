package com.example.student;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.database.LoginHelper;

public class RegeditActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginHelper mHelper;
    private EditText StuNum;
    private EditText StuPwd;
    private EditText Pwd;
    private SQLiteDatabase sqliteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regedit);
        StuNum=findViewById(R.id.stunum);
        StuPwd=findViewById(R.id.stupwd);
        Pwd=findViewById(R.id.pwd);
        findViewById(R.id.regedit).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        mHelper=new LoginHelper(RegeditActivity.this,"user.db",null,1);
        sqliteDatabase=mHelper.getWritableDatabase();
        String stunum=StuNum.getText().toString();
        String stupwd=StuPwd.getText().toString();
        String pwd=Pwd.getText().toString();
        if(v.getId()==R.id.regedit){
            if(stunum.length()!=9){
                Toast.makeText(this,"请输入9位学号",Toast.LENGTH_LONG).show();
                return;
            }else if(!stupwd.equals(pwd)){
                Toast.makeText(this,"两次密码不一致",Toast.LENGTH_LONG).show();
            }else {
                int Result=mHelper.qureynum(sqliteDatabase,stunum);
                if(Result == 1){
                    Toast.makeText(this,"该账号已注册",Toast.LENGTH_LONG).show();
                }
                else {
                    mHelper.Insert(sqliteDatabase,stunum,stupwd);
                    Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
                }
            }
        }
        if(v.getId()==R.id.cancel)
        {
            RegeditActivity.this.finish();
        }
        mHelper.close();

    }
}
