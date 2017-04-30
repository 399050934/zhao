package com.example.brave.curriculum;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brave on 2017/4/14.
 */

public class Info {

    private static final String FILENAME_semester ="semester";
    private static final String FILENAME_course ="course";
    private static final String FILENAME_teacher ="teacher";
    private static final String FILENAME_compus ="compus";
    private static final String FILENAME_building ="building";
    private static final String FILENAME_room ="room";

    private static Map<String, String> map_semester = new HashMap<String, String>();
    private static Map<String, String> map_course = new HashMap<String, String>();
    private static Map<String, String> map_teacher = new HashMap<String, String>();
    private static Map<String, String> map_compus = new HashMap<String, String>();
    private static Map<String, String> map_building = new HashMap<String, String>();
    private static Map<String, String> map_room = new HashMap<String, String>();

    public static void saveInfoSemesterCT(Context context, Elements option) {
        map_semester.clear();
        map_semester.put(option.get(0).text(), option.get(0).attr("value"));
        //Log.i("brave", map_compus.toString());

        Gson gson = new Gson();
        String json_semester = gson.toJson(map_semester);
        //Log.i("brave",json_semester);

        //存入数据
        SharedPreferences share_semester = context.getSharedPreferences(FILENAME_semester, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit_semester = share_semester.edit();//获得edit()接口
        edit_semester.putString("semester", json_semester);//保存字符串类型；
        edit_semester.commit();//提交
    }

    public static void saveInfoCourse(Context context, Elements option) {
        map_course.clear();
        for (int i = 0; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_course.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }
        //Log.i("brave", map_course.toString());

        Gson gson = new Gson();
        String json_course = gson.toJson(map_course);
        //Log.i("brave",json_course);

        //存入数据
        SharedPreferences share_course = context.getSharedPreferences(FILENAME_course, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit_course = share_course.edit();//获得edit()接口
        edit_course.putString("course", json_course);//保存字符串类型；
        edit_course.commit();//提交
    }
    public static void saveInfoTeacher(Context context, Elements option) {
        map_teacher.clear();
        for (int i = 0; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_teacher.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }
        //Log.i("brave", map_course.toString());

        Gson gson = new Gson();
        String json_teacher = gson.toJson(map_teacher);
        //Log.i("brave",json_course);

        //存入数据
        SharedPreferences share_teacher = context.getSharedPreferences(FILENAME_teacher, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit_teacher = share_teacher.edit();//获得edit()接口
        edit_teacher.putString("teacher", json_teacher);//保存字符串类型；
        edit_teacher.commit();//提交
    }

    public static void saveInfoSemester(Context context, Elements option) {
        //将数据存入HashMap
        map_semester.clear();
        map_compus.clear();
        map_semester.put(option.get(0).text(), option.get(0).attr("value"));
        for (int i = 1; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_compus.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }
        //Log.i("brave", map_compus.toString());
        //将HashMap转换为Json对象
        Gson gson = new Gson();
        String json_semester = gson.toJson(map_semester);
        String json_compus = gson.toJson(map_compus);
        //Log.i("brave",json_semester);

        //存入数据
        SharedPreferences share_semester = context.getSharedPreferences(FILENAME_semester, Context.MODE_PRIVATE);
        SharedPreferences share_compus = context.getSharedPreferences(FILENAME_compus, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit_semester = share_semester.edit();//获得edit()接口
        SharedPreferences.Editor edit_compus = share_compus.edit();
        edit_semester.putString("semester", json_semester);//保存字符串类型；
        edit_compus.putString("compus", json_compus);
        edit_semester.commit();//提交
        edit_compus.commit();
    }

    public static void saveInfoBuilding(Context context, Elements option) {
        //将数据存入HashMap
        map_building.clear();
        for (int i = 0; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_building.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }
        //Log.i("brave", map_building.toString());
        //将HashMap转换为Json对象
        Gson gson = new Gson();
        String json_building = gson.toJson(map_building);
        //Log.i("brave",json_building);

        //存入数据
        SharedPreferences share_building = context.getSharedPreferences(FILENAME_building, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit_building = share_building.edit();//获得edit()接口
        edit_building.putString("building", json_building);//保存字符串类型；
        edit_building.commit();//提交
    }

    public static void saveInfoRoom(Context context, Elements option) {
        //将数据存入HashMap
        map_room.clear();
        for (int i = 0; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_room.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }
        //Log.i("brave", map_room.toString());
        //将HashMap转换为Json对象
        Gson gson = new Gson();
        String json_room = gson.toJson(map_room);
        //Log.i("brave",json_room);

        //存入数据
        SharedPreferences share_room = context.getSharedPreferences(FILENAME_room, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit_room = share_room.edit();//获得edit()接口
        edit_room.putString("room", json_room);//保存字符串类型；
        edit_room.commit();//提交
    }

    public static HashMap<String, String> getInfoCourse(Context context) {
        Gson gson = new Gson();
        SharedPreferences share_course = context.getSharedPreferences(FILENAME_course, Context.MODE_PRIVATE);
        String json = share_course.getString("course", "");
        HashMap<String, String> map_course = gson.fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
        //Log.i("brave", map_course.toString());
        return map_course;
    }

    public static HashMap<String, String> getInfoTeacher(Context context) {
        Gson gson = new Gson();
        SharedPreferences share_teacher = context.getSharedPreferences(FILENAME_teacher, Context.MODE_PRIVATE);
        String json = share_teacher.getString("teacher", "");
        HashMap<String, String> map_teacher = gson.fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
        //Log.i("brave", map_teacher.toString());
        return map_teacher;
    }

    public static HashMap<String, String> getInfoSemester(Context context) {
        Gson gson = new Gson();
        SharedPreferences share_semester = context.getSharedPreferences(FILENAME_semester, Context.MODE_PRIVATE);
        String json = share_semester.getString("semester", "");
        HashMap<String, String> map_semester = gson.fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
        //Log.i("brave", map_semester.toString());
        return map_semester;
    }

    public static HashMap<String, String> getInfoCompus(Context context) {
        Gson gson = new Gson();
        SharedPreferences share_compus = context.getSharedPreferences(FILENAME_compus, Context.MODE_PRIVATE);
        String json = share_compus.getString("compus", "");
        HashMap<String, String> map_compus = gson.fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
        //Log.i("brave", map_compus.toString());
        return map_compus;
    }

    public static HashMap<String, String> getInfoBuilding(Context context) {
        Gson gson = new Gson();
        SharedPreferences share_building = context.getSharedPreferences(FILENAME_building, Context.MODE_PRIVATE);
        String json = share_building.getString("building", "");
        HashMap<String, String> map_building = gson.fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
        //Log.i("brave", map_building.toString());
        return map_building;
    }

    public static HashMap<String, String> getInfoRoom(Context context) {
        Gson gson = new Gson();
        SharedPreferences share_room = context.getSharedPreferences(FILENAME_room, Context.MODE_PRIVATE);
        String json = share_room.getString("room", "");
        HashMap<String, String> map_room = gson.fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());
        //Log.i("brave", map_room.toString());
        return map_room;
    }

}
