package com.example.roomssandbox.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.roomssandbox.db.entity.UserEntity;

@Dao
public interface UserDao {
    @Query("SELECT * FROM UserEntity")
    LiveData<List<UserEntity>> getAll();

    @Query("SELECT * FROM UserEntity WHERE uid IN (:userIds)")
    LiveData<List<UserEntity>> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM UserEntity WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    UserEntity findByName(String first, String last);

    @Query("SELECT * FROM UserEntity WHERE first_name LIKE :searchQuery OR " +
            "last_name LIKE :searchQuery")
    LiveData<List<UserEntity>> findPartialMatch(String searchQuery);

    @Query("SELECT COUNT(first_name) FROM UserEntity")
    LiveData<Integer> getUserCount();

    @Query("SELECT COUNT(first_name) FROM UserEntity WHERE first_name LIKE :searchQuery OR " +
            "last_name LIKE :searchQuery")
    LiveData<Integer> getMatchedUserCount(String searchQuery);

    @Insert
    void insert(UserEntity user);

    @Insert
    void insertAll(UserEntity... users);

    @Delete
    void delete(UserEntity user);

    @Query("DELETE FROM UserEntity")
    void deleteAll();
}
