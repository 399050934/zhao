package util;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InfoUtil {

    private static Map<String, String> map_semester = new HashMap<String, String>();
    private static Map<String, String> map_course = new HashMap<String, String>();
    private static Map<String, String> map_teacher = new HashMap<String, String>();
    private static Map<String, String> map_compus = new HashMap<String, String>();
    private static Map<String, String> map_building = new HashMap<String, String>();
    private static Map<String, String> map_room = new HashMap<String, String>();

    public static HashMap<String, String> getInfoCourse(Elements option) {
    	map_course.clear();
        for (int i = 0; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_course.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }

        Gson gson = new Gson();
        String json_course = gson.toJson(map_course);
        HashMap<String, String> map_course = gson.fromJson(json_course, new TypeToken<HashMap<String, String>>(){}.getType());

        return map_course;
    }

    public static HashMap<String, String> getInfoTeacher(Elements option) {
    	map_teacher.clear();
        for (int i = 0; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_teacher.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }

        Gson gson = new Gson();
        String json_teacher = gson.toJson(map_teacher);
        HashMap<String, String> map_teacher = gson.fromJson(json_teacher, new TypeToken<HashMap<String, String>>(){}.getType());

        return map_teacher;
    }
    
    public static HashMap<String, String> getInfoSemester(Elements option) {
    	map_semester.clear();
        map_semester.put(option.get(0).text(), option.get(0).attr("value"));

        Gson gson = new Gson();
        String json_semester = gson.toJson(map_semester);
        HashMap<String, String> map_semester = gson.fromJson(json_semester, new TypeToken<HashMap<String, String>>(){}.getType());
        
        return map_semester;
    }

    public static HashMap<String, String> getInfoCampus(Elements option) {
        map_compus.clear();
        for (int i = 1; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_compus.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }
        
        Gson gson = new Gson();
        String json_compus = gson.toJson(map_compus);
        HashMap<String, String> map_compus = gson.fromJson(json_compus, new TypeToken<HashMap<String, String>>(){}.getType());
        
        return map_compus;
    }

    public static HashMap<String, String> getInfoBuilding(Elements option) {
    	map_building.clear();
        for (int i = 0; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_building.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }

        Gson gson = new Gson();
        String json_building = gson.toJson(map_building);
        HashMap<String, String> map_building = gson.fromJson(json_building, new TypeToken<HashMap<String, String>>(){}.getType());
        
        return map_building;
    }

    public static HashMap<String, String> getInfoRoom(Elements option) {
    	map_room.clear();
        for (int i = 0; i < option.size(); i++) {
            if(!option.get(i).attr("value").equals("")){
                map_room.put(option.get(i).text(), option.get(i).attr("value"));
            }
        }

        Gson gson = new Gson();
        String json_room = gson.toJson(map_room);
        HashMap<String, String> map_room = gson.fromJson(json_room, new TypeToken<HashMap<String, String>>(){}.getType());
        
        return map_room;
    }
}
