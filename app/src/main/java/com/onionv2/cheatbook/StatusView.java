package com.onionv2.cheatbook;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;


public class StatusView extends RelativeLayout {

    private TextView status;
    private String previousText = "Nothing detected";
    private boolean counting = false;
    Handler handler = new Handler();


    public StatusView(Context context) {
        this(context,null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.status_view, this);

        status = findViewById(R.id.statusText);
        status.setVisibility(GONE);
        status.setAlpha(0);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                ViewCompat.animate(status).alpha(1).setDuration(800);
                return false;
            }
        });
    }


    public void setup(){
        status.setVisibility(VISIBLE);
        status.setText("Nothing detected");
        status.setBackgroundColor(Color.parseColor("#B22222"));
    }



    // Sprawdzanie delaya oraz wywłanie zmiany obrazka
    //TODO Change Image on main activity somehow
    private void startCounting(final String eye){
        counting = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setStatus(eye, "#d3d3d3");
                counting = false;
                Log.d("zmiana ###", eye);
            }
        }, 1000);

    }

    private void stopCoutning(){
        handler.removeCallbacksAndMessages(null);
    }


    public void setStatus(String text, String hex){

        //moment zamknięcia lewego oka
        if(previousText == "Both open" && text == "Right open"){
            startCounting(text);
        }
        if(previousText == "Right open" && text != "Right open" && counting){
            stopCoutning();
        }

        //moment zamkniecia prawego oka
        if(previousText == "Both open" && text == "Left open"){
            startCounting(text);
        }
        if(previousText == "Left open" && text != "Left open" && counting){
            stopCoutning();
        }






        status.setText(text);
        status.setBackgroundColor(Color.parseColor(hex));

        previousText = text;

    }
}
