package com.onionv2.cheatbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CreateCheatStepTwo extends AppCompatActivity implements OnStartDragListener {
    private static final int PICK_IMAGES = 2;
    FloatingActionButton fab;
    FloatingActionButton confirm;


    private ArrayList<Uri> images;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    private String subject;
    private String title;


    private ItemTouchHelper mItemTouchHelper;

    InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9541882283543234/3900836273");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cheat_step_two);

        Bundle bundle = getIntent().getExtras();
        subject = bundle.getString("subj");
        title = bundle.getString("title");

        Log.d("tagg", subject + title + "hej");

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        final SubjectHelper subjectHelper = new SubjectHelper(this);

        AppCompatTextView appCompatTextView = findViewById(R.id.headerText);
        appCompatTextView.setText("Select images");



        fab = findViewById(R.id.addFabCreateImage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        recyclerView =  findViewById(R.id.recycler_view);


        images = new ArrayList<>();
        recyclerView.setHasFixedSize(true);

        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        confirm = findViewById(R.id.fabConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tagg", String.valueOf(images));
                if(images.size() >1){

                    Intent intent = new Intent("finish");
                    sendBroadcast(intent);



                    StringBuilder stringBuilder = new StringBuilder();

                    for(int i =0; i < mAdapter.images.size(); i++){
                        stringBuilder.append(mAdapter.images.get(i) + "__,__");

                    }



                    String uris = stringBuilder.toString();

                    Log.d("tagg", String.valueOf(uris));

                    subjectHelper.incrementSubject(subject);
                    databaseHelper.insertCheat(subject, title, uris);

                    SubjectCheats.cheatList.clear();
                    SubjectCheats.getCheats();


                    if(mInterstitialAd.isLoaded()) mInterstitialAd.show();

                    finish();


                }
                else{
                    Snackbar snackbar = Snackbar
                            .make((CoordinatorLayout) findViewById(R.id.coord),  "You must add at lest 2 images!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    //Opens image picker
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGES);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGES){


            ClipData clipData = data.getClipData();


            if(clipData != null) {

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = Uri.parse(getPath(getApplicationContext(),item.getUri()));

                    images.add(uri);
                }

            }else if (data.getData() != null){

                images.add(Uri.parse(getPath(getApplicationContext() ,data.getData())));
            }

            mAdapter.notifyDataSetChanged();

        }

    }



    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Log.i("URI",uri+"");
        String result = uri+"";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {
            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length-1];
            final String[] dat = imgary.split("%3A");
            final String docId = dat[1];
            final String type = dat[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
            } else if ("audio".equals(type)) {
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {
                    dat[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }












    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
