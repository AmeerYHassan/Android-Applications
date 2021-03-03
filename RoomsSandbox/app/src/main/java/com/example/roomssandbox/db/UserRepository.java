package com.example.roomssandbox.db;

import android.app.Application;
import android.service.autofill.UserData;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import java.util.List;

import com.example.roomssandbox.db.entity.UserEntity;
import com.example.roomssandbox.db.dao.UserDao;


public class UserRepository {
    private UserDao userDao;
    private LiveData<List<UserEntity>> allUsers;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAll();
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return allUsers;
    }

    public LiveData<List<UserEntity>> findPartialMatch(String searchQuery) {
        return userDao.findPartialMatch(searchQuery);
    }

    public LiveData<Integer> getUserCount(){
        return userDao.getUserCount();
    }

    public LiveData<Integer> getMatchedUserCount(String searchQuery){
        return userDao.getMatchedUserCount(searchQuery);
    }

    public UserEntity findByName(String firstName, String lastName){
        return userDao.findByName(firstName, lastName);
    };

    public void insert(UserEntity user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
        });
    }

    public void delete(UserEntity user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.delete(user);
        });
    }
}
