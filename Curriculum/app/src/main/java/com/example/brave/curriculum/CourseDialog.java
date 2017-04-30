package com.example.brave.curriculum;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


/**
 * Created by 张潮 on 2017/4/17.
 */

public class CourseDialog extends Dialog {
    private TextView courseInfo;
    private String text;
    public CourseDialog(Context context) {
        super(context);
    }
    public CourseDialog(Context context, int theme){
        super(context, theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_layout);
        setCanceledOnTouchOutside(true);
        initView();
        initData();

    }
    private void initData(){
        if(text!=null) {
            courseInfo.setText(text);
        }
    }
    private void initView(){
        courseInfo = (TextView) findViewById(R.id.course_info);
    }
    public void setCourseInfo(String courseinfo){
        text = courseinfo;
    }
}
