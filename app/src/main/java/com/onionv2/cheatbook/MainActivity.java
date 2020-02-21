package com.onionv2.cheatbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends AppCompatActivity {


    final int callbackPerm = 2;


    RecyclerView recyclerHome;
    ArrayList<String> listItem;
    ArrayList<String> helperListItem;
    SubjectHelper subjectHelper;
    DatabaseHelper databaseHelper;

    SearchView searchView;
    CoordinatorLayout coordinatorLayout;

    FloatingActionButton addFab;

    private ExampleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listItem = new ArrayList<String>();
        helperListItem = new ArrayList<String>();
        subjectHelper = new SubjectHelper(this);
        databaseHelper = new DatabaseHelper(this);
        searchView = findViewById(R.id.search_bar);

        coordinatorLayout = findViewById(R.id.coordinator_home);
        addFab = findViewById(R.id.floatingActionButtonMAin);






        Cursor cursor = subjectHelper.getData();
        while(cursor.moveToNext()){
            listItem.add(cursor.getString(1) + "__,__" + cursor.getInt(2));
            helperListItem.add(cursor.getString(1) + "__,__" + cursor.getInt(2));
        }

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });






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
            public void onDotsClick(final int position, View v) {

                    if(helperListItem.size() == mAdapter.getItemCount()) {

                        PopupMenu popup = new PopupMenu(MainActivity.this, v);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(MainActivity.this)
                                        .setTitle("Delete?")
                                        .setMessage("Are you sure want to delete this file?")
                                        .setCancelable(false)
                                        .setPositiveButton("Delete", R.drawable.ic_delete, new BottomSheetMaterialDialog.OnClickListener() {
                                            @Override
                                            ///// TUTAJ JEST DELETE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                            public void onClick(DialogInterface dialogInterface, int which) {

                                                databaseHelper.deleteCheatBySubject(mAdapter.getSubjectByPos(position));
                                                subjectHelper.deleteSubject(mAdapter.getSubjectByPos(position));

                                                String test = mAdapter.getSubjectByPos(position);
                                                mAdapter.removeItem(position);

                                                listItem.remove(test);
                                                helperListItem.remove(test);


                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setNegativeButton("Cancel", R.drawable.ic_close, new BottomSheetMaterialDialog.OnClickListener() {
                                            @Override // tutaj jest cancel
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .build();

                                // Show Dialog
                                mBottomSheetDialog.show();


                                return true;
                            }
                        });


                        popup.show();
                    }
            }
        });


        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);

                final EditText edittext = new EditText(MainActivity.this);
                alert.setMessage("No more than 20 characters");
                alert.setTitle("Select name");

                alert.setView(edittext);

                alert.setPositiveButton("Create", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {

                        String subject = edittext.getText().toString();

                        if(subject.length() > 0 ){// Dodanie czita

                            subjectHelper.addSubject(subject);
                            listItem.add(subject + "__,__0");
                            helperListItem.add(subject + "__,__0");

                            mAdapter.notifyItemInserted(mAdapter.getItemCount()+ 1);


                        }

                    }
                });

                alert.setNegativeButton("Back", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {



                    }
                });

                alert.show();








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
                    addFab.show();

                }

                else {
                    Collection<String> filtered = Collections2.filter(helperListItem,
                            Predicates.containsPattern("(?i)" + s));
                    listItem.clear();
                    listItem.addAll(filtered);
                    mAdapter.notifyDataSetChanged();
                    addFab.hide();

                }


                return true;
            }
        });
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
