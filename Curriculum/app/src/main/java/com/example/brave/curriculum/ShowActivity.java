package com.example.brave.curriculum;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brave.curriculum.DBUtil.DBManager;

public class ShowActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "ShowActivity";

    private LinearLayout weektitle;
    private LinearLayout class_info;
    private GridViewAdapter gridviewAdapter;
    private GridView courseDetail;
    private TextView text_campus;
    private TextView text_others;
    private LinearLayout activity_show;

    private String[][] contents;
    private String[][] courseArr;
    private String tb_info;
    private Classtable ct;

    private Context context;
    //设置向右滑动返回
    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 250;
    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 250;
    //记录手指按下时的横坐标。
    private float xDown;
    //记录手指移动时的横坐标。
    private float xMove;
    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        context = ShowActivity.this;
        courseDetail = (GridView)findViewById(R.id.courseDetail);
        text_campus = (TextView) findViewById(R.id.text_campus);
        text_others = (TextView) findViewById(R.id.text_others);
        activity_show = (LinearLayout) findViewById(R.id.activity_show);
        weektitle = (LinearLayout) findViewById(R.id.weektitle);
        class_info = (LinearLayout) findViewById(R.id.class_info);

        weektitle.getBackground().setAlpha(50);
        class_info.getBackground().setAlpha(50);

        activity_show.setOnTouchListener(this);

        courseArr = new String[6][7];
        for(int row=0; row<6; row++){
            for(int column=0; column<7; column++){
                courseArr[row][column] = "";
            }
        }

        String params = getIntent().getStringExtra("params");
        Log.d(TAG, "onCreate: 回传参数"+params);
        DBManager dh = new DBManager(context,2);
        ct = dh.selectFromDB(params);
        if(ct != null){
            tb_info = ct.getInfo();
            Log.d(TAG, tb_info);
            if(!tb_info.equals("myxgkcb")){
                courseArr = ct.getClasstable();
                //表头信息处理
                int start = tb_info.indexOf(";") + 1;
                String strtmp = tb_info.substring(start);
                Log.i(TAG, strtmp);
                String[] text = strtmp.split("- - ");
                text_campus.setText(text[0]);
                for (int i = 1; i < text.length; i++) {
                    text_others.append(text[i] + "  ");
                }
            }
        }

        //创建Adapter
        fillStringArray();
        gridviewAdapter = new GridViewAdapter(context);
        gridviewAdapter.setContent(contents, 6, 7);
        courseDetail.setAdapter(gridviewAdapter);
    }

    public void fillStringArray() {
        contents = new String[6][7];
        for(int row=0; row<6; row++){
            for(int column=0; column<7; column++){
                contents[row][column] = "";
            }
        }
        if(ct != null){
            for(int row=0; row<6; row++){
                for(int column=0; column<7; column++){
                    contents[row][column] = courseArr[row][column];
                    //Log.i(TAG, row + "行" + column + "列:" + contents[row][column]);
                }
            }
        }
    }

    public void back(View view) {
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                //活动的距离
                int distanceX = (int) (xMove - xDown);
                //获取顺时速度
                int xSpeed = getScrollVelocity();
                //当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                if(distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                    finish();
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }
    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }
    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }
}
