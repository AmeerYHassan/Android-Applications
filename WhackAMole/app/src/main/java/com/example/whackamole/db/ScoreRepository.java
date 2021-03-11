package com.example.whackamole.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.whackamole.db.dao.ScoreDao;
import com.example.whackamole.db.entity.ScoreEntity;


public class ScoreRepository {

    private ScoreDao scoreDao;
    private LiveData<List<ScoreEntity>> top10;
    private LiveData<List<ScoreEntity>> latest10;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ScoreRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        scoreDao = db.scoreDao();
        top10 = scoreDao.getTop10();
        latest10 = scoreDao.getLatest10();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<ScoreEntity>> getTop10() {
        return top10;
    }

    public LiveData<List<ScoreEntity>> getLatest10() {
        return latest10;
    }

    public LiveData<List<ScoreEntity>> findMatches(String currName) {
        return scoreDao.findMatches(currName);
    }

    public void insert(ScoreEntity score) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.insert(score);
        });
    }

    public void deleteAll(){
        scoreDao.deleteAll();
    }

    public ScoreEntity findByName(String name){
        return scoreDao.findByName(name);
    };

    public void delete(ScoreEntity user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            scoreDao.delete(user);
        });
    }
}
