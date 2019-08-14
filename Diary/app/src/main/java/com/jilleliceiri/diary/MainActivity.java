package com.jilleliceiri.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.os.AsyncTask;
import java.util.List;

/**
 * MainActivity
 *
 * This class functions as the main activity where a user has the option to view all diary entries,
 * create a new diary entry, or view all entries on a map.
 *
 * @author Jill Eliceiri
 */

public class MainActivity extends AppCompatActivity {

    //instance variables
    EntryRoomV2Database myDB = null;
    EditText searchItem;

    FragmentTransaction ft;
    FragmentEntriesList entriesFragment;

    /**
     * This method begins the activity. It gets references to the database and views. It sets a
     * listener on the list of search results. If a user selects a diary entry from the search list,
     * a new intent is started which will allow the user to edit/modify or delete that particular
     * diary entry.
     *
     * @param savedInstanceState the saved instance state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = EntryRoomV2Database.getDatabase(this);
        searchItem = (EditText) findViewById(R.id.searchItem);
    }

    /**
     * This method is called when the user selects the view all button. It creates and starts
     * a new intent which will display all of the diary entries.
     *
     * @param view the view
     */

    public void viewAll(View view) {

        Intent viewAllEntriesIntent = new Intent(MainActivity.this,
                ViewEntriesActivity.class);
        startActivity(viewAllEntriesIntent);
    }

    /**
     * This method is called when the user selects the create new entry button. It creates and
     * starts a new intent which will allow the user to create a new diary entry.
     *
     * @param view the view
     */

    public void createNew(View view) {

        Intent createNewEntryIntent = new Intent(MainActivity.this,
                CreateEntryActivity.class);
        startActivity(createNewEntryIntent);
    }

    /**
     * This method is called when the user selects the search button. It gets the user entered
     * search item and instantiates a new class which will perform a search on the user entered
     * search item.
     *
     * @param view the view
     */

    public void searchAll(View view) {


        String search = searchItem.getText().toString();
        new MainActivity.DatabaseSearch().execute(search);

    }

    public void viewEntriesOnMap(View view) {

        Intent viewMaps = new Intent(MainActivity.this,
                MapsActivity.class);
        startActivity(viewMaps);
    }

    /**
     * This class searches for the user entered search item. When the data access object returns
     * the search results from the database, a method is then called that will display this list.
     */

    private class DatabaseSearch extends AsyncTask<String, Void, List<Entry>> {

        @Override
        protected List<Entry> doInBackground(String... val) {
            return myDB.entryDao().searchForItem(val[0]);
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            buildFragment(entries);
        }
    }

    /**
     * This class builds a fragment which will display a list of diary entries.
     * @param entries the list of diary entries
     */

    public void buildFragment(List<Entry> entries) {
        ft = getSupportFragmentManager().beginTransaction();
        entriesFragment = FragmentEntriesList.newInstance(entries, "main");
        ft.replace(R.id.entriesHolder, entriesFragment);
        ft.commit();
    }

    /**
     * This method starts the edit entry activity via intent.
     * @param entryID the diary entry id
     */


    public void startIntent(String entryID) {
        Intent editEntryIntent = new Intent(MainActivity.this,
                EditEntryActivity.class);
        editEntryIntent.putExtra("entryID", entryID);
        startActivity(editEntryIntent);
    }
}
