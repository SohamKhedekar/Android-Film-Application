package com.example.cs571hw9moviesapp;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchRecyclerCardData {
    private JSONObject obj;

    public SearchRecyclerCardData(JSONObject obj) {
        this.obj = obj;
    }

    public String getImgUrl () {
        String imgUrl = null;
        try {
            imgUrl = "https://image.tmdb.org/t/p/w500" + obj.getString("backdrop_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }

    public String getTypeWithReleaseDate() {
        StringBuilder type = new StringBuilder();
        try {
            type.append(obj.getString("media_type").toUpperCase());
            if (obj.has("release_date")) {
                type.append(" (" + obj.getString("release_date").split("-")[0] + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return type.toString();
    }

    public String getType() {
        String type = null;
        try {
            type = obj.getString("media_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return type;
    }

    public String getTitle() {
        String title = null;
        try {
            title = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return title;
    }

    public String getRating() {
        Double rating = null;
        try {
            rating = Math.round((Double.parseDouble(obj.getString("vote_average"))/2)*10.0)/10.0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(rating);
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
