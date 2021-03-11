package com.example.whackamole;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whackamole.db.entity.ScoreEntity;
import com.example.whackamole.viewmodel.ScoreViewModel;
import com.example.whackamole.viewmodel.ScoreViewModelFactory;

public class History extends AppCompatActivity {

    private ScoreViewModel scoreViewModel;
    Button searchButton;
    TextView searchText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        searchButton = findViewById(R.id.searchBtn);
        searchText = findViewById(R.id.searchText);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // Score items to initially populate the list.
        final ScoreListAdapter adapter = new ScoreListAdapter(new ScoreListAdapter.UserDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreViewModel = new ViewModelProvider(this, new ScoreViewModelFactory(getApplication())).get(ScoreViewModel.class);
        scoreViewModel.getLatest10().observe(this, scores -> {
            adapter.submitList(scores);
        });
    }

    public void searchText(View view){
        // Fetch the different views from our XML
        String searchTextValue = searchText.getText().toString();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(new ScoreListAdapter.UserDiff());

        // Prepare the recycler view to be changed.
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreViewModel = new ViewModelProvider(this, new ScoreViewModelFactory(getApplication())).get(ScoreViewModel.class);

        // If the search box contains text, send the text to be searched. Otherwise, return the latest 10.
        if (searchTextValue.length() > 0){
            scoreViewModel.findMatches(searchTextValue).observe(this, users -> {
                adapter.submitList(users);
            });
            searchText.setText("");
        } else {
            scoreViewModel.getLatest10().observe(this, scores -> {
                adapter.submitList(scores);
            });
        }
    }

    public void backToMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}