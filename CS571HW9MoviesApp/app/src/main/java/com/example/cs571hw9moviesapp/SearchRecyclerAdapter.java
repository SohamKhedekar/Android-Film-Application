package com.example.cs571hw9moviesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> {
    private ArrayList<SearchRecyclerCardData> searchRecyclerCardData;
    private SearchRecyclerAdapter.RecyclerViewClickListener listener;

    public SearchRecyclerAdapter(ArrayList<SearchRecyclerCardData> searchRecyclerCardData, SearchRecyclerAdapter.RecyclerViewClickListener listener) {
        this.searchRecyclerCardData = searchRecyclerCardData;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView searchCardImg;
        private TextView searchCardType;
        private TextView searchCardTitle;
        private ImageView searchCardRatingStar;
        private TextView searchCardRating;

        public MyViewHolder(final View view) {
            super(view);
            searchCardImg = view.findViewById(R.id.search_card_img);
            searchCardType = view.findViewById(R.id.search_card_type);
            searchCardTitle = view.findViewById(R.id.search_card_title);
            searchCardRatingStar = view.findViewById(R.id.search_card_rating_star);
            searchCardRating = view.findViewById(R.id.search_card_rating);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public SearchRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_items_search, parent, false);
        return new SearchRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerAdapter.MyViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(searchRecyclerCardData.get(position).getImgUrl())
                .fitCenter()
                .into(holder.searchCardImg);
        holder.searchCardType.setText(searchRecyclerCardData.get(position).getTypeWithReleaseDate());
        if (searchRecyclerCardData.get(position).getRating().equals("null")) {
            holder.searchCardRatingStar.setVisibility(holder.itemView.GONE);
        }
        else {
            holder.searchCardRatingStar.setVisibility(holder.itemView.VISIBLE);
            holder.searchCardRating.setText(searchRecyclerCardData.get(position).getRating());
        }
        holder.searchCardTitle.setText(searchRecyclerCardData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return searchRecyclerCardData.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
