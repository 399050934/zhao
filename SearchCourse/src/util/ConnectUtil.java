package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.select.Elements;

import pojo.Classtable;

public class ConnectUtil {
	
	private static HashMap<String, String> map_semester = new HashMap<String, String>();
    private static HashMap<String, String> map_course = new HashMap<String, String>();
    private static HashMap<String, String> map_teacher = new HashMap<String, String>();
    private static HashMap<String, String> map_campus = new HashMap<String, String>();
    private static HashMap<String, String> map_building = new HashMap<String, String>();
    private static HashMap<String, String> map_room = new HashMap<String, String>();
    
	
    public static HashMap<String, String> semesterSearch(){
		HttpURLConnection con = null;
        BufferedReader reader = null;
        InputStream in = null;
		URL url;
		try {
			url = new URL("http://xg.zdsoft.com.cn/ZNPK/KBFB_LessonSel.aspx");
			con = (HttpURLConnection) url.openConnection();
	        in = con.getInputStream();

	        reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while((line = reader.readLine()) != null){
	            sb.append(line);
	        }
	        Elements option = JsoupUtil.semesterParser(sb.toString());
	        
	        map_semester = InfoUtil.getInfoSemester(option);
	        return map_semester;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
	public static HashMap<String, String> courseSearch(){
		HttpURLConnection con = null;
        BufferedReader reader = null;
        InputStream in = null;
		URL url;
		try {
			url = new URL("http://xg.zdsoft.com.cn/ZNPK/Private/List_XNXQKC.aspx?xnxq=20160");
			con = (HttpURLConnection) url.openConnection();
	        in = con.getInputStream();

	        reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while((line = reader.readLine()) != null){
	            sb.append(line);
	        }
	        Elements option = JsoupUtil.courseParser(sb.toString());
	        
	        map_course = InfoUtil.getInfoCourse(option);
	        return map_course;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static HashMap<String, String> campusSearch(){
		HttpURLConnection con = null;
	    BufferedReader reader = null;
	    InputStream in = null;
	    try {
            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/KBFB_RoomSel.aspx");
            con = (HttpURLConnection) url.openConnection();
            //获取返回信息
            in = con.getInputStream();

            reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            Elements option = JsoupUtil.campusParser(sb.toString());
            
            map_campus = InfoUtil.getInfoCampus(option);
	        return map_campus;
        } catch (Exception e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	public static HashMap<String, String> buildingSearch(String campus_value){
		HttpURLConnection con = null;
	    BufferedReader reader = null;
	    InputStream in = null;
	    try {
            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/Private/List_JXL.aspx?w=150&id=" + campus_value);
            con = (HttpURLConnection) url.openConnection();
            //获取返回信息
            in = con.getInputStream();

            reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            Elements option = JsoupUtil.buildingParser(sb.toString());
            
            map_building = InfoUtil.getInfoBuilding(option);
	        return map_building;
        } catch (Exception e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	public static HashMap<String, String> roomSearch(String building_value){
		HttpURLConnection con = null;
	    BufferedReader reader = null;
	    InputStream in = null;
	    try {
            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/Private/List_ROOM.aspx?w=150&id=" + building_value);
            con = (HttpURLConnection) url.openConnection();
            //获取返回信息
            in = con.getInputStream();

            reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            Elements option = JsoupUtil.roomParser(sb.toString());
            
            map_room = InfoUtil.getInfoRoom(option);
	        return map_room;
        } catch (Exception e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	public static HashMap<String, String> teacherSearch(){
		HttpURLConnection con = null;
	    BufferedReader reader = null;
	    InputStream in = null;
	    try {
            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/Private/List_JS.aspx?xnxq=20160&t=896");
            con = (HttpURLConnection) url.openConnection();
            in = con.getInputStream();

            reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            Elements option = JsoupUtil.teacherParser(sb.toString());
            
            map_teacher = InfoUtil.getInfoCourse(option);
	        return map_teacher;
        } catch (Exception e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	
	//获取课程表(按课程查询)
	public static Classtable courseSearchResult(String cookie, String params, String formData) {
		//params = semester_value + course_value;
		HttpURLConnection con = null;
		BufferedReader reader = null;
		InputStream in = null;
		try {
            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/KBFB_LessonSel_rpt.aspx");
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Referer", "http://xg.zdsoft.com.cn/ZNPK/KBFB_LessonSel.aspx");
            //发送cookie
            con.addRequestProperty("Cookie", cookie);
            //向服务器提交数据
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(formData);

            in = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
            StringBuilder sb = new StringBuilder();
            sb.setLength(0);
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            
            if(JsoupUtil.searchResultParser(sb.toString())){
                String html = sb.toString();
                //params = semester_value + course_value;
                Classtable table = JsoupUtil.getClasstableInfo(html, params);
                return table;
            }else{
            	//提示验证码错误
            	System.out.println("验证码错误！");
            	return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
	
	//获取课程表(按教师查询)
	public static Classtable teacherSearchResult(String cookie, String params, String formData) {
		//params = semester_value + course_value;
		HttpURLConnection con = null;
		BufferedReader reader = null;
		InputStream in = null;
		try {
            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/TeacherKBFB_rpt.aspx");
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Referer", "http://xg.zdsoft.com.cn/ZNPK/TeacherKBFB.aspx");
            //发送cookie
            con.addRequestProperty("Cookie", cookie);
            //向服务器提交数据
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(formData);

            in = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
            StringBuilder sb = new StringBuilder();
            sb.setLength(0);
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            
            if(JsoupUtil.searchResultParser(sb.toString())){
                String html = sb.toString();
                //params = semester_value + course_value;
                Classtable table = JsoupUtil.getClasstableInfo(html, params);
                return table;
            }else{
                //提示验证码错误
            	//InputStream in_validate = Validate.getValidate();
            	return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
		
	//获取课程表(按教室查询)
	public static Classtable classroomSearchResult(String cookie, String params, String formData) {
		//params = semester_value + course_value;
		HttpURLConnection con = null;
		BufferedReader reader = null;
		InputStream in = null;
		try {
            URL url = new URL("http://xg.zdsoft.com.cn/ZNPK/KBFB_RoomSel_rpt.aspx");
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Referer", "http://xg.zdsoft.com.cn/ZNPK/KBFB_RoomSel.aspx");
            //发送cookie
            con.addRequestProperty("Cookie", cookie);
            //向服务器提交数据
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(formData);

            in = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
            StringBuilder sb = new StringBuilder();
            sb.setLength(0);
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            
            if(JsoupUtil.searchResultParser(sb.toString())){
                String html = sb.toString();
                //params = semester_value + course_value;
                Classtable table = JsoupUtil.getClasstableInfo(html, params);
                return table;
            }else{
            	//提示验证码错误
            	//InputStream in_validate = Validate.getValidate();
            	return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
}
