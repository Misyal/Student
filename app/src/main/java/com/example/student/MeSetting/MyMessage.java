package com.example.student.MeSetting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.student.Info.StudentInfo;
import com.example.student.R;
import com.example.student.database.MessageHelper;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.student.Face.Face.runUpload;
import static com.example.student.utils.ImageUtil.getBitmapFormUri;


public class MyMessage extends AppCompatActivity implements View.OnClickListener {

    private TextView tstunum;
    private String StuNum;
    private TextView ename;
    private TextView eclass, eprofession, ecollege, tvface;
    private Spinner esex;

    private Uri imageUri;
    private Bitmap bitMap;

    private ArrayAdapter<String> adapter;
    private static final String[] m = {"男", "女"};


    private MessageHelper messageHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);

        StuNum = getIntent().getStringExtra("学号");
        tstunum = findViewById(R.id.Tstunum);
        tstunum.setText(StuNum);

        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.CANNEL).setOnClickListener(this);
        findViewById(R.id.regface).setOnClickListener(this);

        tvface = findViewById(R.id.tvface);
        ename = findViewById(R.id.eName);
        eclass = findViewById(R.id.eclass);
        ecollege = findViewById(R.id.ecollege);

        esex = findViewById(R.id.esex);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m);
        esex.setAdapter(adapter);

        eprofession = findViewById(R.id.eprofession);
    }

    @Override
    public void onClick(View v) {
        messageHelper = new MessageHelper(this, "user.db", null, 1);
        sqLiteDatabase = messageHelper.getWritableDatabase();

        if (v.getId() == R.id.btn_add) {
            StudentInfo info = new StudentInfo();
            info.StudentNum = StuNum;
            info.StudentSex = esex.getSelectedItem().toString();
            if (ename.getText().toString().isEmpty()) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_LONG).show();
            } else {
                info.StudentName = ename.getText().toString();
                if (eclass.getText().toString().isEmpty()) {
                    Toast.makeText(this, "班级不能为空", Toast.LENGTH_LONG).show();
                } else {
                    info.StudentClass = eclass.getText().toString();
                    if (ecollege.getText().toString().isEmpty()) {
                        Toast.makeText(this, "学院不能为空", Toast.LENGTH_LONG).show();
                    } else {
                        info.StudentCollege = ecollege.getText().toString();
                        if (eprofession.getText().toString().isEmpty()) {
                            Toast.makeText(this, "专业不能为空", Toast.LENGTH_LONG).show();
                        } else {
                            info.StudentProfession = eprofession.getText().toString();
                            if (imageUri == null) {
                                Toast.makeText(this, "无人脸信息", Toast.LENGTH_LONG).show();
                            } else {
                                //runUpload(StuNum, bitMap, handler);
                                //tvface.setText("人脸信息已保存");
                                long Result = messageHelper.Minsert(sqLiteDatabase, info);
                                if (Result > 0) {
                                    Toast.makeText(this, "成功", Toast.LENGTH_LONG).show();
                                    Intent intent = getIntent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("StuName", ename.getText().toString());
                                    bundle.putString("StuSex", esex.getSelectedItem().toString());
                                    bundle.putString("StuClass", eclass.getText().toString());
                                    bundle.putString("StuCollege", ecollege.getText().toString());
                                    bundle.putString("StuPro", eprofession.getText().toString());
                                    intent.putExtras(bundle);
                                    setResult(1, intent);
                                    this.finish();
                                } else {
                                    Toast.makeText(this, "失败", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            }

        } else {
            if (v.getId() == R.id.regface) {

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();            //7.0拍照必加

                //打开拍照
                Intent openFileIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                openFileIntent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

                //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_face";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.TITLE, imageFileName);
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                openFileIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openFileIntent, 3);


            } else {
                if (v.getId() == R.id.CANNEL) {
                    this.finish();
                }
            }
        }
        messageHelper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            try {
                // 源图 bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                // 需要进行图片压缩
                bitMap = getBitmapFormUri(imageUri, MyMessage.this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runUpload(StuNum, bitMap, handler);

            File file=new File(imageUri.getPath());
            getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{imageUri.getPath()});//删除系统缩略图
            file.delete();//删除SD中图片

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    Toast.makeText(MyMessage.this, "上传成功", Toast.LENGTH_SHORT).show();
                    tvface.setText("人脸信息已保存");
                    break;
                }
                // 百度的人脸识别错误码
                case 223105: {
                    Toast.makeText(MyMessage.this, "已经有你的图片了", Toast.LENGTH_SHORT).show();

                    break;
                }
                default: {
                    Toast.makeText(MyMessage.this, "上传失败", Toast.LENGTH_SHORT).show();
                    tvface.setText("人脸信息未保存");
                    break;
                }

            }
        }
    };

}


