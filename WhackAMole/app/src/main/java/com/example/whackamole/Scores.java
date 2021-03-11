package com.example.whackamole;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.whackamole.viewmodel.ScoreViewModel;
import com.example.whackamole.viewmodel.ScoreViewModelFactory;

public class Scores extends AppCompatActivity {

    private ScoreViewModel scoreViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        // Instantiate the Activity, fetch all of the views we will use.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(new ScoreListAdapter.UserDiff());

        // Update the recycler view with the top 10 scores ordered by score value
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreViewModel = new ViewModelProvider(this, new ScoreViewModelFactory(getApplication())).get(ScoreViewModel.class);
        scoreViewModel.getTop10().observe(this, scores -> {
            adapter.submitList(scores);
        });
    }

    // Button to take you back to the menu
    public void backToMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}