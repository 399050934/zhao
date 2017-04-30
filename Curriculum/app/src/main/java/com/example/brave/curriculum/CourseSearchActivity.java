package com.example.brave.curriculum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.brave.curriculum.DBUtil.DBManager;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CourseSearchActivity extends HideKeyboard implements View.OnTouchListener {

    private static final String TAG = "CourseSearchActivity";
    private Bitmap bitmap;
    private Button course_search;
    private EditText identify_code;
    private EditText et_course;
    private ImageView validateImg;
    private LinearLayout linear_course;
    private LinearLayout activity_search;

    private Spinner semesterSpinner;
    private Spinner courseSpinner;
    private List<String> semesterList = new ArrayList<String>();
    private List<String> courseList = new ArrayList<String>();
    private ArrayAdapter<String> semesterAdapter;
    private ArrayAdapter<String> courseAdapter;

    private static Context context;

    String cookie = "";
    private static String code = null;
    private static HashMap<String, String> map_semester = null;
    private static HashMap<String, String> map_course = null;

    private static Set<String> keys = null;
    private static Iterator<HashMap.Entry<String, String>> it = null;

    private static String semester_value = null;
    private static String course_value = null;

    JsoupUtil jsoupUtil = new JsoupUtil();
    private DBManager dh;

    private static String params;
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

    private static StringBuilder sb;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);
        course_search = (Button)findViewById(R.id.course_search);
        validateImg = (ImageView) findViewById(R.id.validateImg_course);
        identify_code = (EditText) findViewById(R.id.identify_code_c);
        linear_course = (LinearLayout) findViewById(R.id.linear_course);
        et_course = (EditText) findViewById(R.id.et_course);
        activity_search = (LinearLayout) findViewById(R.id.activity_search);

        activity_search.setOnTouchListener(this);

        context = CourseSearchActivity.this;

        dh = new DBManager(context, 2);

        //获取cookie值
        Bundle bundle = this.getIntent().getExtras();
        cookie = bundle.getString("cookie");
        //Spinner设置
        semesterSpinner = (Spinner)findViewById(R.id.semester);
        courseSpinner = (Spinner)findViewById(R.id.course_name);

        semesterSpinner.setVisibility(View.VISIBLE);
        //获取HashMap
        map_semester = Info.getInfoSemester(context);
        //放入数据
        keys = map_semester.keySet();
        it = map_semester.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            HashMap.Entry<String, String> entry = it.next();
            semesterList.add(i, entry.getKey());
            i++;
        }
        i = 0;

        courseSpinner.setVisibility(View.VISIBLE);

        //添加Adapter
        semesterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semesterList);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(semesterAdapter);
        courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseList);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);
        //添加监听
        semesterSpinner.setOnItemSelectedListener(new SemesterSelectedListener());
        courseSpinner.setOnItemSelectedListener(new CourseSelectedListener());

        et_course.addTextChangedListener(textWatcher);

        course_search.getBackground().setAlpha(50);

        sb = new StringBuilder();

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(map_course != null){
                courseList.clear();
                map_course = Info.getInfoCourse(context);
                keys = map_course.keySet();
                it = map_course.entrySet().iterator();
                String et = et_course.getText().toString();
                if(!et.equals("")){
                    int i = 0;
                    while (it.hasNext()) {
                        HashMap.Entry<String, String> entry = it.next();
                        String course = entry.getKey();
                        if(course.contains(et)){
                            courseList.add(i, course);
                            i++;
                        }
                    }

                    CourseSearchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            courseAdapter.notifyDataSetChanged();
                        }
                    });
                    //自动触发查询事件
                    if(courseSpinner.getSelectedItem() != null){
                        String spinner_key = courseSpinner.getSelectedItem().toString();
                        Log.i(TAG, spinner_key);
                        String spinner_value = "";
                        it = map_course.entrySet().iterator();
                        while (it.hasNext()) {
                            HashMap.Entry<String, String> entry = it.next();
                            String course = entry.getKey();
                            if(course.equals(spinner_key)){
                                spinner_value = entry.getValue();
                            }
                        }
                        Log.i(TAG, spinner_value);
                        params = semester_value + spinner_value;
                        Log.i(TAG, params);
                        new Thread(){
                            HttpURLConnection con = null;
                            BufferedReader reader = null;
                            InputStream in = null;
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("http://115.159.106.126:8080/SearchCourse/query/" + params);
                                    //Log.i(TAG, url.toString());
                                    con = (HttpURLConnection) url.openConnection();
                                    con.setDoOutput(true);

                                    in = con.getInputStream();
                                    reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
                                    sb.setLength(0);
                                    String line = null;
                                    while((line = reader.readLine()) != null){
                                        sb.append(line);
                                    }
                                    Log.i(TAG, sb.toString());
                                    if(sb.toString().equals(" ")){
                                        CourseSearchActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                linear_course.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }else{
                                        CourseSearchActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                linear_course.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }else {
                    int i = 0;
                    while (it.hasNext()) {
                        HashMap.Entry<String, String> entry = it.next();
                        String course = entry.getKey();
                        courseList.add(i, course);
                        i++;
                    }
                    CourseSearchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            courseAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }
    };

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

    class SemesterSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String str = semesterAdapter.getItem(position);
            semester_value = map_semester.get(str);
            if(map_course != null){
                map_course.clear();
            }
            courseList.clear();
            if(isOpenNetwork() == true){
                new Thread(){
                    HttpURLConnection con = null;
                    BufferedReader reader = null;
                    InputStream in = null;
                    Document doc = null;
                    String code = "";
                    @Override
                    public void run() {
                        try {
                            sleep(100);
                            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/Private/List_XNXQKC.aspx?xnxq=" + semester_value);
                            con = (HttpURLConnection) url.openConnection();
                            con.setRequestProperty("Referer", "http://xg.zdsoft.com.cn/ZNPK/KBFB_LessonSel.aspx");
                            //发送cookie
                            con.addRequestProperty("Cookie", cookie);
                            in = con.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
                            sb.setLength(0);
                            String line = null;
                            while((line = reader.readLine()) != null){
                                sb.append(line);
                            }
                            //Log.i("brave", sb.toString());
                            Elements option = JSONParser.courseParser(sb.toString());
                            Info.saveInfoCourse(context, option);
                            map_course = Info.getInfoCourse(context);
                            keys = map_course.keySet();
                            it = map_course.entrySet().iterator();
                            int i = 0;
                            while (it.hasNext()) {
                                HashMap.Entry<String, String> entry = it.next();
                                courseList.add(i, entry.getKey());
                                i++;
                            }
                            CourseSearchActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    courseAdapter.notifyDataSetChanged();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally{
                            try {
                                in.close();
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            con.disconnect();
                        }
                    }
                }.start();
            }else{
                Toast.makeText(CourseSearchActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class CourseSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String str = courseAdapter.getItem(position);
            course_value = map_course.get(str);
            params = semester_value + course_value;

            if((dh.selectFromDB(params)) != null){
                //隐藏验证码
                linear_course.setVisibility(View.INVISIBLE);
            }else{
                new Thread(){
                    HttpURLConnection con = null;
                    BufferedReader reader = null;
                    InputStream in = null;
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://115.159.106.126:8080/SearchCourse/query/" + params);
                            //Log.i(TAG, url.toString());
                            con = (HttpURLConnection) url.openConnection();
                            con.setDoOutput(true);

                            in = con.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
                            sb.setLength(0);
                            String line = null;
                            while((line = reader.readLine()) != null){
                                sb.append(line);
                            }
                            Log.i(TAG, sb.toString());
                            if(sb.toString().equals(" ")){
                                CourseSearchActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        linear_course.setVisibility(View.VISIBLE);
                                    }
                                });
                            }else{
                                CourseSearchActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        linear_course.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();


            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void courseSearchResult(View view) {
        if(isOpenNetwork() == true){
            Log.i(TAG, "判断之前：" + params);
            if((dh.selectFromDB(params)) != null){
                Intent intent = new Intent(CourseSearchActivity.this, ShowActivity.class);
                intent.putExtra("params", params);
                Log.i(TAG, "数据库不为空之后: " + params);
                startActivity(intent);
            }else if(!sb.toString().equals(" ")){
                Classtable ct = gson.fromJson(sb.toString(), new TypeToken<Classtable>(){}.getType());
                //Log.i(TAG, ct.getParameter());
                dh = new DBManager(context, 2);
                dh.addType1(ct);
                Intent intent = new Intent(CourseSearchActivity.this, ShowActivity.class);
                intent.putExtra("params", params);
                Log.d(TAG, "run: 传参数为"+params);
                startActivity(intent);
            }else{
                new Thread(){
                    HttpURLConnection con = null;
                    BufferedReader reader = null;
                    InputStream in = null;
                    @Override
                    public void run() {
                        try {
                            //form_data
                            code = identify_code.getText().toString();
                            String formData = "Sel_XNXQ=" + semester_value + "&Sel_KC=" + course_value + "&gs=1&txt_yzm=" + code;
                            URL url = new URL("http://115.159.106.126:8080/SearchCourse/queryNet/" + params + "/" + formData);
                            //Log.i(TAG, url.toString());
                            con = (HttpURLConnection) url.openConnection();
                            con.setDoOutput(true);

                            in = con.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
                            StringBuilder sb = new StringBuilder();
                            sb.setLength(0);
                            String line = null;
                            while((line = reader.readLine()) != null){
                                sb.append(line);
                            }
                            //Log.i("1111111111", sb.toString());
                            if(JSONParser.searchResultParser(sb.toString())){
                                String html = sb.toString();
                                params = semester_value + course_value;
                                Classtable ct = gson.fromJson(sb.toString(), new TypeToken<Classtable>(){}.getType());
                                Log.i(TAG, ct.getParameter());
                                dh = new DBManager(context, 2);
                                dh.addType1(ct);
                                Intent intent = new Intent(CourseSearchActivity.this, ShowActivity.class);
                                intent.putExtra("params", params);
                                Log.d(TAG, "run: 传参数为"+params);
                                startActivity(intent);
                            }else{
                                CourseSearchActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CourseSearchActivity.this, "验证码错误！", Toast.LENGTH_LONG).show();
                                    }
                                });
                                //自动更换验证码
                                getValidate();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }else{
            Toast.makeText(CourseSearchActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
        }
    }

    public void imageValidate(View view) {
        getValidate();
    }

    public void getValidate(){
        new Thread(){
            @Override
            public void run() {
                bitmap = Validate.getValidate();
            }
        }.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        validateImg.setImageBitmap(bitmap);
    }

    private boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    public void back(View view) {
        finish();
    }
}
