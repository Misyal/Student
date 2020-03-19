package com.example.student.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.student.Info.StudentInfo;
import com.example.student.MainActivity;
import com.example.student.MeSetting.MyCourse;
import com.example.student.MeSetting.MyMessage;
import com.example.student.MeSetting.MyRecord;
import com.example.student.R;
import com.example.student.database.MessageHelper;


public class SettingFragment extends Fragment implements View.OnClickListener {
    private Button quit;
    private TextView sn,sname,ssex,sclass,scollege,sprofession;
    String stunum;
    private MessageHelper messageHelper;
    private SQLiteDatabase sqLiteDatabase;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);
        quit=view.findViewById(R.id.quit);
        quit.setOnClickListener(this);
        view.findViewById(R.id.mycourse).setOnClickListener(this);
        view.findViewById(R.id.myrecord).setOnClickListener(this);
        view.findViewById(R.id.mymessage).setOnClickListener(this);

        sname=view.findViewById(R.id.EName);
        ssex=view.findViewById(R.id.ESex);
        sclass=view.findViewById(R.id.EClass);
        scollege=view.findViewById(R.id.ECollege);
        sprofession=view.findViewById(R.id.EProfession);

        stunum=getArguments().getString("学号");

        sn=view.findViewById(R.id.SN);
        sn.setText(stunum);

        messageHelper=new MessageHelper(getActivity(),"user.db",null,1);
        sqLiteDatabase=messageHelper.getWritableDatabase();
        StudentInfo info1=messageHelper.Mqurey(sqLiteDatabase,stunum);
        sname.setText(info1.StudentName);
        ssex.setText(info1.StudentSex);
        sclass.setText(info1.StudentClass);
        scollege.setText(info1.StudentCollege);
        sprofession.setText(info1.StudentProfession);

        return view;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.myrecord){
            Intent intent=new Intent(getActivity(), MyRecord.class);
            intent.putExtra("学号",stunum);
            startActivity(intent);

        }else {
            if(v.getId()==R.id.mymessage){
                Intent intent =new Intent(getActivity(), MyMessage.class);
                intent.putExtra("学号",stunum);
                startActivityForResult(intent,0);
            }else {
                if(v.getId()==R.id.mycourse){
                    Intent intent=new Intent(getActivity(), MyCourse.class);
                    intent.putExtra("学号",stunum);
                    startActivity(intent);
                }else {
                    if(v.getId()==R.id.quit){
                        Intent intent =new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }
        }
    }

}
