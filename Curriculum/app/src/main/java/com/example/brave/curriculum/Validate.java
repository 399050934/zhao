package com.example.brave.curriculum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by brave on 2017/4/14.
 */

public class Validate {

    public static Bitmap getValidate(){
        HttpURLConnection con = null;
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            URL url_validate = new URL("http://115.159.106.126:8080/SearchCourse/validate");
            con = (HttpURLConnection) url_validate.openConnection();
            in = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
