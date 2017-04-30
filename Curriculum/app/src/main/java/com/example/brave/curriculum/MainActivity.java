package com.example.brave.curriculum;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button course_search_btn;
    private Button classroom_search_btn;
    private Button teacher_search_btn;
    private ImageView school_icon;

    private static Context context;

    private  String cookie = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        course_search_btn = (Button)findViewById(R.id.course_search_btn);
        classroom_search_btn = (Button)findViewById(R.id.classroom_searc_btn);
        classroom_search_btn = (Button)findViewById(R.id.teacher_search_btn);
        school_icon = (ImageView) findViewById(R.id.school_icon);
        context = MainActivity.this;

    }

    public void courseSearch(View view) {
        if(isOpenNetwork() == true){
            new Thread(){
                HttpURLConnection con = null;
                BufferedReader reader = null;
                InputStream in = null;
                Document doc = null;
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/KBFB_LessonSel.aspx");
                        con = (HttpURLConnection) url.openConnection();
                        in = con.getInputStream();
                        //抓取cookie
                        String cookie_temp = con.getHeaderField("Set-Cookie");
                        cookie = cookie_temp.split(";")[0];

                        reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = reader.readLine()) != null){
                            sb.append(line);
                        }
                        Elements option = JSONParser.semesterParser(sb.toString());
                        //Log.i("brave", option.toString());
                        //保存信息
                        Info.saveInfoSemesterCT(context, option);
                        Intent intent = new Intent(MainActivity.this, CourseSearchActivity.class);
                        intent.putExtra("cookie", cookie);
                        startActivity(intent);
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
            Toast.makeText(MainActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
        }

    }

    public void classroomSearch(View view) {
        if(isOpenNetwork() == true){
            new Thread(){
                HttpURLConnection con = null;
                BufferedReader reader = null;
                InputStream in = null;
                Document doc = null;
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/KBFB_RoomSel.aspx");
                        con = (HttpURLConnection) url.openConnection();
                        //获取返回信息
                        in = con.getInputStream();
                        //抓取cookie
                        String cookie_temp = con.getHeaderField("Set-Cookie");
                        cookie = cookie_temp.split(";")[0];

                        reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = reader.readLine()) != null){
                            sb.append(line);
                        }
                        Elements option = JSONParser.semesterParser(sb.toString());
                        //保存信息
                        Info.saveInfoSemester(context, option);
                        Intent intent = new Intent(MainActivity.this, ClassroomSearchActivity.class);
                        intent.putExtra("cookie", cookie);
                        startActivity(intent);
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
            Toast.makeText(MainActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
        }
    }

    public void teacherSearch(View view) {
        if(isOpenNetwork() == true){
            new Thread(){
                HttpURLConnection con = null;
                BufferedReader reader = null;
                InputStream in = null;
                Document doc = null;
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/TeacherKBFB.aspx");
                        con = (HttpURLConnection) url.openConnection();
                        in = con.getInputStream();
                        //抓取cookie
                        String cookie_temp = con.getHeaderField("Set-Cookie");
                        cookie = cookie_temp.split(";")[0];

                        reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = reader.readLine()) != null){
                            sb.append(line);
                        }
                        Elements option = JSONParser.semesterParser(sb.toString());
                        //Log.i("brave", option.toString());
                        //保存信息
                        Info.saveInfoSemesterCT(context, option);
                        Intent intent = new Intent(MainActivity.this, TeacherSearchActivity.class);
                        intent.putExtra("cookie", cookie);
                        startActivity(intent);
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
            Toast.makeText(MainActivity.this, "请检查网络连接！", Toast.LENGTH_LONG).show();
        }

    }
    private boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
