package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pojo.Classtable;


public class JsoupUtil {
	public static Elements courseParser(String content){
        //处理html页面
        Document doc = Jsoup.parse(content.toString());
        Elements script = doc.select("script");
        //处理数据
        String html_tmp = script.toString();
        int start = html_tmp.indexOf("<select");
        int end = html_tmp.indexOf("</select>");
        String html = html_tmp.substring(start, end);

        doc = Jsoup.parse(html.toString());
        Elements option = doc.select("option");

        return option;
    }

    public static Elements semesterParser(String content){
        //处理html页面
        Document doc = Jsoup.parse(content.toString());
        Elements option = doc.select("option");
        
        return option;
    }
    
    public static Elements campusParser(String content){
        //处理html页面
        Document doc = Jsoup.parse(content.toString());
        Elements option = doc.select("option");
        //Log.i("brave", option);
        return option;
    }

    public static Elements buildingParser(String content){
        //处理html页面
        Document doc = Jsoup.parse(content.toString());
        Elements script = doc.select("script");
        //处理数据
        String html_tmp = script.toString();
        int start = html_tmp.indexOf("<select");
        int end = html_tmp.indexOf("</select>");
        String html = html_tmp.substring(start, end);

        doc = Jsoup.parse(html.toString());
        Elements option = doc.select("option");

        return option;
    }

    public static Elements roomParser(String content){
        //处理html页面
        Document doc = Jsoup.parse(content.toString());
        Elements script = doc.select("script");
        //处理数据
        String html_tmp = script.toString();
        int start = html_tmp.indexOf("<select");
        int end = html_tmp.indexOf("</select>");
        String html = html_tmp.substring(start, end);

        doc = Jsoup.parse(html.toString());
        Elements option = doc.select("option");
        //Log.i("brave", html);

        return option;
    }
    
    public static Elements teacherParser(String content){
        //处理html页面
        Document doc = Jsoup.parse(content.toString());
        Elements script = doc.select("script");
        //处理数据
        String html_tmp = script.toString();
        int start = html_tmp.indexOf("<select");
        int end = html_tmp.indexOf("</select>");
        String html = html_tmp.substring(start, end);

        doc = Jsoup.parse(html.toString());
        Elements option = doc.select("option");
        
        return option;
    }

	//保存课程表
	public static Classtable getClasstableInfo(String content,String params) {
		content = content.replace("&ensp;", "-\r");
		Document doc = Jsoup.parse(content);
		//Elements name = doc.select("tr");

		Elements table = doc.select("table");
		if(table.size()<=1) {
			System.out.println("没有相关课程表");
			Classtable ct = new Classtable("myxgkcb", params);
			return ct;
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
			Classtable ct = new Classtable(tablename+";"+info, classTable, params);
			return ct;
		}

		return null;
	}
	
	public static boolean searchResultParser(String content){
        if(content.contains("验证码错误")){
            return false;
        }else{
            return true;
        }
    }

}

