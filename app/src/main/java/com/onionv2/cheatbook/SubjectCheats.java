package com.onionv2.cheatbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SubjectCheats extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Toolbar toolbarSubject;
    public static String subject;
    RecyclerView recyclerView;
    public static CoordinatorLayout coordinatorLayout;
    public static SubjectCheatsAdapter subjectCheatsAdapter;
    public static List<Cheat> cheatList;
    public static DatabaseHelper databaseHelper;
    public static SubjectHelper subjectHelper;
    FloatingActionButton addFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_cheats);

        Bundle bundle = getIntent().getExtras();
        subject = bundle.getString("subj");
        databaseHelper = new DatabaseHelper(this);
        subjectHelper = new SubjectHelper(this);

        addFab = findViewById(R.id.addCheatFab);







        recyclerView = findViewById(R.id.recycler_view_subject);

        cheatList = new ArrayList<>();

        subjectCheatsAdapter = new SubjectCheatsAdapter(this, cheatList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(subjectCheatsAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new SubjectCheatsAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(SubjectCheats.this, GridActivity.class);

                intent.putExtra("title", cheatList.get(position).getTitle());
                intent.putExtra("subj", cheatList.get(position).getSubject());
                intent.putExtra("uris", cheatList.get(position).getUris());
                intent.putExtra("id", cheatList.get(position).getId());
                intent.putExtra("timestamp", cheatList.get(position).getTimestamp());

                startActivity(intent);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubjectCheats.this, CreateCheatStepOne.class);
                intent.putExtra("subj", subject);
                startActivity(intent);
            }
        });



        getCheats();

    }

    public static void getCheats() {

        cheatList.addAll(databaseHelper.getCheatsBySubject(subject));
        subjectCheatsAdapter.notifyDataSetChanged();
        if (cheatList.size() == 0){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout,  "No cheats! Add new by clicking on plus button", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof SubjectCheatsAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cheatList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Cheat deletedItem = cheatList.get(viewHolder.getAdapterPosition());
            databaseHelper.deleteCheat(deletedItem.getId(), deletedItem.getTimestamp());
            subjectHelper.decrementSubject(subject);

            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            subjectCheatsAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " successfully deleted!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subjectCheatsAdapter.restoreItem(deletedItem, deletedIndex);
                    databaseHelper.insertCheat(deletedItem.getSubject(), deletedItem.getTitle(), deletedItem.getUris());
                    subjectHelper.incrementSubject(subject);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
