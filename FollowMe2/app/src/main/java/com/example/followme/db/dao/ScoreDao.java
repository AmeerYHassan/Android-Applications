package com.example.followme.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.followme.db.entity.ScoreEntity;

@Dao
public interface ScoreDao {
    @Query("SELECT * FROM scores")
    LiveData<List<ScoreEntity>> getAll();

    @Query("SELECT * FROM scores order by score desc, created desc LIMIT 10")
    LiveData<List<ScoreEntity>> getTop10();

    @Query("SELECT * FROM scores WHERE name LIKE :name LIMIT 10")
    ScoreEntity findByName(String name);

    @Insert
    void insert(ScoreEntity score);

    @Delete
    void delete(ScoreEntity score);

    @Query("DELETE FROM scores")
    void deleteAll();
}
