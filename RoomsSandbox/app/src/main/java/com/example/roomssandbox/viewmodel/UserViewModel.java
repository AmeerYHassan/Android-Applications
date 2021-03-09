package com.example.roomssandbox.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.roomssandbox.db.UserRepository;
import com.example.roomssandbox.db.entity.UserEntity;

public class UserViewModel extends AndroidViewModel {
    private UserRepository mRepository;
    private final LiveData<List<UserEntity>> allUsers;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
        allUsers = mRepository.getAllUsers();
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return allUsers;
    }

    public LiveData<List<UserEntity>> findPartialMatch(String searchQuery) {
        return mRepository.findPartialMatch(searchQuery);
    }

    public LiveData<Integer> getUserCount(){ return mRepository.getUserCount(); }

    public LiveData<Integer> getMatchedUserCount(String searchQuery){
        return mRepository.getMatchedUserCount(searchQuery);
    }

    public void insert(UserEntity user) {
        mRepository.insert(user);
    }

    public void delete(UserEntity user) { mRepository.delete(user);}

    public UserEntity findByName(String firstName, String lastName){
        return mRepository.findByName(firstName, lastName);
    };
}
