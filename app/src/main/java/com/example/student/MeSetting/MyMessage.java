package com.example.student.MeSetting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.student.Face.FaceRegActivity;
import com.example.student.Info.StudentInfo;
import com.example.student.R;
import com.example.student.database.MessageHelper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MyMessage extends AppCompatActivity implements View.OnClickListener {

    private TextView tstunum;
    private String StuNum;
    private TextView ename;
    private TextView eclass,eprofession,ecollege,tvface;
    private Spinner esex;

    private ArrayAdapter<String> adapter;
    private static final String[] m={"男","女"};


    private MessageHelper messageHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);

        StuNum=getIntent().getStringExtra("学号");
        tstunum=findViewById(R.id.Tstunum);
        tstunum.setText(StuNum);

        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.CANNEL).setOnClickListener(this);
        findViewById(R.id.regface).setOnClickListener(this);

        tvface=findViewById(R.id.tvface);
        ename=findViewById(R.id.eName);
        eclass=findViewById(R.id.eclass);
        ecollege=findViewById(R.id.ecollege);

        esex=findViewById(R.id.esex);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
        esex.setAdapter(adapter);

        eprofession=findViewById(R.id.eprofession);
    }

    @Override
    public void onClick(View v) {
      messageHelper=new MessageHelper(this,"user.db",null,1);
      sqLiteDatabase=messageHelper.getWritableDatabase();

        if(v.getId()==R.id.btn_add) {
            StudentInfo info = new StudentInfo();
            info.StudentNum = StuNum;
            info.StudentSex = esex.getSelectedItem().toString();
            if(ename.getText().toString().isEmpty()){
                Toast.makeText(this,"姓名不能为空",Toast.LENGTH_LONG).show();
            }else {
                info.StudentName = ename.getText().toString();
                if(eclass.getText().toString().isEmpty()) {
                    Toast.makeText(this, "班级不能为空", Toast.LENGTH_LONG).show();
                }else {
                    info.StudentClass = eclass.getText().toString();
                    if(ecollege.getText().toString().isEmpty()) {
                        Toast.makeText(this, "学院不能为空", Toast.LENGTH_LONG).show();
                    }else {
                        info.StudentCollege = ecollege.getText().toString();
                        if(eprofession.getText().toString().isEmpty()) {
                            Toast.makeText(this, "专业不能为空", Toast.LENGTH_LONG).show();
                        }else {
                            info.StudentProfession = eprofession.getText().toString();
                            long  Result=messageHelper.Minsert(sqLiteDatabase,info);
                            if(Result>0){
                                Toast.makeText(this,"成功",Toast.LENGTH_LONG).show();
                                Intent intent=getIntent();
                                Bundle bundle=new Bundle();
                                bundle.putString("StuName",ename.getText().toString());
                                bundle.putString("StuSex",esex.getSelectedItem().toString());
                                bundle.putString("StuClass",eclass.getText().toString());
                                bundle.putString("StuCollege",ecollege.getText().toString());
                                bundle.putString("StuPro",eprofession.getText().toString());
                                intent.putExtras(bundle);
                                setResult(1,intent);
                                this.finish();
                            }else {
                                Toast.makeText(this,"失败",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }

        }else {
            if(v.getId()==R.id.regface){
                Intent intent=new Intent(MyMessage.this, FaceRegActivity.class);
                intent.putExtra("学号",StuNum);
                startActivityForResult(intent,1);
            }
        else {
            if (v.getId()==R.id.CANNEL){
                this.finish();
            }
        }
        }
        messageHelper.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==2){

        }
    }
}
