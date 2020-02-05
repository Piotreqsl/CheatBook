package com.onionv2.cheatbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cheat_step_two);

        Bundle bundle = getIntent().getExtras();
        subject = bundle.getString("subj");
        title = bundle.getString("title");

        Log.d("tagg", subject + title + "hej ");

        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        final SubjectHelper subjectHelper = new SubjectHelper(this);



        fab = findViewById(R.id.addFabCreateImage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });




        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Toast.makeText(getApplicationContext(), position + "hej", Toast.LENGTH_SHORT).show();

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
                    Uri uri = getImagePath(item.getUri());

                    images.add(uri);
                }

            }else if (data.getData() != null){

                images.add(getImagePath(data.getData()));
            }

            mAdapter.notifyDataSetChanged();

        }

    }



    private Uri getImagePath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return Uri.parse(path);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
