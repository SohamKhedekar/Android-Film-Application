package com.example.cs571hw9moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<RecyclerCardData> cardDataList;
    ImageView scrollViewButton;
    PopupMenu popupMenu;
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    Map<String, String> watchlistElements;
    String prefData;
    String prefValue;
    String[] prefValueItems;
    private RecyclerViewClickListener listener;

    public RecyclerAdapter(ArrayList<RecyclerCardData> cardDataList, RecyclerViewClickListener listener) {
        this.cardDataList = cardDataList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView cardImg;
        String tmdbUrl;

        public MyViewHolder(final View view) {
            super(view);
            cardImg = view.findViewById(R.id.card_img);
            pref = view.getContext().getSharedPreferences("Watchlist", 0);
            prefEditor = pref.edit();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_items_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(cardDataList.get(position).getImgUrl())
                .fitCenter()
                .into(holder.cardImg);
        scrollViewButton = holder.itemView.findViewById(R.id.scroll_view_button);
        scrollViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = new ContextThemeWrapper(holder.itemView.getContext(), R.style.PopupMenu);
                popupMenu = new PopupMenu(context, holder.itemView);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                //Get watchlist data
                prefValue = cardDataList.get(position).getType() + "-" + cardDataList.get(position).getID() +
                        "-" + cardDataList.get(position).getTitle() + "-" + cardDataList.get(position).getImgUrl();
                watchlistElements = (Map<String, String>) pref.getAll();
                if (ifWatchlistContains()) {
                    popupMenu.getMenu().getItem(4).setVisible(true);
                    popupMenu.getMenu().getItem(3).setVisible(false);
                }
                else {
                    popupMenu.getMenu().getItem(4).setVisible(false);
                    popupMenu.getMenu().getItem(3).setVisible(true);
                }
                //Set on click
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent browserIntent;
                        switch (menuItem.getItemId()) {
                            case R.id.tmdb_pop_up_item:
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cardDataList.get(position).getTMDBUrl()));
                                context.startActivity(browserIntent);
                                break;
                            case R.id.fb_pop_up_item:
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + cardDataList.get(position).getTMDBUrl()));
                                context.startActivity(browserIntent);
                                break;
                            case R.id.twitter_pop_up_item:
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check this out!&url=" + cardDataList.get(position).getTMDBUrl()));
                                context.startActivity(browserIntent);
                                break;
                            case R.id.watchlist_pop_up_item:
                                addToWatchlist();
                                Toast.makeText(holder.itemView.getContext(), cardDataList.get(position).getTitle() + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.remove_watchlist_pop_up_item:
                                removeFromWatchlist();
                                Toast.makeText(holder.itemView.getContext(), cardDataList.get(position).getTitle() + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
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

    private void addToWatchlist() {
        prefData += prefValue + ";";
        prefEditor.putString("items", prefData);
        prefEditor.commit();
    }

    private void removeFromWatchlist() {
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
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
