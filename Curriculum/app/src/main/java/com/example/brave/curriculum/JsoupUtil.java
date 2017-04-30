package com.example.brave.curriculum;

import com.example.brave.curriculum.Classtable;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JsoupUtil {

	//获取班级名
	public  Elements getJsoupClassname(){
		//Document doc = Jsoup.parse(content.toString());
		Document doc = null;
		//获取script
		try {
			doc = Jsoup.connect("http://xg.zdsoft.com.cn/ZNPK/KBFB_LessonSel_rpt.aspx").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Elements option = doc.select("table");
		option = option.select("option");
		System.out.println(option.toString());

		return option;
	}

	//获取老师
	public  Elements getJsoupTeachername(){
		//Document doc = Jsoup.parse(content.toString());
		Document doc = null;
		//获取script
		try {
			doc = Jsoup.connect("http://xg.zdsoft.com.cn/ZNPK/Private/List_JS.aspx?xnxq=20160&t=264").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String script = doc.data();

		script =script.substring(script.indexOf("'")+1, script.lastIndexOf("'"));
//		System.out.println(script);
		doc = Jsoup.parse(script);
		Elements option = doc.select("option");
		return option;
	}
	//获取教室
	public  Elements getroomParser(String url){
		//处理html页面
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String  script = doc.data();
		//处理数据
		script =script.substring(script.indexOf("'")+1, script.lastIndexOf("'"));
//        String html_tmp = script.toString();
//        int start = html_tmp.indexOf("<select");
//        int end = html_tmp.indexOf("</select>");
//        String html = html_tmp.substring(start, end);

		doc = Jsoup.parse(script);
		Elements option = doc.select("option");
		//Log.i("brave", html);

		return option;
	}
	//获取课程名
	public  Elements getJsoupCourse(){
		Document doc = null;
		try {
			doc = Jsoup.connect("http://xg.zdsoft.com.cn/ZNPK/Private/List_XNXQKC.aspx?xnxq=20160").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String script = doc.data();
		//System.out.println(script);

		script =script.substring(script.indexOf("'")+1, script.lastIndexOf("'"));
		doc = Jsoup.parse(script);
		Elements option = doc.select("option");
		return option;
	}

	//保存课程表
	public Classtable  getclasstableInfo(String html,String params) {
		html = html.replace("&ensp;", "-\r");
		Document doc = Jsoup.parse(html);
		//Elements name = doc.select("tr");

		Elements table = doc.select("table");
		if(table.size()<=1) {
			System.out.println("没有相关课程表");
			return null;
		}else if(table.size()>1){
			System.out.println("有"+table.size()+"个table");
			//表名
			String tablename = table.get(1).text();
			System.out.println(tablename);
			//表头信息
			String info = table.get(2).text();
			System.out.println(info);
			Elements trs = table.get(3).select("tr");
			String [][] classTable = new String[6][7];
			for(int i=1;i<trs.size();i++){
				Element tr=trs.get(i);
				Elements tds=tr.getElementsByTag("td");
//	              System.out.println("有"+trs.size()+"行"+tds.size()+"列");
				for(int j=1;j<tds.size();j++){
					//System.out.println("处理前："+i+"行"+j+"列 "+tds.get(j).text());
					if(i==1||i==3||i==5){
						if(j<tds.size()-1){
							Element td=tds.get(j+1);
							classTable[i-1][j-1]=td.text();
							// System.out.println("处理后："+i+"行"+j+"列 "+td.text());
						}
					}else{
						Element td=tds.get(j);
						classTable[i-1][j-1]=td.text();
						//System.out.println("处理后："+i+"行"+j+"列 "+td.text());
					}
				}
			}
			//显示测试
			for(int i =0;i<classTable.length;i++) {
				for(int j =0;j<classTable[i].length;j++) {
					System.out.println("第"+i+"行"+"第"+j+"列"+classTable[i][j]);
				}

			}
			Classtable ct = new Classtable(tablename+";"+info, classTable, params){

			};
			return ct;
		}

		return null;
	}

}

