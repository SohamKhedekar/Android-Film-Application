package com.example.cs571hw9moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    String id;
    String type;
    String title;
    YouTubePlayerView youTubePlayerView;
    ImageView backdropImage;
    TextView titleTxt;
    ProgressBar spinner;
    TextView spinnerText;
    LinearLayout detailsContent;
    TextView overviewTitle;
    ReadMoreTextView overviewTxt;
    TextView genresTitle;
    TextView genresTxt;
    TextView yearTitle;
    TextView yearTxt;
    ImageView addToWatchlistButton;
    ImageView removeFromWatchlistButton;
    ImageView twitterButton;
    ImageView fbButton;
    TextView castTitle;
    private RecyclerView castRecyclerView;
    ArrayList<CastRecyclerData> castRecyclerData;
    TextView reviewsTitle;
    private RecyclerView reviewRecyclerView;
    ArrayList<ReviewRecyclerData> reviewRecyclerData;
    private ReviewRecyclerAdapter.RecyclerViewClickListener listener;
    TextView recTitle;
    private RecyclerView recRecyclerView;
    ArrayList<RecyclerCardData> recRecyclerData;
    private RecRecyclerAdapter.RecyclerViewClickListener recListener;

    RequestQueue queue;
    int requestCount = 0;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    Map<String, String> watchlistElements;
    String prefData;
    String prefValue;
    String[] prefValueItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_CS571HW9MoviesApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();
        type = extras.getString("type").toLowerCase();
        id = extras.getString("id");
        title = extras.getString("title");
        spinner = findViewById(R.id.progressBar1);
        spinnerText = findViewById(R.id.progressBar1_text);
        detailsContent = findViewById(R.id.details_content);
        spinner.setVisibility(View.VISIBLE);
        spinnerText.setVisibility(View.VISIBLE);
        detailsContent.setVisibility(View.GONE);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.setVisibility(View.GONE);
        backdropImage = findViewById(R.id.backdrop_image);
        backdropImage.setVisibility(View.GONE);
        titleTxt = findViewById(R.id.title_txt);
        overviewTitle = findViewById(R.id.overview_title);
        overviewTxt = findViewById(R.id.overview_txt);
        overviewTitle.setVisibility(View.GONE);
        overviewTxt.setVisibility(View.GONE);
        genresTitle = findViewById(R.id.genres_title);
        genresTxt = findViewById(R.id.genres_txt);
        genresTitle.setVisibility(View.GONE);
        genresTxt.setVisibility(View.GONE);
        yearTitle = findViewById(R.id.year_title);
        yearTxt = findViewById(R.id.year_txt);
        yearTitle.setVisibility(View.GONE);
        yearTxt.setVisibility(View.GONE);
        addToWatchlistButton = findViewById(R.id.add_to_watchlist_button);
        addToWatchlistButton.setOnClickListener(addToWatchlistListener);
        removeFromWatchlistButton = findViewById(R.id.remove_from_watchlist_button);
        removeFromWatchlistButton.setOnClickListener(removeFromWatchlistListener);
        twitterButton = findViewById(R.id.twitter_button);
        fbButton = findViewById(R.id.fb_button);
        castTitle = findViewById(R.id.cast_title);
        castRecyclerView = findViewById(R.id.recycler_cast);
        castRecyclerData = new ArrayList<>();
        reviewsTitle = findViewById(R.id.reviews_title);
        reviewRecyclerView = findViewById(R.id.recycler_reviews);
        reviewRecyclerData = new ArrayList<>();
        recTitle = findViewById(R.id.rec_title);
        recRecyclerView = findViewById(R.id.recycler_rec);
        recRecyclerData = new ArrayList<>();

        queue = Volley.newRequestQueue(this);

        pref = getSharedPreferences("Watchlist", 0);
        prefEditor = pref.edit();

        getData();
    }

    View.OnClickListener addToWatchlistListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            watchlistElements = (Map<String, String>) pref.getAll();
            prefData = watchlistElements.getOrDefault("items", "");
            prefData += prefValue + ";";
            prefEditor.putString("items", prefData);
            prefEditor.commit();
            addToWatchlistButton.setVisibility(View.GONE);
            removeFromWatchlistButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), title + " was added to Watchlist", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener removeFromWatchlistListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            watchlistElements = (Map<String, String>) pref.getAll();
            prefData = watchlistElements.getOrDefault("items", "");
            prefValueItems = prefData.split(";");
            StringBuilder newPrefData = new StringBuilder();
            if (prefValueItems.length > 1) {
                for (String item:prefValueItems) {
                    if (!prefValue.equals(item)) {
                        newPrefData.append(item + ";");
                    }
                }
            }
            prefEditor.putString("items", newPrefData.toString());
            prefEditor.commit();
            removeFromWatchlistButton.setVisibility(View.GONE);
            addToWatchlistButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), title + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
        }
    };

    private void getData() {
        if (type.equals("movie")) {
            jsonParseVideo("https://sk-wt-hws-9.wl.r.appspot.com/apis/moviesVideo/?movie_id=" + id);
            jsonParseDetails("https://sk-wt-hws-9.wl.r.appspot.com/apis/movieDetails/?movie_id=" + id);
            jsonParseCast("https://sk-wt-hws-9.wl.r.appspot.com/apis/movieCast/?movie_id=" + id);
            jsonParseReviews("https://sk-wt-hws-9.wl.r.appspot.com/apis/movieReviews/?movie_id=" + id);
            jsonParseRec("https://sk-wt-hws-9.wl.r.appspot.com/apis/recommendedMovies/?movie_id=" + id);
        }
        else {
            jsonParseVideo("https://sk-wt-hws-9.wl.r.appspot.com/apis/tvShowVideo/?tv_show_id=" + id);
            jsonParseDetails("https://sk-wt-hws-9.wl.r.appspot.com/apis/tvShowDetails/?tv_show_id=" + id);
            jsonParseCast("https://sk-wt-hws-9.wl.r.appspot.com/apis/tvShowCast/?tv_show_id=" + id);
            jsonParseReviews("https://sk-wt-hws-9.wl.r.appspot.com/apis/tvShowReviews/?tv_show_id=" + id);
            jsonParseRec("https://sk-wt-hws-9.wl.r.appspot.com/apis/recommendedTVShows/?tv_show_id=" + id);
        }
    }

    private void jsonParseVideo(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("key")) {
                            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                @Override
                                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                    String videoId = null;
                                    try {
                                        videoId = response.getString("key");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    youTubePlayer.cueVideo(videoId, 0);
                                }
                            });
                            youTubePlayerView.setVisibility(View.VISIBLE);
                        }
                        else {
                            backdropImage.setVisibility(View.VISIBLE);
                        }
                        requestPending();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        queue.add(request);
        requestCount++;
    }

    private void jsonParseDetails(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("backdrop_path")) {
                                Glide.with(getApplicationContext())
                                        .load(response.getString("backdrop_path"))
                                        .fitCenter()
                                        .into(backdropImage);
                            }
                            else {
                                backdropImage.setImageResource(R.drawable.backdrop_path_placeholder);
                            }
                            titleTxt.setText(response.getString("title"));
                            if (response.has("overview")) {
                                overviewTxt.setText(response.getString("overview"));
                                overviewTitle.setVisibility(View.VISIBLE);
                                overviewTxt.setVisibility(View.VISIBLE);
                            }
                            if (response.has("genres")) {
                                genresTxt.setText(response.getString("genres"));
                                genresTitle.setVisibility(View.VISIBLE);
                                genresTxt.setVisibility(View.VISIBLE);
                            }
                            if (response.has("release_date")) {
                                yearTxt.setText(response.getString("release_date"));
                                yearTitle.setVisibility(View.VISIBLE);
                                yearTxt.setVisibility(View.VISIBLE);
                            }
                            prefValue = response.getString("type") + "-" + id +
                                    "-" + response.getString("title") + "-" + response.getString("poster_path");
                            watchlistElements = (Map<String, String>) pref.getAll();
                            if (ifWatchlistContains()) {
                                addToWatchlistButton.setVisibility(View.GONE);
                            }
                            else {
                                removeFromWatchlistButton.setVisibility(View.GONE);
                            }
                            fbButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = null;
                                    try {
                                        browserIntent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" +
                                                "https://www.themoviedb.org/" + type +  "/" + response.getString("id")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(browserIntent);
                                }
                            });
                            twitterButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = null;
                                    try {
                                        browserIntent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https://twitter.com/intent/tweet?text=Check this out!&url=" +
                                                        "https://www.themoviedb.org/" + type +  "/" + response.getString("id")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(browserIntent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestPending();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        queue.add(request);
        requestCount++;
    }

    private void jsonParseCast(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("results")) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("results");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    CastRecyclerData temp = new CastRecyclerData(obj);
                                    castRecyclerData.add(temp);
                                }
                                setCastAdapter();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            castTitle.setVisibility(View.GONE);
                            castRecyclerView.setVisibility(View.GONE);
                        }
                        requestPending();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        queue.add(request);
        requestCount++;
    }

    private void jsonParseReviews(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("results")) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("results");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    reviewRecyclerData.add(new ReviewRecyclerData(obj));
                                }
                                setReviewAdapter();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            reviewsTitle.setVisibility(View.GONE);
                            reviewRecyclerView.setVisibility(View.GONE);
                        }
                        requestPending();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        queue.add(request);
        requestCount++;
    }

    private void jsonParseRec(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("results")) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("results");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    recRecyclerData.add(new RecyclerCardData(obj));
                                }
                                setRecAdapter();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            recTitle.setVisibility(View.GONE);
                            recRecyclerView.setVisibility(View.GONE);
                        }
                        requestPending();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        queue.add(request);
        requestCount++;
    }

    private void requestPending() {
        requestCount--;
        if (requestCount == 0) {
            spinner.setVisibility(View.GONE);
            spinnerText.setVisibility(View.GONE);
            detailsContent.setVisibility(View.VISIBLE);
        }
    }

    private boolean ifWatchlistContains () {
        prefData = watchlistElements.getOrDefault("items", "");
        if (prefData.length() != 0) {
            prefValueItems = prefData.split(";");
            for (String item:prefValueItems) {
                if (prefValue.equals(item))
                    return true;
            }
        }
        return false;
    }

    private void setCastAdapter() {
        CastRecyclerAdapter adapter = new CastRecyclerAdapter(castRecyclerData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        castRecyclerView.setLayoutManager(gridLayoutManager);
        castRecyclerView.setItemAnimator(new DefaultItemAnimator());
        castRecyclerView.setAdapter(adapter);
        castRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setReviewAdapter() {
        setOnClickListener(reviewRecyclerData);
        ReviewRecyclerAdapter adapter = new ReviewRecyclerAdapter(reviewRecyclerData, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setAdapter(adapter);
        reviewRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setOnClickListener(ArrayList<ReviewRecyclerData> reviewRecyclerData) {
        listener = new ReviewRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putExtra("author", reviewRecyclerData.get(position).getAuthor());
                intent.putExtra("date", reviewRecyclerData.get(position).getDate());
                intent.putExtra("rating", String.valueOf(reviewRecyclerData.get(position).getRating()));
                intent.putExtra("content", reviewRecyclerData.get(position).getContent());
                startActivity(intent);
            }
        };
    }

    private void setRecAdapter() {
        setRecOnClickListener();
        RecRecyclerAdapter adapter = new RecRecyclerAdapter(recRecyclerData, recListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recRecyclerView.setLayoutManager(layoutManager);
        recRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recRecyclerView.setAdapter(adapter);
        recRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setRecOnClickListener() {
        recListener = new RecRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("type", recRecyclerData.get(position).getType());
                intent.putExtra("id", recRecyclerData.get(position).getID());
                intent.putExtra("title", recRecyclerData.get(position).getTitle());
                startActivity(intent);
            }
        };
    }

}
