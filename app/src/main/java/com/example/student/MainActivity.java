package com.example.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.database.LoginHelper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button Login;
    private Button Regedit;
    private Button Froget;
    private EditText StuNum;
    private EditText StuPwd;
    private CheckBox Remeber;

    private SQLiteDatabase sqliteDatabase;
    private LoginHelper mHelper;

    private boolean bRemember = true; // 是否记住密码
    private SharedPreferences mShare;// 声明一个共享参数对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StuNum =findViewById(R.id.stunum);
        StuPwd =findViewById(R.id.stupwd);
        Login =findViewById(R.id.login);
        Froget =findViewById(R.id.froget);
        Regedit= findViewById(R.id.regedit);
        Remeber =findViewById(R.id.remember);
        Remeber.setOnCheckedChangeListener(new CheckListener());
        Froget.setOnClickListener(this);
        Regedit.setOnClickListener(this);
        Login.setOnClickListener(this);


        mShare=getSharedPreferences("share",MODE_PRIVATE);
        String stunum=mShare.getString("学号","");
        String stupwd=mShare.getString("密码","");
        StuNum.setText(stunum);
        StuPwd.setText(stupwd);

    }

    private class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.remember) {
                bRemember = isChecked;
            }
        }
    }


    @Override
    public void onClick(View v) {
        mHelper=new LoginHelper(MainActivity.this,"user.db",null,1);
        sqliteDatabase=mHelper.getWritableDatabase();
        String stunum = StuNum.getText().toString();
        String stupwd = StuPwd.getText().toString();
        if(v.getId()==R.id.froget){   //点击忘记密码按钮
            if(stunum.length()!=9){
                Toast.makeText(this,"请输入9位学号",Toast.LENGTH_LONG).show();
                return;
            }
            else
            {   //带着学号跳转到忘记密码页面
                Intent intent = new Intent(this,FrogetActivity.class);
                intent.putExtra("学号",stunum);
                startActivity(intent);
            }
        }
        else if(v.getId()==R.id.regedit){  //点击注册按钮
            Intent intent =new Intent(this,RegeditActivity.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.login){  //点击登录按钮
            if(stunum.equals("")||stupwd.equals("")){
                Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            }
            else {
                int Result=mHelper.qureypwd(sqliteDatabase,stunum,stupwd);
                if( Result==-1){
                    Toast.makeText(this,"密码错误",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent=new Intent(this,TabActivity.class);
                    intent.putExtra("学号",stunum);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }

            if(bRemember){
                SharedPreferences.Editor editor=mShare.edit();
                editor.putString("学号",stunum);
                editor.putString("密码",stupwd);
                editor.commit();//提交内容
            }

        }
        mHelper.close();

    }

    protected void onRestart() {
        StuPwd.setText("");
        super.onRestart();
    }

}

