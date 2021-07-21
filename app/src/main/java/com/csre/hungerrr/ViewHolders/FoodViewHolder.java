package com.csre.hungerrr.ViewHolders;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.csre.hungerrr.Interface.ItemClickListener;


public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;


    public FoodViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onclick(view, getAdapterPosition(), false);
    }
}
