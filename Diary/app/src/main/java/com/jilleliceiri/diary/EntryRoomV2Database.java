package com.jilleliceiri.diary;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * EntryRoomV2Database
 *
 * This class contains the database holder and serves as the main access point for relational data.
 * It is an abstract class which extends RoomDatabase. Code used for this class is from the course
 * materials in CPSC 6138.
 *
 */

@Database(entities = {Entry.class}, version = 2)
public abstract class EntryRoomV2Database extends RoomDatabase {

    //instance variables
    public abstract EntryDAO entryDao();
    private static volatile EntryRoomV2Database INSTANCE;

    static EntryRoomV2Database getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (EntryRoomV2Database.class)
            {
                if(INSTANCE ==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EntryRoomV2Database.class, "entry_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
