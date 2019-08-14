package com.jilleliceiri.diary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * EntryDAO interface
 *
 * This interface contains methods to insert, query, update, and delete from the database.
 *
 * @author Jill Eliceiri
 */

@Dao
public interface EntryDAO {
    @Insert
    public void insert(Entry entry);

    @Update
    public void update(Entry entry);

    @Query("DELETE FROM entry_table")
    public void deleteAll();

    @Query("SELECT * from entry_table ORDER BY id ASC")
    List<Entry> getAllEntries();

    @Query("SELECT * from entry_table where id=:id")
    Entry getEntry(int id);

    //@Query("SELECT * from entry_table WHERE word LIKE '%' || :word || '%' ORDER BY word ASC")
    @Query("SELECT * from entry_table WHERE subject LIKE '%' || :search OR content LIKE '%' || :search|| '%' ORDER BY id ASC")
    List<Entry> searchForItem(String search);

    @Delete
    void delete(Entry entry);
}

