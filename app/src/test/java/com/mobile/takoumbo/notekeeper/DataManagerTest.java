package com.mobile.takoumbo.notekeeper;

import android.provider.ContactsContract;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    @Test
    public void createNewNote() {
        final DataManager dataManager = DataManager.getInstance();
        final CourseInfo courseInfo = dataManager.getCourse("android_async");
        final String noteTitle = "Test note Title";
        final  String noteText = "This is the body test of my note";

        // Let's create a new note

        int newNotePosition = dataManager.createNewNote();
        NoteInfo noteInfo = dataManager.getNotes().get(newNotePosition);
        noteInfo.setCourse(courseInfo);
        noteInfo.setTitle(noteTitle);
        noteInfo.setText(noteText);

        // Let's check if the values we've assigned to the new note are correct

        // assetSame compares if two references point to the same object

        NoteInfo comparedInfo = dataManager.getNotes().get(newNotePosition);
        assertEquals(comparedInfo.getCourse(), courseInfo);
        assertEquals(comparedInfo.getTitle(), noteTitle);
        assertEquals(comparedInfo.getText(), noteText);

    }
}