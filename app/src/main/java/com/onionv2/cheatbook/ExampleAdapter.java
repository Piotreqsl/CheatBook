package com.onionv2.cheatbook;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onionv2.cheatbook.R;

import java.util.ArrayList;

import javax.security.auth.Subject;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<String> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDotsClick(int position, View v);

        void onItemClick(int position);


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton dots;
        TextView subjectText, countText;


        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iconSingle);
            subjectText = itemView.findViewById(R.id.textSingle);
            countText = itemView.findViewById(R.id.subtextSingle);
            dots = itemView.findViewById(R.id.menu_vertical_home);

            dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDotsClick(position, dots);

                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });



        }
    }

    public ExampleAdapter(ArrayList<String> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_folder, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(view, mListener);
        return evh;
    }


    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        //holder.imageView.setImageURI(); Obrazki jebnac customowe
        String[] parts = mExampleList.get(position).split("__,__");
        String subject = parts[0];

        holder.subjectText.setText(subject);
        holder.countText.setText(parts[1] + " items");




        if (subject.equals("Art")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1wgRIjXhWxpDfZh0B65aIZaN7vSpEb7Sq" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }


        else if (subject.equals("Astronomy")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1Rg1CGgzbfprpYzWQFzY1SMfqDGAUoxM6" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Biology")) {
            Glide
                    .with(holder.countText.getContext())

                    .load("https://drive.google.com/uc?id=1CsbaNhNmePdSlDxWCATf21Z8I5yW_m17" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Business studies")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1WPjxy09hjDANRZZXIrCBpj1D7NJfbxKr" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Chemistry")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1OcXA7MSlYqaH38SclCLevM2I6Qo9-cGI" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Citizenship")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1I7HEXVGC83R1KBXtq5ecz9gmRWdadHNQ" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Geography")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=18dySJbkIcL_aZTIIofy6Nxbu4Ns7MquT" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("History")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1pSOZUpgLCeZp9PmEI7zyxkZ8RGQrZk3T" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Computer Science")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1xreYBoa3q774hldTV9dgHLfifNgL3277" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Law")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1XQqNbX4Kl9RqS0Mou2QS9RdrWghdgteE" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Literature")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=10DtdjymWEV32Q1-iratUdsxg6X-IF4Yb" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Maths")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1xC4E-utzFqsm32j4QlfqN6Ptlt4FM8U0" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Music")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1OpYGp31FWCuW7aTPbJG9d5IQH8WL_ZIu" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Physics")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=13aW88nY7ngLuErN2azGKAgWJkX-vnVEA" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Religion")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1KEKx1LZAc1y5KQvB0zTd_glONydafR3W" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Polish")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1PiY3w73Y5QJJZWC6mXRiHPX3OR6bSafZ" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("English")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1qkgp-8WL70RRK4ucGCLgLYRaSzCJxx3S" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Spanish")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1pg84RvqJnVImHp1rfq1VFVRAAAQSYYtO" )

                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Italian")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=183pXtdd3MI3JOAm4YAACBsczSqRU0fOP" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Norwegian")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1Pnl2dmFK1ITDZ-v7FdfPH3V7mu8_SJNO" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("Russian")) {
            Glide
                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1f15aY30wjU_9Coc3ErfHzGc2gLfm04nL" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else if (subject.equals("German")) {
            Glide

                    .with(holder.countText.getContext())
                    .load("https://drive.google.com/uc?id=1Y7cItjKfwAZJaxoQt7sZ3K1knbyTZlX3" )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        else {
            Glide
                    .with(holder.countText.getContext())
                    .load(R.drawable.placeholder )
                    .into(holder.imageView);
        }




    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void removeItem(int position) {
        mExampleList.remove(position);
        notifyItemRemoved(position);
    }





    public String getSubjectByPos(int i) {return mExampleList.get(i).split("__,__")[0];}

}