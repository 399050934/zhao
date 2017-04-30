package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import pojo.Classtable;
import service.CourseManager;
import util.ConnectUtil;
import util.Validate;

@ResponseBody
@Controller
public class CourseController {
	@Autowired
	private CourseManager courseManager;
	private static HashMap<String, String> map_semester = new HashMap<String, String>();
    private static HashMap<String, String> map_course = new HashMap<String, String>();
    private static HashMap<String, String> map_teacher = new HashMap<String, String>();
    private static HashMap<String, String> map_campus = new HashMap<String, String>();
    private static HashMap<String, String> map_building = new HashMap<String, String>();
    private static HashMap<String, String> map_room = new HashMap<String, String>();
	private static String cookie;
	Gson gson = new Gson();
	
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	//获取验证码
	@RequestMapping(value="/validate",method=RequestMethod.GET)
	public void validate(OutputStream out) throws IOException{
		InputStream in = Validate.getValidate();
		String validate = in.toString();
		byte[] buf = new byte[256];
		int len = 0;
		while((len = in.read(buf)) != -1){
			out.write(buf, 0, len);
		}
	}
	//查询课程表(数据库)
	@RequestMapping(value="/query/{params}",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public String queryLocal(@PathVariable String params){
		List<Classtable> table = courseManager.queryCourse(params);
		if(table.size() != 0){
			for(Classtable tb : table){
				String classTable = gson.toJson(tb);
				System.out.println(params + ":" + classTable);
				return classTable;
			}
		}
		return " ";
	}
	//获取课程表(联网查询)
	//按课程查询
	@RequestMapping(value="/queryNet/{params}/{formData}",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public String queryInternet(@PathVariable String params, @PathVariable String formData){
		//connectUtil = new ConnectUtil();
		Classtable ct = ConnectUtil.courseSearchResult(cookie, params, formData);
		if(ct == null){
			System.out.println("Controller:验证码错误！");
			return " ";
		}else if(ct.getInfo().equals("myxgkcb")){
			System.out.println("Controller:没有相关课程表！");
			courseManager.insertCourse(ct);
		}else{
			courseManager.insertCourse(ct);
		}
		String classTable = gson.toJson(ct);
		System.out.println("Controller:" + classTable);
		return classTable;
	}
	//按教师查询
	@RequestMapping(value="/queryNetTeacher/{params}/{formData}",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public String queryInternetTeacher(@PathVariable String params, @PathVariable String formData){
		//connectUtil = new ConnectUtil();
		Classtable ct = ConnectUtil.teacherSearchResult(cookie, params, formData);
		if(ct == null){
			System.out.println("Controller:验证码错误！");
			return " ";
		}else if(ct.getInfo().equals("myxgkcb")){
			System.out.println("Controller:没有相关课程表！");
			courseManager.insertCourse(ct);
		}else{
			courseManager.insertCourse(ct);
		}
		String classTable = gson.toJson(ct);
		System.out.println("Controller:" + classTable);
		return classTable;
	}
	//按教室查询
	@RequestMapping(value="/queryNetRoom/{params}/{formData}",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public String queryInternetRoom(@PathVariable String params, @PathVariable String formData){
		//connectUtil = new ConnectUtil();
		Classtable ct = ConnectUtil.classroomSearchResult(cookie, params, formData);
		if(ct == null){
			System.out.println("Controller:验证码错误！");
			return " ";
		}else if(ct.getInfo().equals("myxgkcb")){
			System.out.println("Controller:没有相关课程表！");
			courseManager.insertCourse(ct);
		}else{
			courseManager.insertCourse(ct);
		}
		String classTable = gson.toJson(ct);
		System.out.println("Controller:" + classTable);
		return classTable;
	}
	//获取下拉列表内容
	//学期下拉列表
	@RequestMapping(value="/semesterList")
	public HashMap<String, String> querySemesterList(){
		map_semester = ConnectUtil.semesterSearch();
		return map_semester;
	}
	//课程下拉列表
	@RequestMapping(value="/courseList")
	public HashMap<String, String> queryCourseList(){
		map_course = ConnectUtil.courseSearch();
		//System.out.println(map_course.toString());
		return map_course;
	}
	//教师下拉列表
	@RequestMapping(value="/teacherList")
	public HashMap<String, String> queryTeacherList(){
		map_teacher = ConnectUtil.teacherSearch();
		//System.out.println(map_course.toString());
		return map_teacher;
	}
	//campus下拉列表
	@RequestMapping(value="/campusList")
	public HashMap<String, String> queryCampusList(){
		map_campus = ConnectUtil.campusSearch();
		//System.out.println(map_course.toString());
		return map_campus;
	}
	//building下拉列表
	@RequestMapping(value="/buildingList/{campus_value}")
	public HashMap<String, String> queryBuildingList(@PathVariable String campus_value){
		map_building = ConnectUtil.buildingSearch(campus_value);
		//System.out.println(map_course.toString());
		return map_building;
	}
	//classroom下拉列表
	@RequestMapping(value="/roomList/{building_value}")
	public HashMap<String, String> queryRoomList(@PathVariable String building_value){
		map_room = ConnectUtil.roomSearch(building_value);
		//System.out.println(map_course.toString());
		return map_room;
	}
	
}
