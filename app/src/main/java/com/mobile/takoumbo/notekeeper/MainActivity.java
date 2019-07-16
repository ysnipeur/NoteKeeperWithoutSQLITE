package com.mobile.takoumbo.notekeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterRecyclerView.OnNoteListener, CourseAdapterRecyclerView.onCourseListener{

    private RecyclerView recyclerView;
    private List<ModelClass> modelClassList;
    private  List<NoteInfo> noteInfoList;
    private  List<CourseInfo> courseInfosList;
    private AdapterRecyclerView adapterRecyclerView;
    private LinearLayoutManager layoutManager;
    private NavigationView navigationView;
    private View mainView;
    private CourseAdapterRecyclerView courseAdapterRecyclerView;
    private GridLayoutManager gridLayoutManager;

    private NoteKeeperOpenHelper noteKeeperOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Working with the sqlite database

        noteKeeperOpenHelper = new NoteKeeperOpenHelper(this);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });

        // Let's take into consideration our settings preferences

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeDisplayContent();
    }

    @Override
    protected void onDestroy() {
        noteKeeperOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        adapterRecyclerView.notifyDataSetChanged();
        updateNavHeader();
    }

    private void updateNavHeader() {

        // Getting references to our navigation drawer headerView

        View headerView = navigationView.getHeaderView(0);

        TextView textUserName = headerView.findViewById(R.id.text_user_name);
        TextView textEmailAddress = headerView.findViewById(R.id.text_user_email_address);

        // Getting references to our sharedPreferences

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String userName = preferences.getString("user_display_name", "");
        String userEmail = preferences.getString("user_email_address", "");

        textUserName.setText(userName);
        textEmailAddress.setText(userEmail);

    }

    private void initializeDisplayContent() {

        recyclerView = findViewById(R.id.list_items);

        // Setting the layout manager for the recyclerView

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);

        // Setting grid layout for the course recyclerView

        gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.course_grid_span));

        noteInfoList = new ArrayList<>();
        courseInfosList = new ArrayList<>();

        noteInfoList = DataManager.getInstance().getNotes();
        courseInfosList = DataManager.getInstance().getCourses();


        adapterRecyclerView = new AdapterRecyclerView(noteInfoList, this);
        courseAdapterRecyclerView = new CourseAdapterRecyclerView(courseInfosList, this);


        recyclerView.setHasFixedSize(true);
        displayNotes();
        displayCourses();
        adapterRecyclerView.notifyDataSetChanged();



    }

    private void displayCourses() {
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(courseAdapterRecyclerView);

        selectNavigationMenuItem(R.id.nav_courses);
    }

    // Method used to display the list of notes when the user clicks on the item menu(notes) found in the navigation drawer
    private void displayNotes() {

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterRecyclerView);

        // let's connect to our database, it database exists getReadableDatabase() returns SQLiteDatabase reference if not it creates database
        // this in not the best way of doing, causes slaugish user experience
        SQLiteDatabase db = noteKeeperOpenHelper.getReadableDatabase();
        selectNavigationMenuItem(R.id.nav_notes);
    }

    // Method used to select a menuItem
    private void selectNavigationMenuItem(int id) {
        Menu menu = navigationView.getMenu();

        // getting the item(notes)

        menu.findItem(id).setChecked(true);
    }

    @Override
    public void onNoteClick(int position) {


        Intent intent = new Intent(MainActivity.this, NoteActivity.class);

        NoteInfo noteInfo = noteInfoList.get(position);

        intent.putExtra(NoteActivity.NOTE_INFO, noteInfo);
        intent.putExtra(NoteActivity.NOTE_POSITION, position);

        // Log.d("Activity", "onNoteClick: in activity list :" + noteInfo.getText());
        startActivity(intent);
    }

    @Override
    public void onCourseClick(int position) {

        mainView = findViewById(R.id.list_items);
        CourseInfo courseInfo = courseInfosList.get(position);
        Snackbar.make(mainView, courseInfo.getTitle(), Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            // Handle the camera action
            displayNotes();
        } else if (id == R.id.nav_courses) {

           displayCourses();

        } else if (id == R.id.nav_share) {

            //handleSelection("Keep on sharing");

            handleShare();

        } else if (id == R.id.nav_send) {
            handleSelection("ready to send");
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleShare() {

        mainView = findViewById(R.id.list_items);

        Snackbar.make(mainView, "Share to  - : " +
                PreferenceManager.getDefaultSharedPreferences(this).getString("user_favorite_social", ""),
                Snackbar.LENGTH_LONG).show();
    }

    // Each time user selects share we will show current sharedPreference concerning social network
    private void handleSelection(String message)
    {
        mainView = findViewById(R.id.list_items);

        Snackbar.make(mainView, message, Snackbar.LENGTH_LONG).show();
    }

}
