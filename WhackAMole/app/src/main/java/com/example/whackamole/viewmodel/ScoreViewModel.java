package com.example.whackamole.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.whackamole.db.ScoreRepository;
import com.example.whackamole.db.entity.ScoreEntity;

public class ScoreViewModel extends AndroidViewModel {

    private ScoreRepository mRepository;

    private final LiveData<List<ScoreEntity>> top10;
    private final LiveData<List<ScoreEntity>> latest10;

    public ScoreViewModel(Application application) {
        super(application);
        mRepository = new ScoreRepository(application);
        top10 = mRepository.getTop10();
        latest10 = mRepository.getLatest10();
    }

    public LiveData<List<ScoreEntity>> getTop10() {
        return top10;
    }

    public LiveData<List<ScoreEntity>> getLatest10() {
        return latest10;
    }

    public LiveData<List<ScoreEntity>> findMatches(String currName) {
        return mRepository.findMatches(currName);
    }

    public void insert(ScoreEntity score) {
        mRepository.insert(score);
    }

    public void deleteAll() { mRepository.deleteAll(); }

    public ScoreEntity findByName(String name){
        return mRepository.findByName(name);
    }

    public void delete(ScoreEntity user) { mRepository.delete(user);}
}
