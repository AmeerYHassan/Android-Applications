package com.example.roomssandbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.roomssandbox.db.entity.UserEntity;
import com.example.roomssandbox.viewmodel.UserViewModel;
import com.example.roomssandbox.viewmodel.UserViewModelFactory;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private UserViewModel userViewModel;
    public static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;
    String currentCount;
    Button searchButton;
    TextView searchText;
    TextView userCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UserListAdapter adapter = new UserListAdapter(new UserListAdapter.UserDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView userCount = findViewById(R.id.userCount);

        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(getApplication())).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, users -> {
            adapter.submitList(users);
        });

        userViewModel.getUserCount().observe(this, users ->{
            currentCount = users.toString() + " Users";
            userCount.setText(currentCount);
        });

        TextView searchText = findViewById(R.id.textSearchBar);

        FloatingActionButton fab_add = findViewById(R.id.fab);
        fab_add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
            startActivityForResult(intent, NEW_USER_ACTIVITY_REQUEST_CODE);
        });

        FloatingActionButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> {
            String searchTextValue = searchText.getText().toString();
            if (searchTextValue.length() > 0){
                userViewModel.findPartialMatch(searchTextValue).observe(this, users -> {
                    adapter.submitList(users);
                });
                userViewModel.getMatchedUserCount(searchTextValue).observe(this, users ->{
                    currentCount = users.toString() + " Users";
                    userCount.setText(currentCount);
                });
            } else {
                userViewModel.getAllUsers().observe(this, users -> {
                    adapter.submitList(users);
                });
                userViewModel.getUserCount().observe(this, users ->{
                    currentCount = users.toString() + " Users";
                    userCount.setText(currentCount);
                });
            }
        });

        FloatingActionButton deleteButton = findViewById(R.id.fab_remove);
        deleteButton.setOnClickListener(view -> {
            String searchTextValue = searchText.getText().toString();
            if (searchTextValue.length() > 0){
                String firstName = searchTextValue.split(" ")[0];
                String lastName = searchTextValue.split(" ")[1];
                AsyncTask.execute(() -> {
                    UserEntity currUser = userViewModel.findByName(firstName, lastName);
                    userViewModel.delete(currUser);
                });
                userViewModel.getAllUsers().observe(this, users -> {
                    adapter.submitList(users);
                });
                searchText.setText("");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_USER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String[] n = data.getStringArrayExtra(AddUserActivity.EXTRA_REPLY);
            UserEntity user = new UserEntity(n[1], n[0]);
            userViewModel.insert(user);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}