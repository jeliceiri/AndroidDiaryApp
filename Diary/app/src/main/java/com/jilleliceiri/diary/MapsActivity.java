package com.jilleliceiri.diary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MapsActivity
 *
 * This class displays all diary entries on a map using markers
 *
 * @author Jill Eliceiri
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //instance variables
    private GoogleMap mMap;
    EntryRoomV2Database myDB = null;
    Map<Integer, LatLng> locations;

    /**
     * This method begins the activity. It gets references to the database and views. It creates a
     * db object and calls a methid which obtains all diary entries.
     *
     * @param savedInstanceState the saved instance state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myDB = EntryRoomV2Database.getDatabase(this);
        new MapsActivity.DatabaseGetAllEntries().execute();
    }

    /**
     * This class searches for all diary entries. When the data access object returns
     * the results from the database, a method is then called that build a map.
     */

    private class DatabaseGetAllEntries extends AsyncTask<Void, Void, List<Entry>> {
        @Override
        protected List<Entry> doInBackground(Void... val) {
            return myDB.entryDao().getAllEntries();
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            buildMap();

            //loop through all the entries and store in the hashmap
            locations = new HashMap<>();
            for (Entry anEntry : entries) {
                LatLng newLoc = (new LatLng(anEntry.getLatitude(), anEntry.getLongitude()));
                int id = anEntry.getId();
                locations.put(id, newLoc);
            }
        }
    }

    private void buildMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Once map is available, add markers and move camera.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //list used to hold all markers
        List<MarkerOptions> markers = new ArrayList<>();

        //loop through all entries to populate map with markers and id
        for (Map.Entry<Integer, LatLng> entry : locations.entrySet()) {
            MarkerOptions markerOptions = new MarkerOptions();
            String id = Integer.toString(entry.getKey());
            LatLng latLng = entry.getValue();

            //add marker to map
            mMap.addMarker(markerOptions.position(latLng).title(id));

            //add marker to list of markers
            markers.add(markerOptions);
        }
        //create bounds of all markers, used to display all on map
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 500;

        //move camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
    }

    /**
     * Called when the user clicks a map marker. A toast message displays letting the user know
     * which diary entry id has been clicked and the edit entry activity begins via an intent.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        String entryID = (String) marker.getTitle();

        Toast.makeText(this,
                entryID +
                        " has been clicked ",
                Toast.LENGTH_LONG).show();

        Intent editEntryIntent = new Intent(MapsActivity.this,
                EditEntryActivity.class);
        editEntryIntent.putExtra("entryID", entryID);
        startActivity(editEntryIntent);

        //false indicates the event was not consumed and default behavior occurs, which
        //is for the camera to move and center marker and open info window if present.
        return false;
    }
}
