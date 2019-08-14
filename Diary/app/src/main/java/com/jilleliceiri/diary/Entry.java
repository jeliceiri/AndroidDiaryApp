package com.jilleliceiri.diary;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entry
 *
 * This class represents a diary entry entity.
 *
 * @author Jill Eliceiri
 */

@Entity(tableName = "entry_table")

public class Entry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "subject")
    private String subject = "";

    @NonNull
    @ColumnInfo(name = "content")
    private String content = "";

    @NonNull
    @ColumnInfo(name = "date")
    private String date = "";

    @NonNull
    @ColumnInfo(name = "modified")
    private String modified = "";

    @NonNull
    @ColumnInfo(name = "latitude")
    private Double latitude = null;

    @NonNull
    @ColumnInfo(name = "longitude")
    private Double longitude = null;

    @NonNull
    @ColumnInfo(name = "temperature")
    private String temperature = "";

    @NonNull
    @ColumnInfo(name = "forecast")
    private String forecast = "";


    //constructor
    public Entry(@NonNull String subject, String content, String date, Double latitude, Double longitude, String temperature, String forecast) {

        this.setSubject(subject);
        this.setContent(content);
        this.setDate(date);
        this.setModified(date);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setTemperature(temperature);
        this.setForecast(forecast);
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getSubject() {
        return subject;
    }

    public void setSubject(@NonNull String subject) {
        this.subject = subject;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getModified() {
        return modified;
    }

    public void setModified(@NonNull String modified) {
        this.modified = modified;
    }

    @NonNull
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull Double latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull Double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(@NonNull String temperature) {
        this.temperature = temperature;
    }

    @NonNull
    public String getForecast() {
        return forecast;
    }

    public void setForecast(@NonNull String forecast) {
        this.forecast = forecast;
    }
}
