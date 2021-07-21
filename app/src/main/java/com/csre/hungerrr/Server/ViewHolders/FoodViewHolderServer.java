package com.csre.hungerrr.Server.ViewHolders;


import android.view.ContextMenu;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.csre.hungerrr.Common.Common;
import com.csre.hungerrr.Interface.ItemClickListener;


public class FoodViewHolderServer extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    private ItemClickListener itemClickListener;


    public FoodViewHolderServer(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onclick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select action");
        contextMenu.add(0, 0, getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0, 1, getAdapterPosition(), Common.DELETE);
    }
}
