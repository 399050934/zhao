package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import controller.CourseController;

/**
 * Created by brave on 2017/4/14.
 */

public class Validate {
	
	private static String cookie = "";
	
    public static InputStream getValidate(){
        CourseController cc = new CourseController();
    	HttpURLConnection con = null;
        InputStream in = null;
        try {
            URL url_validate = new URL("http://xg.zdsoft.com.cn/sys/ValidateCode.aspx");
            con = (HttpURLConnection) url_validate.openConnection();
            in = con.getInputStream();
            //抓取cookie
	        String cookie_temp = con.getHeaderField("Set-Cookie");
	        cookie = cookie_temp.split(";")[0];
	        cc.setCookie(cookie);
	        //System.out.println("Validate:" + cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}
