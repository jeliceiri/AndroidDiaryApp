package com.jilleliceiri.diary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * ViewEntriesActivity
 *
 * This class retrieves and displays all diary entries. It allows the user to select an individual
 * diary entry and when selected creates a new intent where they can then edit/modify or delete
 * that particular entry.
 *
 * @author Jill Eliceiri
 */


public class ViewEntriesActivity extends AppCompatActivity {

    //instance variables
    EntryRoomV2Database myDB = null;
    ListView listOfEntries;
    ArrayAdapter<String> adapter;

    FragmentTransaction ft;
    FragmentEntriesList entriesFragment;

    /**
     * This method begins the activity. It gets references to the database and views. It
     * instantiates a class and calls a method that will get a list of all diary entries, each of
     * which has an attached listener.
     *
     * @param savedInstanceState the saved instance state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewall);
        myDB = EntryRoomV2Database.getDatabase(this);
        new ViewEntriesActivity.DatabaseGetAllEntries().execute();
    }

    /**
     * This class contains a method that retrieves all diary entries and calls a method to display
     * them.
     */

    private class DatabaseGetAllEntries extends AsyncTask<Void, Void, List<Entry>>
    {
        @Override
        protected List<Entry> doInBackground(Void... val) {
            return myDB.entryDao().getAllEntries();
        }

        @Override
        protected void onPostExecute(List<Entry> entries)
        {
            buildFragment(entries);
        }
    }

    /**
     * This class builds a fragment which will display a list of diary entries.
     * @param entries the list of diary entries
     */

    public void buildFragment(List<Entry> entries) {
        ft = getSupportFragmentManager().beginTransaction();
        entriesFragment = FragmentEntriesList.newInstance(entries, "viewAll");
        ft.replace(R.id.allEntriesHolder, entriesFragment);
        ft.commit();
    }

    /**
     * This method creates an edit entry activity via intent
     * @param entryID the diary entry id
     */

    public void startIntent(String entryID) {
        Intent editEntryIntent = new Intent(ViewEntriesActivity.this,
                EditEntryActivity.class);
        editEntryIntent.putExtra("entryID", entryID);
        startActivity(editEntryIntent);
    }
}
