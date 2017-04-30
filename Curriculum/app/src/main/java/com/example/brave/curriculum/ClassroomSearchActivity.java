package com.example.brave.curriculum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClassroomSearchActivity extends HideKeyboard implements View.OnTouchListener {
    private static final String TAG = "ClassroomSearchActivity";

    private Bitmap bitmap;
    private Button classroom_search;
    private EditText identify_code;
    private ImageView validateImg;
    private LinearLayout linear_room;
    private LinearLayout activity_classroom_search;

    private static String cookie = "";
    private static HashMap<String, String> map_semester = null;
    private static HashMap<String, String> map_compus = null;
    private static HashMap<String, String> map_building = null;
    private static HashMap<String, String> map_room = null;

    private static Set<String> keys = null;
    private static Iterator<HashMap.Entry<String, String>> it = null;

    private static Context context;

    private Spinner semesterSpinner;
    private Spinner campusSpinner;
    private Spinner buildingSpinner;
    private Spinner classroomSpinner;
    private List<String> semesterList = new ArrayList<String>();
    private List<String> campusList = new ArrayList<String>();
    private List<String> buildingList = new ArrayList<String>();
    private List<String> classroomList = new ArrayList<String>();
    private ArrayAdapter<String> semesterAdapter;
    private ArrayAdapter<String> campusAdapter;
    private ArrayAdapter<String> buildingAdapter;
    private ArrayAdapter<String> classroomAdapter;

    private static String code = null;
    private static String semester_value = null;
    private static String campus_value = null;
    private static String building_value = null;
    private static String room_value = null;

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
        setContentView(R.layout.activity_classroom_search);
        classroom_search = (Button)findViewById(R.id.classroom_search);
        identify_code = (EditText) findViewById(R.id.identify_code_r);
        validateImg = (ImageView) findViewById(R.id.validateImg_room);
        linear_room = (LinearLayout) findViewById(R.id.linear_room);
        activity_classroom_search = (LinearLayout) findViewById(R.id.activity_classroom_search);

        activity_classroom_search.setOnTouchListener(this);

        context = ClassroomSearchActivity.this;

        dh = new DBManager(context, 2);

        semesterSpinner = (Spinner)findViewById(R.id.semester);
        campusSpinner = (Spinner)findViewById(R.id.campus);
        buildingSpinner = (Spinner)findViewById(R.id.building);
        classroomSpinner = (Spinner)findViewById(R.id.classroom_name);
        //获取cookie值
        Bundle bundle = this.getIntent().getExtras();
        cookie = bundle.getString("cookie");

        //Spinner设置
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

        campusSpinner.setVisibility(View.VISIBLE);
        map_compus = Info.getInfoCompus(context);
        keys = map_compus.keySet();
        it = map_compus.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<String, String> entry = it.next();
            campusList.add(i, entry.getKey());
            i++;
        }
        i = 0;

        buildingSpinner.setVisibility(View.VISIBLE);
        classroomSpinner.setVisibility(View.VISIBLE);

        //添加Adapter
        semesterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semesterList);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(semesterAdapter);

        campusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, campusList);
        campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campusSpinner.setAdapter(campusAdapter);

        buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildingList);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(buildingAdapter);

        classroomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classroomList);
        classroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroomSpinner.setAdapter(classroomAdapter);
        //添加监听
        semesterSpinner.setOnItemSelectedListener(new SemesterSelectedListener());
        campusSpinner.setOnItemSelectedListener(new CampusSelectedListener());
        buildingSpinner.setOnItemSelectedListener(new BuildingSelectedListener());
        classroomSpinner.setOnItemSelectedListener(new ClassroomSelectedListener());

        classroom_search.getBackground().setAlpha(50);

        sb = new StringBuilder();

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

    class SemesterSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String str = semesterAdapter.getItem(position);
            semester_value = map_semester.get(str);
            //Log.i("qqqqqqqq", semester_value);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    //校区选择事件
    class CampusSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String str = campusAdapter.getItem(position);
            campus_value = map_compus.get(str);
            if(map_building != null){
                map_building.clear();
            }
            buildingList.clear();
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
                            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/Private/List_JXL.aspx?w=150&id=" + campus_value);
                            con = (HttpURLConnection) url.openConnection();
                            con.setRequestProperty("Referer", "http://xg.zdsoft.com.cn/ZNPK/KBFB_RoomSel.aspx");
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
                            Elements option = JSONParser.buildingParser(sb.toString());
                            Info.saveInfoBuilding(context, option);
                            map_building = Info.getInfoBuilding(context);
                            keys = map_building.keySet();
                            it = map_building.entrySet().iterator();
                            int i = 0;
                            while (it.hasNext()) {
                                HashMap.Entry<String, String> entry = it.next();
                                buildingList.add(i, entry.getKey());
                                i++;
                            }
                            ClassroomSearchActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    buildingAdapter.notifyDataSetChanged();
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
                Toast.makeText(ClassroomSearchActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    //教学楼选择事件
    class BuildingSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String str = buildingAdapter.getItem(position);
            building_value = map_building.get(str);
            if(map_room != null){
                map_room.clear();
            }
            classroomList.clear();
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
                            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/Private/List_ROOM.aspx?w=150&id=" + building_value);
                            con = (HttpURLConnection) url.openConnection();
                            con.setRequestProperty("Referer", "http://xg.zdsoft.com.cn/ZNPK/KBFB_RoomSel.aspx");
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
                            Elements option = JSONParser.roomParser(sb.toString());
                            Info.saveInfoRoom(context, option);
                            map_room = Info.getInfoRoom(context);
                            keys = map_room.keySet();
                            it = map_room.entrySet().iterator();
                            int i = 0;
                            while (it.hasNext()) {
                                HashMap.Entry<String, String> entry = it.next();
                                classroomList.add(i, entry.getKey());
                                i++;
                            }
                            ClassroomSearchActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    classroomAdapter.notifyDataSetChanged();
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
                Toast.makeText(ClassroomSearchActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class ClassroomSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String str = classroomAdapter.getItem(position);
            room_value = map_room.get(str);
            //Log.i("qqqqqqqq", room_value);
            params = semester_value + campus_value + building_value + room_value;
            if((dh.selectFromDB(params)) != null){
                //隐藏验证码
                linear_room.setVisibility(View.INVISIBLE);
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
                                ClassroomSearchActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        linear_room.setVisibility(View.VISIBLE);
                                    }
                                });
                            }else{
                                ClassroomSearchActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        linear_room.setVisibility(View.INVISIBLE);
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

    public void classroomSearchResult(final View view) {
        if(isOpenNetwork() == true){
            params = semester_value + campus_value + building_value + room_value;
            if((dh.selectFromDB(params)) != null){
                Intent intent = new Intent(ClassroomSearchActivity.this, ShowActivity.class);
                intent.putExtra("params", params);
                Log.d(TAG, "run: 传参数为"+params);
                startActivity(intent);
            }else if(!sb.toString().equals(" ")){
                Classtable ct = gson.fromJson(sb.toString(), new TypeToken<Classtable>(){}.getType());
                //Log.i(TAG, ct.getParameter());
                dh = new DBManager(context, 2);
                dh.addType1(ct);
                Intent intent = new Intent(ClassroomSearchActivity.this, ShowActivity.class);
                intent.putExtra("params", params);
                Log.d(TAG, "run: 传参数为"+params);
                startActivity(intent);
            }else {
                new Thread(){
                    HttpURLConnection con = null;
                    BufferedReader reader = null;
                    InputStream in = null;
                    Document doc = null;
                    @Override
                    public void run() {
                        try {
                            //form_data
                            code = identify_code.getText().toString();
                            String formData = "Sel_XNXQ=" + semester_value + "&rad_gs=1&txt_yzm=" + code + "&Sel_XQ=" + campus_value + "&Sel_JXL=" + building_value + "&Sel_ROOM=" + room_value;
                            URL url = new URL("http://115.159.106.126:8080/SearchCourse/queryNetRoom/" + params + "/" + formData);
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
                                params = semester_value + campus_value + building_value + room_value;
                                Classtable ct = gson.fromJson(sb.toString(), new TypeToken<Classtable>(){}.getType());
                                Log.i(TAG, ct.getParameter());
                                dh = new DBManager(context, 2);
                                dh.addType1(ct);
                                Intent intent = new Intent(ClassroomSearchActivity.this, ShowActivity.class);
                                intent.putExtra("params", params);
                                Log.d(TAG, "run: 传参数为"+params);
                                startActivity(intent);
                            }else{
                                ClassroomSearchActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ClassroomSearchActivity.this, "验证码错误！", Toast.LENGTH_LONG).show();
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
            Toast.makeText(ClassroomSearchActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
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
