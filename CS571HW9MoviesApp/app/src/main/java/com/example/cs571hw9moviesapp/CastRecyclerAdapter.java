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

public class CastRecyclerAdapter extends RecyclerView.Adapter<CastRecyclerAdapter.MyViewHolder> {
    private ArrayList<CastRecyclerData> cardDataList;

    public CastRecyclerAdapter(ArrayList<CastRecyclerData> cardDataList) {
        this.cardDataList = cardDataList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView castCardImg;
        private TextView castCardTxt;

        public MyViewHolder(final View view) {
            super(view);
            castCardImg = view.findViewById(R.id.cast_card_img);
            castCardTxt = view.findViewById(R.id.cast_card_txt);
        }
    }

    @NonNull
    @Override
    public CastRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_items_cast, parent, false);
        return new CastRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CastRecyclerAdapter.MyViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(cardDataList.get(position).getImgUrl())
                .fitCenter()
                .into(holder.castCardImg);
        holder.castCardTxt.setText(cardDataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }
}
