package com.example.brave.curriculum;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by brave on 2017/4/14.
 */

public class JSONParser {

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
        //Log.i("brave", option.toString());

        return option;
    }

    public static Elements semesterParser(String content){
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
        //Log.i("brave", option.toString());

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

    public static boolean searchResultParser(String content){
        if(content.equals(" ")){
            return false;
        }else{
            return true;
        }
    }
}
