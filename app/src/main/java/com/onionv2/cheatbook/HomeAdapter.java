package com.onionv2.cheatbook;

import android.content.Context;
import android.icu.text.CaseMap;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.FolderView> {


    ArrayList<String> subjects;

    public HomeAdapter(ArrayList<String> str){
        this.subjects = str;
    }


    public class FolderView extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView subjectText, countText;

        public FolderView(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iconSingle);
            subjectText = itemView.findViewById(R.id.textSingle);
            countText = itemView.findViewById(R.id.subtextSingle);
        }
    }

    @NonNull
    @Override
    public FolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_folder, parent, false);
        return new FolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderView holder, int position) {
        //holder.imageView.setImageURI(); Obrazki jebnac customowe
        String[] parts = subjects.get(position).split("__,__");
        holder.subjectText.setText(parts[0]);
        holder.countText.setText(parts[1] + " items");



    }

    @Override
    public int getItemCount() {
        return subjects.size();
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
