package com.jilleliceiri.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * CreateEntryActivity
 *
 * This class allows the user to create a new diary entry. It allows the user enter a subject,
 * content, and date, which the latter is used to populate both the date created field and the date
 * modified field. The id field is generated automatically. Weather information (temperature and
 * forecast), along with Location inforamtion (latitude and longitude) is also inserted into the db.
 * After a diary entry is created, the user is allowed to create another diary entry per the
 * project specs.
 *
 * @author Jill Eliceiri
 */

public class CreateEntryActivity extends AppCompatActivity implements LocationListener {

    //instance variables
    EntryRoomV2Database myDB = null;
    EditText subject;
    EditText content;
    EditText date;
    Context context = this;
    Double longitude;
    Double latitude;
    static String temperature;
    static String forecast;
    int reqCode = (int) (Math.random() * 100.0);

    /**
     * This method begins the activity. It gets references to the database and views, and calls
     * a method to request location permissions.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        myDB = EntryRoomV2Database.getDatabase(this);
        subject = findViewById(R.id.editEntrySubject);
        content = findViewById(R.id.editEntryContent);
        date = findViewById(R.id.editModifiedDate);
        requestLocationPermissions();
    }

    /**
     * This method is called when the save button is selected. It gets the user entered
     * subject, content, and date. It then instantiates a class which will insert this info into
     * the database, along with location and weather information.
     *
     * @param view the view
     */

    public void saveEntry(View view) {

        Entry newEntry = new Entry(
                subject.getText().toString(),
                content.getText().toString(),
                date.getText().toString(),
                latitude,
                longitude,
                temperature,
                forecast
        );

        new DatabaseInsertTask().execute(newEntry);
    }

    /**
     * This class inserts a new diary entry into the database. It then displays a toast message
     * and allows the user to enter another diary entry.
     */

    private class DatabaseInsertTask extends AsyncTask<Entry, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Entry... words) {
            myDB.entryDao().insert(words[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean val) {
            Toast.makeText(context, "Sucessfully inserted ", Toast.LENGTH_LONG).show();
            //per project specs: allow user to enter another entry
            Intent mainActivityIntent = new Intent(CreateEntryActivity.this,
                    CreateEntryActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    /**
     * This method checks permissions and displays an appropriate toast message.
     */

    private void requestLocationPermissions() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if ((ActivityCompat.checkSelfPermission(this, perms[0]) == PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(this, perms[1]) == PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Now we have permissions", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Now we request permissions", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, perms, reqCode);
        }
    }

    /**
     * This method displays an appropriate toast message as to which permissions were granted and
     * calls a method to subscribe location provider
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == reqCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Got Permissions for Manifest.permission.COARSE_LOCATION", Toast.LENGTH_LONG).show();
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Got Permissions for Manifest.permission.FINE_LOCATION", Toast.LENGTH_LONG).show();
            }
        }
        subscribeLocationProvider();
    }

    /**
     * This class obtains the location information and creates a weather requester task which
     * obtains the weather information
     */

    @Override
    public void onLocationChanged(Location location) {
        //obtain latitude and longitude
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //get weather info
        WeatherRequesterTask wtask = new WeatherRequesterTask(this);
        wtask.execute(location);

        //remove updates
        LocationManager locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locman.removeUpdates(this);
    }

    /**
     * This method sets the temperature and forecast
     * @param temp the temperature
     * @param forc the forecast
     */

    public static void sendInfo(String temp, String forc) {
        temperature = temp;
        forecast = forc;
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    /**
     * This method creates a location manager object and requests location updates if the
     * permissions are granted.
     */

    private void subscribeLocationProvider() {

        LocationManager locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locman.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeLocationProvider();
    }

    @Override
    public void onStop() {
        super.onStop();

        LocationManager locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locman.removeUpdates(this);
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locman.removeUpdates(this);
        }
    }
}
