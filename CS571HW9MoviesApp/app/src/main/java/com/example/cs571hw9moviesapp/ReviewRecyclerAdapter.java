package com.example.cs571hw9moviesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewRecyclerAdapter  extends RecyclerView.Adapter<ReviewRecyclerAdapter.MyViewHolder> {
    private ArrayList<ReviewRecyclerData> reviewRecyclerData;
    private ReviewRecyclerAdapter.RecyclerViewClickListener listener;

    public ReviewRecyclerAdapter(ArrayList<ReviewRecyclerData> reviewRecyclerData, ReviewRecyclerAdapter.RecyclerViewClickListener listener) {
        this.reviewRecyclerData = reviewRecyclerData;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reviewCardTitle;
        TextView reviewCardRating;
        TextView reviewCardContent;

        public MyViewHolder(final View view) {
            super(view);
            reviewCardTitle = view.findViewById(R.id.review_card_title);
            reviewCardRating = view.findViewById(R.id.review_card_rating);
            reviewCardContent = view.findViewById(R.id.review_card_content);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ReviewRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_items_review, parent, false);
        return new ReviewRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerAdapter.MyViewHolder holder, int position) {
        holder.reviewCardTitle.setText("by " + reviewRecyclerData.get(position).getAuthor() + " on "
                + reviewRecyclerData.get(position).getDate());
        holder.reviewCardRating.setText(reviewRecyclerData.get(position).getRating() + "/5");
        holder.reviewCardContent.setText(reviewRecyclerData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewRecyclerData.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
