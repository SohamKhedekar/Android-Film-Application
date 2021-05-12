package com.example.cs571hw9moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    SearchView searchView;
    private RecyclerView recyclerView;
    TextView defaultSearchResultTxt;
    RequestQueue queue;
    private SearchRecyclerAdapter.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search_view);
        recyclerView = view.findViewById(R.id.recycler_search);
        defaultSearchResultTxt = view.findViewById(R.id.default_search_result_text);
        defaultSearchResultTxt.setVisibility(view.GONE);
        searchView.setQueryHint("Search movies and TV");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(onQueryTextListener);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        return view;
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (newText.length() != 0) {
                ArrayList<SearchRecyclerCardData> searchRecyclerCardData = new ArrayList<>();
                getSearchResults(newText, searchRecyclerCardData);
            }
            return false;
        }
    };

    private void getSearchResults(String sQuery, ArrayList<SearchRecyclerCardData> searchRecyclerCardData) {
        String url = "https://sk-wt-hws-9.wl.r.appspot.com/apis/search/?search_query=" + sQuery;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                searchRecyclerCardData.add(new SearchRecyclerCardData(obj));
                            }
                            if (searchRecyclerCardData.size() == 0) {
                                defaultSearchResultTxt.setVisibility(getView().VISIBLE);
                            }
                            else {
                                defaultSearchResultTxt.setVisibility(getView().GONE);
                            }
                            setAdapter(searchRecyclerCardData, recyclerView);
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        queue.add(jsonArrayRequest);
    }

    private void setAdapter(ArrayList<SearchRecyclerCardData> searchRecyclerCardData, RecyclerView recyclerView) {
        setOnClickListener(searchRecyclerCardData);
        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(searchRecyclerCardData, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setOnClickListener(ArrayList<SearchRecyclerCardData> searchRecyclerCardData) {
        listener = new SearchRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("type", searchRecyclerCardData.get(position).getType());
                intent.putExtra("id", searchRecyclerCardData.get(position).getID());
                intent.putExtra("title", searchRecyclerCardData.get(position).getTitle());
                startActivity(intent);
            }
        };
    }
}
