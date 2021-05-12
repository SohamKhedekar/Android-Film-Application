package com.example.cs571hw9moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WatchlistFragment extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    Map<String, String> watchlistElements;
    String prefData;
    String prefValue;
    String[] prefValueItems;
    private RecyclerView recyclerView;
    ArrayList<RecyclerCardData> recyclerCardData;
    TextView watchlistMessage;
    private WatchlistRecyclerAdapter.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        watchlistMessage = view.findViewById(R.id.watchlist_message);
        recyclerView = view.findViewById(R.id.recycler_watchlist);
        recyclerCardData = new ArrayList<>();
        pref = view.getContext().getSharedPreferences("Watchlist", 0);
        prefEditor = pref.edit();
        watchlistElements = (Map<String, String>) pref.getAll();
        prefData = watchlistElements.getOrDefault("items", "");
        if (prefData.length() != 0) {
            watchlistMessage.setVisibility(View.GONE);
            prefValueItems = prefData.split(";");
            for (int i = 0; i < prefValueItems.length; i++) {
                String[] values = prefValueItems[i].split("-");
                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", values[0]);
                    obj.put("id", values[1]);
                    obj.put("title", values[2]);
                    obj.put("poster_path", values[3]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerCardData.add(new RecyclerCardData(obj));
            }
            setAdapter();
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        else {
            watchlistMessage.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerCardData = new ArrayList<>();
        pref = getView().getContext().getSharedPreferences("Watchlist", 0);
        prefEditor = pref.edit();
        watchlistElements = (Map<String, String>) pref.getAll();
        prefData = watchlistElements.getOrDefault("items", "");
        if (prefData.length() != 0) {
            watchlistMessage.setVisibility(View.GONE);
            prefValueItems = prefData.split(";");
            for (int i = 0; i < prefValueItems.length; i++) {
                String[] values = prefValueItems[i].split("-");
                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", values[0]);
                    obj.put("id", values[1]);
                    obj.put("title", values[2]);
                    obj.put("poster_path", values[3]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerCardData.add(new RecyclerCardData(obj));
            }
            setAdapter();
        }
        else {
            watchlistMessage.setVisibility(View.VISIBLE);
            setAdapter();
        }
    }

    private void setAdapter() {
        setOnClickListener(recyclerCardData);
        WatchlistRecyclerAdapter adapter = new WatchlistRecyclerAdapter(recyclerCardData, watchlistMessage, listener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setOnClickListener(ArrayList<RecyclerCardData> recyclerCardData) {
        listener = new WatchlistRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("type", recyclerCardData.get(position).getType());
                intent.putExtra("id", recyclerCardData.get(position).getID());
                intent.putExtra("title", recyclerCardData.get(position).getTitle());
                startActivity(intent);
            }
        };
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
            | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(recyclerCardData, i, i + 1);
                    recyclerView.getAdapter().notifyItemMoved(i, i+1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(recyclerCardData, i, i - 1);
                    recyclerView.getAdapter().notifyItemMoved(i, i-1);
                }
            }
            swapWatchlistItems(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    private void swapWatchlistItems(int fromPosition, int toPosition) {
        watchlistElements = (Map<String, String>) pref.getAll();
        prefData = watchlistElements.getOrDefault("items", "");
        if (prefData.length() != 0) {
            prefValueItems = prefData.split(";");
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                String temp = prefValueItems[i];
                prefValueItems[i] = prefValueItems[i+1];
                prefValueItems[i+1] = temp;
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                String temp = prefValueItems[i];
                prefValueItems[i] = prefValueItems[i-1];
                prefValueItems[i-1] = temp;
            }
        }
        StringBuilder newPrefData = new StringBuilder();
        for (String item:prefValueItems) {
            newPrefData.append(item + ";");
        }
        prefEditor.putString("items", newPrefData.toString());
        prefEditor.commit();
    }
}
