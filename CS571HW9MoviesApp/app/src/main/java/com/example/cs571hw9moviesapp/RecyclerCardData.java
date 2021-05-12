package com.example.cs571hw9moviesapp;

import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerCardData {
    private JSONObject obj;

    public RecyclerCardData (JSONObject obj) {
        this.obj = obj;
    }

    public String getImgUrl() {
        String imgUrl = null;
        try {
            imgUrl = obj.getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }

    public String getTMDBUrl() {
        String tmdbUrl = null;
        try {
            if (obj.getString("type").equals("Movie")) {
                tmdbUrl = "https://www.themoviedb.org/movie/" + obj.getString("id");
            }
            else if (obj.getString("type").equals("TV")) {
                tmdbUrl = "https://www.themoviedb.org/tv/" + obj.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tmdbUrl;
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
}
