package com.onionv2.cheatbook;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.List;
import java.util.Objects;


public class CheatView extends RelativeLayout {

    private PhotoView image;
    private List<Uri> images;
    private int position;
    private Context cont;
    TextView posText;



    public CheatView(Context context) {
        this(context,null);
    }

    public CheatView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CheatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.cont = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.cheat_view, this);
        image = findViewById(R.id.photo_view);
        posText = findViewById(R.id.position);


    }

    public void setup(List<Uri> imgs, int pos){
        this.images = imgs;
        this.position = pos;

        posText.setGravity(Gravity.CENTER_HORIZONTAL);
        posText.setText(pos +1 + "/" + images.size());

        Glide.with(getContext()).load(new File(images.get(pos).getPath()))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);

    }

    public void loadNext(){
        int length = images.size();
        if (position == length - 1) position=0;
        else position++;

        posText.setText(position +1 + "/" + images.size());

        Glide.with(getContext()).load(new File(images.get(position).getPath()))
                .thumbnail(0.5f)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
    }

    public void loadPrevious(){
        int length = images.size();
        if (position == 0) position= length -1;
        else position--;

        posText.setText(position +1 + "/" + images.size());

        Glide.with(getContext()).load(new File(images.get(position).getPath()))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);

    }




}
