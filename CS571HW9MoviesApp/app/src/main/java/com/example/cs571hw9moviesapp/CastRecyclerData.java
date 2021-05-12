package com.example.cs571hw9moviesapp;

import org.json.JSONException;
import org.json.JSONObject;

public class CastRecyclerData {
    private JSONObject obj;

    public CastRecyclerData (JSONObject obj) {
        this.obj = obj;
    }

    public String getImgUrl() {
        String imgUrl = null;
        try {
            imgUrl = obj.getString("profile_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }

    public String getName() {
        String name = null;
        try {
            name = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
}
