package com.example.cs571hw9moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
    SliderView sliderMovie;
    TextView txtTopRatedMovie;
    private ArrayList<RecyclerCardData> cardDataListTopRatedMovie;
    private RecyclerView recyclerViewTopRatedMovie;
    TextView txtPopularMovie;
    private ArrayList<RecyclerCardData> cardDataListPopularMovie;
    private RecyclerView recyclerViewPopularMovie;
    TextView footerTMDBMovie;
    TextView footerDevMovie;
    SliderView sliderTV;
    TextView txtTopRatedTV;
    private ArrayList<RecyclerCardData> cardDataListTopRatedTV;
    private RecyclerView recyclerViewTopRatedTV;
    TextView txtPopularTV;
    private ArrayList<RecyclerCardData> cardDataListPopularTV;
    private RecyclerView recyclerViewPopularTV;
    TextView footerTMDBTV;
    TextView footerDevTV;

    RequestQueue queue;
    int requestCount = 0;
    ProgressBar spinner;
    TextView spinnerText;
    NestedScrollView sv;
    Toolbar toolbar;
    Intent browserIntent;
    private RecyclerAdapter.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(toolbarListener);
//        setHasOptionsMenu(true);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        spinner = view.findViewById(R.id.progressBar1);
        spinnerText = view.findViewById(R.id.progressBar1_text);
        sv = view.findViewById(R.id.scroll_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sliderMovie = getView().findViewById(R.id.slider_movie);
        txtTopRatedMovie = getView().findViewById(R.id.txt_top_rated_movie);
        recyclerViewTopRatedMovie = getView().findViewById(R.id.recycler_top_rated_movie);
        cardDataListTopRatedMovie = new ArrayList<>();
        txtPopularMovie = getView().findViewById(R.id.txt_popular_movie);
        recyclerViewPopularMovie = getView().findViewById(R.id.recycler_popular_movie);
        cardDataListPopularMovie = new ArrayList<>();
        footerTMDBMovie = getView().findViewById(R.id.footer_tmdb_movie);
        footerTMDBMovie.setOnClickListener(footerOnClickListener);
        footerDevMovie = getView().findViewById(R.id.footer_dev_movie);
        sliderTV = getView().findViewById(R.id.slider_tv);
        txtTopRatedTV = getView().findViewById(R.id.txt_top_rated_TV);
        recyclerViewTopRatedTV = getView().findViewById(R.id.recycler_top_rated_tv);
        cardDataListTopRatedTV = new ArrayList<>();
        txtPopularTV = getView().findViewById(R.id.txt_popular_tv);
        recyclerViewPopularTV = getView().findViewById(R.id.recycler_popular_tv);
        cardDataListPopularTV = new ArrayList<>();
        footerTMDBTV = getView().findViewById(R.id.footer_tmdb_tv);
        footerTMDBTV.setOnClickListener(footerOnClickListener);
        footerDevTV = getView().findViewById(R.id.footer_dev_tv);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        sv.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        spinnerText.setVisibility(View.VISIBLE);
        getData();
        getMovieData();
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.main_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.movie_tab:
//                getMovieData();
//                item.setChecked(true);
//                break;
//            case R.id.tv_tab:
//                getTVData();
//                item.setChecked(true);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private Toolbar.OnMenuItemClickListener toolbarListener =
            new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.movie_tab:
                    getMovieData();
                    break;
                case R.id.tv_tab:
                    getTVData();
                    break;
            }
            return true;
        }
    };

    private View.OnClickListener footerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"));
            startActivity(browserIntent);
        }
    };

    private void getData() {
        jsonParseSlider("https://sk-wt-hws-9.wl.r.appspot.com/apis/currentlyPlayingMovies", sliderMovie);
        jsonParseScroll("https://sk-wt-hws-9.wl.r.appspot.com/apis/topRatedMovies", cardDataListTopRatedMovie);
        setAdapter(cardDataListTopRatedMovie, recyclerViewTopRatedMovie);
        jsonParseScroll("https://sk-wt-hws-9.wl.r.appspot.com/apis/popularMovies", cardDataListPopularMovie);
        setAdapter(cardDataListPopularMovie, recyclerViewPopularMovie);
        jsonParseSlider("https://sk-wt-hws-9.wl.r.appspot.com/apis/trendingTVShows", sliderTV);
        jsonParseScroll("https://sk-wt-hws-9.wl.r.appspot.com/apis/topRatedTVShows", cardDataListTopRatedTV);
        setAdapter(cardDataListTopRatedTV, recyclerViewTopRatedTV);
        jsonParseScroll("https://sk-wt-hws-9.wl.r.appspot.com/apis/popularTVShows", cardDataListPopularTV);
        setAdapter(cardDataListPopularTV, recyclerViewPopularTV);
    }

    private void setAdapter(ArrayList<RecyclerCardData> cardDataList, RecyclerView recyclerView) {
        setOnClickListener(cardDataList);
        RecyclerAdapter adapter = new RecyclerAdapter(cardDataList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setOnClickListener(ArrayList<RecyclerCardData> cardDataList) {
        listener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("type", cardDataList.get(position).getType());
                intent.putExtra("id", cardDataList.get(position).getID());
                intent.putExtra("title", cardDataList.get(position).getTitle());
                startActivity(intent);
            }
        };
    }

    private void getMovieData() {
        sliderTV.setVisibility(View.GONE);
        txtTopRatedTV.setVisibility(View.GONE);
        recyclerViewTopRatedTV.setVisibility(View.GONE);
        txtPopularTV.setVisibility(View.GONE);
        recyclerViewPopularTV.setVisibility(View.GONE);
        footerTMDBTV.setVisibility(View.GONE);
        footerDevTV.setVisibility(View.GONE);
        sliderMovie.setVisibility(View.VISIBLE);
        txtTopRatedMovie.setVisibility(View.VISIBLE);
        recyclerViewTopRatedMovie.setVisibility(View.VISIBLE);
        txtPopularMovie.setVisibility(View.VISIBLE);
        recyclerViewPopularMovie.setVisibility(View.VISIBLE);
        footerTMDBMovie.setVisibility(View.VISIBLE);
        footerDevMovie.setVisibility(View.VISIBLE);
        MenuItem tab = toolbar.getMenu().getItem(0);
        SpannableString spanString = new SpannableString(tab.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0);
        tab.setTitle(spanString);
        tab = toolbar.getMenu().getItem(1);
        spanString = new SpannableString(tab.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#205db3")), 0, spanString.length(), 0);
        tab.setTitle(spanString);
    }

    private void getTVData() {
        sliderMovie.setVisibility(View.GONE);
        txtTopRatedMovie.setVisibility(View.GONE);
        recyclerViewTopRatedMovie.setVisibility(View.GONE);
        txtPopularMovie.setVisibility(View.GONE);
        recyclerViewPopularMovie.setVisibility(View.GONE);
        footerTMDBMovie.setVisibility(View.GONE);
        footerDevMovie.setVisibility(View.GONE);
        sliderTV.setVisibility(View.VISIBLE);
        txtTopRatedTV.setVisibility(View.VISIBLE);
        recyclerViewTopRatedTV.setVisibility(View.VISIBLE);
        txtPopularTV.setVisibility(View.VISIBLE);
        recyclerViewPopularTV.setVisibility(View.VISIBLE);
        sliderTV.setVisibility(View.VISIBLE);
        txtTopRatedTV.setVisibility(View.VISIBLE);
        footerTMDBTV.setVisibility(View.VISIBLE);
        footerDevTV.setVisibility(View.VISIBLE);
        MenuItem tab = toolbar.getMenu().getItem(1);
        SpannableString spanString = new SpannableString(tab.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0);
        tab.setTitle(spanString);
        tab = toolbar.getMenu().getItem(0);
        spanString = new SpannableString(tab.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#205db3")), 0, spanString.length(), 0);
        tab.setTitle(spanString);
    }

    private void jsonParseSlider(String url, SliderView slider) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            initializeSlider(jsonArray, slider);
                            requestPending();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void jsonParseScroll(String url, ArrayList<RecyclerCardData> cardDataList) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                cardDataList.add(new RecyclerCardData(obj));
                            }
                            requestPending();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            sv.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    private void initializeSlider(JSONArray jsonArray, SliderView sliderView) {
        // we are creating array list for storing our image urls.
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        //SliderView sliderView = getView().findViewById(R.id.slider);

        // adding the urls inside array list
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = null;
            try {
                obj = jsonArray.getJSONObject(i);
                sliderDataArrayList.add(new SliderData(obj));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(getActivity().getApplicationContext(), sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }
}
