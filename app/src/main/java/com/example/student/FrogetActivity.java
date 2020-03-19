package com.example.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.database.LoginHelper;

public class FrogetActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText PwdFst;
    private EditText PwdSec;
    private EditText Vercode;//输入的验证码
    private String StuNum;
    private String VerCode;//获取的验证码

    private LoginHelper mHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_froget);
        PwdFst=findViewById(R.id.et_password_first);
        PwdSec=findViewById(R.id.et_password_second);
        Vercode=findViewById(R.id.verifycode);
        findViewById(R.id.regedit).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.Bt_verifycode).setOnClickListener(this);
        StuNum=getIntent().getStringExtra("学号");
    }

    @Override
    public void onClick(View v) {
        mHelper=new LoginHelper(FrogetActivity.this,"user.db",null,1);
        sqLiteDatabase=mHelper.getWritableDatabase();
        if(v.getId()==R.id.Bt_verifycode){
            //获取验证码
            VerCode=String.format("%06d", (int) (Math.random() * 1000000 % 1000000));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请记住验证码");
            builder.setMessage("本次验证码是" + VerCode + "，请输入验证码");
            builder.setPositiveButton("好的", null);
            AlertDialog alert = builder.create();
            alert.show();
        }else if(v.getId()==R.id.regedit){
            String pwdfst=PwdFst.getText().toString();
            String pwdsec=PwdSec.getText().toString();
            if(pwdfst.equals("")){
                Toast.makeText(this,"密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(!pwdfst.equals(pwdsec)){
                Toast.makeText(this,"两次密码不一致",Toast.LENGTH_LONG).show();
            }
            if(!Vercode.getText().toString().equals(VerCode)){
                Toast.makeText(this,"验证码错误",Toast.LENGTH_LONG).show();
            }else {
                int Result =mHelper.Update(sqLiteDatabase,StuNum,pwdfst);
                if(Result>0){
                Toast.makeText(this,"密码修改成功",Toast.LENGTH_LONG).show();
                }
            }

        }
        else if(v.getId()==R.id.cancel){
            FrogetActivity.this.finish();
        }
        mHelper.close();

    }
}
