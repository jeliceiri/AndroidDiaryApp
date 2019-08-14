package com.jilleliceiri.diary;

import androidx.fragment.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * EditEntryActivity
 *
 * This class displays an individual diary entry and allows the user to edit or delete this existing
 * diary entry. Fields that the user can edit/modify are the subject, content, and modified date.
 *
 * @author Jill Eliceiri
 */

public class EditEntryActivity extends FragmentActivity implements OnMapReadyCallback {

    //instance variables
    EditText editEntrySubject;
    EditText editEntryContent;
    EditText editModifiedDate;
    TextView id;
    TextView temperature;
    TextView forecast;
    EntryRoomV2Database myDB = null;
    Context context = this;
    String content;
    Entry myEntry;
    Double lat;
    Double lon;
    private GoogleMap mMap;

    /**
     * This method begins the activity. It gets references to the database and views. It also gets
     * the id of the diary entry to be edited, via the getExtras() method, and uses this id to query
     * the db.
     *
     * @param savedInstanceState the saved instance state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editEntrySubject = (EditText) findViewById(R.id.editEntrySubject);
        editEntryContent = (EditText) findViewById(R.id.editEntryContent);
        editModifiedDate = (EditText) findViewById(R.id.editModifiedDate);
        id = (TextView) findViewById(R.id.entryID);
        temperature = (TextView) findViewById(R.id.temperature);
        forecast = (TextView) findViewById(R.id.forecast);
        myDB = EntryRoomV2Database.getDatabase(this);
        Intent intent = getIntent();
        String entryID = intent.getExtras().getString("entryID");
        int numID = Integer.parseInt(entryID);

        //call method to query database and get the entry
        new EditEntryActivity.DatabaseRetrieveEntry().execute(numID);
    }

    /**
     * When map is ready, display the location of the individual diary entry.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng entry = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(entry));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(entry));
    }

    /**
     * This builds a map.
     */

    public void displayMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * This class has a method which retrieves a diary entry. It then calls methods to display
     * the entry and a map where it is located.
     */

    private class DatabaseRetrieveEntry extends AsyncTask<Integer, Void, Entry> {
        @Override
        protected Entry doInBackground(Integer... integers) {
            return myDB.entryDao().getEntry(integers[0]);
        }

        @Override
        protected void onPostExecute(Entry entry) {
            displayEntry(entry);
            displayMap();
        }
    }

    /**
     * This method displays an individual diary entry.
     *
     * @param entry the diary entry
     */

    private void displayEntry(Entry entry) {

        lat = entry.getLatitude();
        lon = entry.getLongitude();
        id.setText("Diary Entry ID: " + String.valueOf(entry.getId()));
        editEntrySubject.setText(entry.getSubject());
        editEntryContent.setText(entry.getContent());
        editModifiedDate.setText(entry.getModified());
        temperature.setText("Temperature: " + entry.getTemperature());
        forecast.setText("Forecast: " + entry.getForecast());
        myEntry = entry;
    }

    /**
     * This method is called when the update button is selected. It gets the user entered subject,
     * content, and modified date, and calls a method that executes a query which updates the
     * individual diary entry in the database.
     *
     * @param view the view
     */

    public void saveEdit(View view) {
        myEntry.setSubject(editEntrySubject.getText().toString());
        myEntry.setContent(editEntryContent.getText().toString());
        myEntry.setModified(editModifiedDate.getText().toString());
        new DatabaseUpdateTask().execute(myEntry);
    }

    /**
     * This method is called when the delete button is selected. A method is called that executes a
     * query which deletes an individual diary entry in the database.
     *
     * @param view the view
     */

    public void deleteEntry(View view) {
        new DatabaseDeleteTask().execute(myEntry);
    }

    /**
     * This class contains a method that updates an individual record in the database. It then
     * displays a toast message and starts a new intent for the main activity.
     */

    private class DatabaseUpdateTask extends AsyncTask<Entry, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Entry... entries) {
            myDB.entryDao().update(entries[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean val) {
            Toast.makeText(context, "Sucessfully updated ", Toast.LENGTH_LONG).show();
            Intent mainActivityIntent = new Intent(EditEntryActivity.this,
                    MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    /**
     * This class contains a method that deletes an individual record in the database. It then
     * displays a toast message and starts a new intent for the main activity.
     */

    private class DatabaseDeleteTask extends AsyncTask<Entry, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Entry... entries) {
            myDB.entryDao().delete(entries[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean val) {
            Toast.makeText(context, "Sucessfully deleted ", Toast.LENGTH_LONG).show();
            Intent mainActivityIntent = new Intent(EditEntryActivity.this,
                    MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }
}
