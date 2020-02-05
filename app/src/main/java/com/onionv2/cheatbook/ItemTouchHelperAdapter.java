package com.onionv2.cheatbook;

import androidx.recyclerview.widget.RecyclerView;


public interface ItemTouchHelperAdapter {



    void onItemMove(int fromPosition, int toPosition);


    void onItemDismiss(int position);
}