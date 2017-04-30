package com.example.brave.curriculum;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 张潮 on 2017/4/15.
 */

public class GridViewAdapter extends BaseAdapter {
    private static final String TAG = "GridViewAdapter";
    private Context ctx;
    private static String[][] contents;
    private static int rowTotal;
    private static int columnTotal;
    private static int positionTotal;
    private static int rand;

    private CourseDialog dialog;

    public GridViewAdapter(Context context) {
        this.ctx = context;
    }

    public int getCount() {
        return positionTotal;
    }

    public Object getItem(int position) {
        //列二维索引 余数
        int column = position % columnTotal;
        //行二维索引，商
        int row = position / columnTotal;
        return contents[row][column];
    }

    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View getView(final int position, View convertView, ViewGroup parent) {
        rand = 0;
        if(convertView==null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.grid_item, null);
        }

        final TextView textView = (TextView)convertView.findViewById(R.id.gridText);
        textView.setOnClickListener(new View.OnClickListener(){
            Boolean flag = true;
            @Override
            public void onClick(View v) {
                String detail = textView.getText().toString();
                dialog = new CourseDialog(ctx, R.style.dialog);
                dialog.setCourseInfo(detail);
                dialog.show();
                if(flag){
                    flag = false;
                    textView.setEllipsize(null); // 展开
                }else{
                    flag = true;
                    textView.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                }
            }
        });
        //若有课，则添加数据
        if(!(getItem(position).toString()).equals("")) {
            Log.i(TAG, getItem(position).toString());
            textView.setText((String)getItem(position));
            textView.setTextColor(Color.WHITE);
            //变换颜色
            rand = position % columnTotal;
            switch(rand) {
                case 0:
                    textView.setBackground(ctx.getResources().getDrawable(R.drawable.grid_item_bg));
                    break;
                case 1:
                    textView.setBackground(ctx.getResources().getDrawable(R.drawable.course_bg1));
                    break;
                case 2:
                    textView.setBackground(ctx.getResources().getDrawable(R.drawable.course_bg2));
                    break;
                case 3:
                    textView.setBackground(ctx.getResources().getDrawable(R.drawable.course_bg3));
                    break;
                case 4:
                    textView.setBackground(ctx.getResources().getDrawable(R.drawable.course_bg4));
                    break;
                case 5:
                    textView.setBackground(ctx.getResources().getDrawable(R.drawable.course_bg5));
                    break;
                case 6:
                    textView.setBackground(ctx.getResources().getDrawable(R.drawable.course_bg6));
                    break;
                case 7:
                    textView.setBackground(ctx.getResources().getDrawable(R.drawable.course_bg7));
                    break;
            }
        }else{
            textView.setTextColor(Color.WHITE);
        }
        return convertView;
    }
    //设置内容，行数，列数
    public  void setContent(String[][] contents, int row, int column) {
        this.contents = contents;
        this.rowTotal = row;
        this.columnTotal = column;
        positionTotal = rowTotal * columnTotal;
    }
}
