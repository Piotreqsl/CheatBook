package com.onionv2.cheatbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class GridActivity extends AppCompatActivity implements OnStartDragListener {
    private static final int PICK_IMAGES = 2;
    FloatingActionButton fab;


    private ArrayList<Uri> images;
    private String subject;
    private String title;
    private String timestamp;
    private int id;

    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    private ItemTouchHelper mItemTouchHelper;


    InterstitialAd mInterstitialAd;
    AdView mAdView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        images = new ArrayList<>();

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();
        subject = bundle.getString("subj");
        title = bundle.getString("title");
        id = bundle.getInt("id");
        timestamp = bundle.getString("timestamp");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        //Removing not valid imgs
        int counter = 0;
        for(int i = 0; i < bundle.getString("uris").split("__,__").length; i++ ){
            File file = new File(bundle.getString("uris").split("__,__")[i]);

            if(file.exists()){
            images.add(Uri.parse(bundle.getString("uris").split("__,__")[i]));
            }
            else{
                counter++;
            }
        }
        if(counter != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for(int i =0; i < images.size(); i++){
                stringBuilder.append(images.get(i) + "__,__");
            }


            Cheat fixedCheat = new Cheat();
            fixedCheat.setTitle(title);
            fixedCheat.setUris(stringBuilder.toString());
            fixedCheat.setSubject(subject);
            fixedCheat.setId(id);
            fixedCheat.setTimestamp(timestamp);

            databaseHelper.updateCheat(fixedCheat);

        }


        AppCompatTextView appCompatTextView = findViewById(R.id.headerText);
        appCompatTextView.setText(subject + ": " + title);




        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9541882283543234/3900836273");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        recyclerView =  findViewById(R.id.recycler_view);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);




        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {


            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {


                StringBuilder stringBuilder = new StringBuilder();
                for(int i =0; i < images.size(); i++){
                    stringBuilder.append(images.get(i) + "__,__");
                }
                Cheat fixedCheat = new Cheat();
                fixedCheat.setTitle(title);
                fixedCheat.setUris(stringBuilder.toString());
                fixedCheat.setSubject(subject);
                fixedCheat.setId(id);
                fixedCheat.setTimestamp(timestamp);

                databaseHelper.updateCheat(fixedCheat);
            }
        });

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);



                Intent intent = new Intent(GridActivity.this, CameraActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                if(mInterstitialAd.isLoaded()) mInterstitialAd.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
