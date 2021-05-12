package com.example.cs571hw9moviesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecRecyclerAdapter extends RecyclerView.Adapter<RecRecyclerAdapter.MyViewHolder> {
    private ArrayList<RecyclerCardData> recRecyclerData;
    private RecyclerViewClickListener recListener;

    public RecRecyclerAdapter(ArrayList<RecyclerCardData> recRecyclerData, RecRecyclerAdapter.RecyclerViewClickListener recListener) {
        this.recRecyclerData = recRecyclerData;
        this.recListener = recListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView recCardImg;

        public MyViewHolder(final View view) {
            super(view);
            recCardImg = view.findViewById(R.id.rec_card_img);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recListener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_items_rec, parent, false);
        return new RecRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecRecyclerAdapter.MyViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(recRecyclerData.get(position).getImgUrl())
                .fitCenter()
                .into(holder.recCardImg);
    }

    @Override
    public int getItemCount() {
        return recRecyclerData.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
