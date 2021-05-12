package com.example.cs571hw9moviesapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewRecyclerData {
    private JSONObject obj;

    public ReviewRecyclerData(JSONObject obj) {
        this.obj = obj;
    }

    public String getAuthor () {
        String author = null;
        try {
            author = obj.getString("author");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return author;
    }

    public String getDate () {
        String date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date d = sdf.parse(obj.getString("created_at"));
            DateFormat df5 = new SimpleDateFormat("E, MMM dd yyyy");
            date = df5.format(d);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public int getRating () {
        int rating = 0;
        try {
            rating = Integer.parseInt(obj.getString("rating"))/2;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rating;
    }

    public String getContent () {
        String content = null;
        try {
            content = obj.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return content;
    }
}
