package com.jilleliceiri.diary;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * WeatherRequesterTask
 *
 * This class uses the NOAA weather web services to retrieve weather info about when and where
 * the diary entry was created. Weather info obtained includes the temperature and the forecast.
 * Code from this class is from CSU CPSC 6138 course materials (Created by csu on 7/19/2016)
 *
 * @author Jill Eliceiri
 */

public class WeatherRequesterTask extends AsyncTask<Location, Void, String> {

    //instance variables
    String forecast;
    String temperature;
    Context context;

    //constructor
    public WeatherRequesterTask(Context con) {
        context = con;
    }

    //getters and setters
    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }


    /**
     * This method calls a method that obtains the weather information
     */


    @Override
    protected String doInBackground(Location... locations) {
        //Toast.makeText(context, "Inside do in background", Toast.LENGTH_LONG).show();
        try {
            //return invokeWebServiceNoApache(locations[0]);
            return parseAndNotify(invokeWebServiceNoApache(locations[0]));

        } catch (URISyntaxException e) {
            Log.e("Error URI Syntax ", e.getMessage());
        } catch (IOException e) {
            Log.e("Error URI Syntax ", e.getMessage());
        }
        return null;
    }

    /**
     * This method sends the weather info to the create entry activity where it will be set
     */

    protected void onPostExecute(String result) {
        if (result != null) {

            CreateEntryActivity.sendInfo(temperature, forecast);

            //The code below builds an alert dialog with the info and displays it
            /*
            //1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            //2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(result)
            .setTitle("Weather Request");

            //3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();*/
        }
    }

    /**
     * This method creates a url, opens a connection, and collects the data returned
     */

    private String invokeWebServiceNoApache(Location loc) throws URISyntaxException, IOException {

        String lat = "" + loc.getLatitude();
        String lon = "" + loc.getLongitude();
        URL url = new URL("https://api.weather.gov/points/" + lat + "," + lon + "/forecast");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line + NL);
        }
        urlConnection.disconnect();
        return sb.toString();
    }

    /**
     * This method parses the result using JSON, sets the temperature and forecast variables, and
     * returns a string containing the temperature and forecast.
     */

    private String parseAndNotify(String theResult) {
        JSONTokener theTokener = new JSONTokener(theResult);
        JSONObject theWeatherResult;
        try {
            theWeatherResult = (JSONObject) theTokener.nextValue();
            if (theWeatherResult != null) {
                JSONObject curWeather = theWeatherResult.getJSONObject("properties");
                JSONArray array = curWeather.getJSONArray("periods");
                setTemperature(array.getJSONObject(0).getString("temperature"));
                setForecast(array.getJSONObject(0).getString("detailedForecast"));
                return getTemperature() + getForecast();
            }
        } catch (JSONException e) {
            Log.e("Error on AsynchTask ", e.getMessage());
        }
        return null;
    }
}
