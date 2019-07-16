package com.mobile.takoumbo.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity implements AdapterRecyclerView.OnNoteListener {



    private RecyclerView recyclerView;
    private List<ModelClass> modelClassList;
    private  List<NoteInfo> noteInfoList;
    private AdapterRecyclerView adapterRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });


        initializeDisplayContent();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        adapterRecyclerView.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {

        recyclerView = findViewById(R.id.list_note);

        // Setting the layout manager for the recyclerView

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        // Creating the list of the modelclass which will be passed to my adapter
        modelClassList = new ArrayList<>();
        noteInfoList = new ArrayList<>();


        // Filling in the values of modelClass to be printed by the viewholder on the item_layout

        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));
        modelClassList.add(new ModelClass(R.drawable.ic_launcher_background, "Software Engineer", "This is one of the most important job in computer science"));

        // Creating the adapter

        //Adapter adapter = new Adapter(modelClassList, this);

         noteInfoList = DataManager.getInstance().getNotes();

        adapterRecyclerView = new AdapterRecyclerView(noteInfoList, this);



       // ArrayAdapter<NoteInfo> adapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterRecyclerView);
        adapterRecyclerView.notifyDataSetChanged();


        //adapter.notifyDataSetChanged();

    }

    @Override
    public void onNoteClick(int position) {


        Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);

        NoteInfo noteInfo = noteInfoList.get(position);

        intent.putExtra(NoteActivity.NOTE_INFO, noteInfo);
        intent.putExtra(NoteActivity.NOTE_POSITION, position);

       // Log.d("Activity", "onNoteClick: in activity list :" + noteInfo.getText());
        startActivity(intent);
    }
}
