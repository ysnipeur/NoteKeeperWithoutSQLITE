package com.mobile.takoumbo.notekeeper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_INFO = "com.mobile.takoumbo.notekeeper.NOTE_INFO";
    public static final String NOTE_POSITION = "com.mobile.takoumbo.notekeeper.NOTE_POSITION";
    private static final int POSITION_NOT_SET = -1;
    private final String TAG  = getClass().getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private NoteInfo noteInfo;
    private List<CourseInfo> listCourses;
    private boolean isNewNote;

    private Spinner spinner;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private int newNotePosition;
    private boolean isCancelling;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;
    private int notePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Let's have access to our spinner

        spinner = findViewById(R.id.spinner_courses);

        listCourses = DataManager.getInstance().getCourses();

        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCourses);

        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterCourses);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePictureIntent();
            }
        });


        readDisplayStateValues(); // Getting the parcelled data
        saveOriginalNoteValues();

        textNoteTitle = findViewById(R.id.text_note_title);

        textNoteText = findViewById(R.id.text_note_text);

        // If it is a new note, we don't display content

        if(!isNewNote)
            displayNote(spinner, textNoteTitle, textNoteText);

    }

    private void saveOriginalNoteValues() {
        if(isNewNote)
            return;
        originalNoteCourseId = noteInfo.getCourse().getCourseId();
        originalNoteTitle = noteInfo.getTitle();
        originalNoteText = noteInfo.getText();
    }

    private void displayNote(Spinner spinner, EditText textNoteTitle, EditText textNoteText) {

        int courseIndex = listCourses.indexOf(noteInfo.getCourse());

        spinner.setSelection(courseIndex);
        textNoteTitle.setText(noteInfo.getTitle());
        textNoteText.setText(noteInfo.getText());

        ///Log.d("Activity", "onNoteClick: " + noteInfo.getCourse().getTitle());

    }

    // Getting note from the intent

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        noteInfo = intent.getParcelableExtra(NOTE_INFO);
        notePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        //name = intent.getStringExtra(NoteListActivity.NAME);
        isNewNote = (noteInfo == null);

        if(isNewNote)
            createNewNote();
    }

    private void createNewNote() {
        DataManager dataManager = DataManager.getInstance();
        newNotePosition = dataManager.createNewNote();
        noteInfo = dataManager.getNotes().get(newNotePosition);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {

            sendEmail();
            return true;
        }

        else if(id == R.id.action_cancel)
        {
            isCancelling = true;
            finish();
        }
        else if(id == R.id.action_next)
        {
            moveNext();

        }

        return super.onOptionsItemSelected(item);
    }

    private void moveNext() {
        saveNote();
        ++notePosition;
        noteInfo = DataManager.getInstance().getNotes().get(notePosition);

        saveOriginalNoteValues();

        displayNote(spinner, textNoteText, textNoteTitle);

        invalidateOptionsMenu(); // It is responsible for calling back the onPrepareOptionsMenu

    }

    @Override
    protected void onPause() {
            super.onPause();

            if(isCancelling)
            {
                if(isNewNote)
                    DataManager.getInstance().removeNote(newNotePosition);
                else
                    restorePreviousNoteValues();
            }
            else
            {
                System.out.println("We enter here");
                //String text = textNoteText.getText().toString();
                //System.out.println(text);
                //noteInfo.setText(text);
                saveNote();
            }


    }

    private void restorePreviousNoteValues() {

        CourseInfo courseInfo = DataManager.getInstance().getCourse(originalNoteCourseId);
        noteInfo.setCourse(courseInfo);
        noteInfo.setTitle(originalNoteTitle);
        noteInfo.setText(originalNoteText);
    }

    private void saveNote() {


        noteInfo.setCourse((CourseInfo) spinner.getSelectedItem());
        noteInfo.setTitle(textNoteTitle.getText().toString());
        noteInfo.setText(textNoteText.getText().toString());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_next);
        int lastIndex = DataManager.getInstance().getNotes().size() - 1;

        menuItem.setEnabled(notePosition < lastIndex);
        if(notePosition > lastIndex)
            Toast.makeText(this, "No further element", Toast.LENGTH_LONG).show();
        
        return super.onPrepareOptionsMenu(menu);
    }

    private void sendEmail() {

        CourseInfo courseInfo = (CourseInfo) spinner.getSelectedItem();

        String subject = textNoteTitle.getText().toString();
        String body = "Checkout what i'm learning in the pluralsight program \"" +
                        courseInfo.getTitle() + "\"\n" + textNoteText.getText().toString();

        // Intent to send maill

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        if(intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }

    private void TakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //imageView.setImageBitmap(imageBitmap);
        }
    }

}
