package com.onionv2.cheatbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends AppCompatActivity {

    Button grid;
    final int callbackPerm = 2;


    RecyclerView recyclerHome;
    ArrayList<String> listItem;
    ArrayList<String> helperListItem;
    SubjectHelper subjectHelper;
    SearchView searchView;

    private ExampleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listItem = new ArrayList<String>();
        helperListItem = new ArrayList<String>();
        subjectHelper = new SubjectHelper(this);
        searchView = findViewById(R.id.search_bar);






        Cursor cursor = subjectHelper.getData();
        while(cursor.moveToNext()){
            listItem.add(cursor.getString(1) + "__,__" + cursor.getInt(2));
            helperListItem.add(cursor.getString(1) + "__,__" + cursor.getInt(2));
        }







        // tego nie tykaj
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    callbackPerm);
        }



        recyclerHome = findViewById(R.id.recycler_home);
        mAdapter = new ExampleAdapter(listItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerHome.setLayoutManager(gridLayoutManager);
        recyclerHome.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, SubjectCheats.class);
                intent.putExtra("subj", listItem.get(position).split("__,__")[0]);
                startActivity(intent);
            }

            @Override
            public void onDotsClick(int position) {
                Toast.makeText(getApplicationContext(), "hejh", Toast.LENGTH_SHORT).show();
                Log.d("Hej", "hejejsd");


            }
        });





        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    listItem.clear();
                    listItem.addAll(helperListItem);
                    mAdapter.notifyDataSetChanged();
                }

                else {
                    Collection<String> filtered = Collections2.filter(helperListItem,
                            Predicates.containsPattern("(?i)" + s));
                    listItem.clear();
                    listItem.addAll(filtered);
                    mAdapter.notifyDataSetChanged();
                }


                return true;
            }
        });

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
