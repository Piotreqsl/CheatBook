package com.onionv2.cheatbook;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectCheatsAdapter extends RecyclerView.Adapter<SubjectCheatsAdapter.MyViewHolder> {

    private Context context;
    private List<Cheat> cheats;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Title, Date, numOfImgs;

        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            Title = view.findViewById(R.id.CheatTitle);
            Date = view.findViewById(R.id.CheatDate);
            numOfImgs = view.findViewById(R.id.numOfImgs);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public SubjectCheatsAdapter(Context context, List<Cheat> cartList) {
        this.context = context;
        this.cheats = cartList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_cheat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Cheat cheat = cheats.get(position);

        holder.Title.setText(cheat.getTitle());
        holder.Date.setText(cheat.getTimestamp().substring(0,10));
        holder.numOfImgs.setText(cheat.getUris().split("__,__").length + " items");

    }

    @Override
    public int getItemCount() {
        return cheats.size();
    }

    public void removeItem(int position) {
        cheats.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cheat item, int position) {
        cheats.add(position, item);


        notifyItemInserted(position);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}