package com.example.cs571hw9moviesapp;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WatchlistRecyclerAdapter extends RecyclerView.Adapter<WatchlistRecyclerAdapter.MyViewHolder> {
    private ArrayList<RecyclerCardData> cardDataList;
    private WatchlistRecyclerAdapter.RecyclerViewClickListener listener;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    Map<String, String> watchlistElements;
    String prefData;
    String prefValue;
    String[] prefValueItems;
    TextView watchlistMessage;

    public WatchlistRecyclerAdapter(ArrayList<RecyclerCardData> cardDataList, TextView watchlistMessage,
                                    WatchlistRecyclerAdapter.RecyclerViewClickListener listener) {
        this.cardDataList = cardDataList;
        this.watchlistMessage = watchlistMessage;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView watchlistCardImg;
        private TextView watchlistCardType;
        private ImageView watchlistButton;

        public MyViewHolder(final View view) {
            super(view);
            watchlistCardImg = view.findViewById(R.id.watchlist_card_img);
            watchlistCardType = view.findViewById(R.id.watchlist_card_type);
            watchlistButton = view.findViewById(R.id.watchlist_button);
            watchlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefValue = cardDataList.get(getAdapterPosition()).getType() + "-" + cardDataList.get(getAdapterPosition()).getID() +
                            "-" + cardDataList.get(getAdapterPosition()).getTitle() + "-" + cardDataList.get(getAdapterPosition()).getImgUrl();
                    removeFromWatchlist(getAdapterPosition());
                    Toast.makeText(v.getContext(), "\"" + cardDataList.get(getAdapterPosition()).getTitle() + "\""
                            + " was removed from favourites", Toast.LENGTH_SHORT).show();
                    cardDataList.remove(getAdapterPosition());
                    if (cardDataList.size() == 0) {
                        watchlistMessage.setVisibility(view.VISIBLE);
                    }
                    notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();
                    watchlistElements = (Map<String, String>) pref.getAll();
                    prefData = watchlistElements.getOrDefault("items", "");
                    if (prefData.length() != 0) {
                        prefValueItems = prefData.split(";");
                    }
                }
            });
            pref = view.getContext().getSharedPreferences("Watchlist", 0);
            prefEditor = pref.edit();
            watchlistElements = (Map<String, String>) pref.getAll();
            prefData = watchlistElements.getOrDefault("items", "");
            if (prefData.length() != 0) {
                prefValueItems = prefData.split(";");
            }
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public WatchlistRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_items_watchlist, parent, false);
        return new WatchlistRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistRecyclerAdapter.MyViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(cardDataList.get(position).getImgUrl())
                .fitCenter()
                .into(holder.watchlistCardImg);
        holder.watchlistCardType.setText(cardDataList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    private void removeFromWatchlist(int position) {
        watchlistElements = (Map<String, String>) pref.getAll();
        prefData = watchlistElements.getOrDefault("items", "");
        if (prefData.length() != 0) {
            prefValueItems = prefData.split(";");
        }
        StringBuilder newPrefData = new StringBuilder();
        if (prefValueItems.length > 1) {
            for (int i = 0; i < prefValueItems.length; i++) {
                if (i != position) {
                    newPrefData.append(prefValueItems[i] + ";");
                }
            }
        }
        prefEditor.putString("items", newPrefData.toString());
        prefEditor.commit();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
