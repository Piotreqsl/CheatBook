package com.onionv2.cheatbook;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Listener for manual initiation of a drag.
 */
public interface OnStartDragListener {


    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}