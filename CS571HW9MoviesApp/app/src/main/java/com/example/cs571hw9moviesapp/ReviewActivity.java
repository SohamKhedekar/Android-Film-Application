package com.example.cs571hw9moviesapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {
    String author;
    String date;
    String rating;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_CS571HW9MoviesApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Bundle extras = getIntent().getExtras();
        author = extras.getString("author").toLowerCase();
        date = extras.getString("date");
        rating = extras.getString("rating");
        content = extras.getString("content");
        TextView ratingTxt = findViewById(R.id.review_activity_rating);
        ratingTxt.setText(rating + "/5");
        TextView titleTxt = findViewById(R.id.review_activity_title);
        titleTxt.setText("by " + author + " on " + date);
        TextView contentTxt = findViewById(R.id.review_activity_content);
        contentTxt.setText(content);
    }
}
