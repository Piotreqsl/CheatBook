package com.onionv2.cheatbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button grid;
    final int callbackPerm = 2;


    RecyclerView recyclerHome;
    ArrayList<String> listItem;
    SubjectHelper subjectHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listItem = new ArrayList<String>();
        subjectHelper = new SubjectHelper(this);





        Cursor cursor = subjectHelper.getData();
        while(cursor.moveToNext()){
            listItem.add(cursor.getString(1) + "__,__" + cursor.getInt(2));
        }







        // tego nie tykaj
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    callbackPerm);
        }



        recyclerHome = findViewById(R.id.recycler_home);




        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerHome.setLayoutManager(gridLayoutManager);

        recyclerHome.setAdapter(new HomeAdapter(listItem));

        recyclerHome.addOnItemTouchListener(new HomeAdapter.RecyclerTouchListener(getApplicationContext(), recyclerHome, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, SubjectCheats.class);
                intent.putExtra("subj", listItem.get(position).split("__,__")[0]);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        //debug only

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case callbackPerm: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
