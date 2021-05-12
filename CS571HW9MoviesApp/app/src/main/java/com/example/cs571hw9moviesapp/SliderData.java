package com.example.cs571hw9moviesapp;

import org.json.JSONException;
import org.json.JSONObject;

public class SliderData {
    // image url is used to
    // store the url of image
    //private String imgUrl;
    private JSONObject obj;

    // Constructor method.
    public SliderData(JSONObject obj) {
        this.obj = obj;
    }

    // Getter method
    public String getImgUrl() {
        String imgUrl = null;
        try {
            imgUrl = "https://image.tmdb.org/t/p/w500" + obj.getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }

    public String getType() {
        String type = null;
        try {
            type = obj.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return type;
    }

    public String getID() {
        String id = null;
        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getTitle() {
        String title = null;
        try {
            title = obj.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return title;
    }

    // Setter method
//    public void setImgUrl(String imgUrl) {
//        this.imgUrl = imgUrl;
//    }
}
