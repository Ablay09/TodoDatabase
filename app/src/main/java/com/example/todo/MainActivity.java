package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TodoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private FloatingActionButton fab;

    protected static TodoDatabase todoDatabase;

    private GetAllTodosAsync getAllTodosAsync;
    private List<Todo> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        initAdapter();
        initRoomDatabase();
        initViews();
    }

    private void initRoomDatabase() {
        todoDatabase = TodoDatabase.getInstance(this);
        readFromDatabase();
    }

    private void initViews() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
                startActivityForResult(intent, Constants.CREATE_TO_DO_REQUEST);
            }
        });
    }

    @Override
    public void onItemClick(int position, Todo item) {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra(Constants.TODO, item);
        intent.putExtra(Constants.POSITION, position);
        startActivityForResult(intent, Constants.UPDATE_TO_DO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CREATE_TO_DO_REQUEST) {
                Todo todo = (Todo) data.getSerializableExtra(Constants.TODO);
                if (todo != null) {
                    todoAdapter.addItem(todo);
                }
            } else if (requestCode == Constants.UPDATE_TO_DO_REQUEST) {
                Todo todo = (Todo) data.getSerializableExtra(Constants.TODO);
                int position = (int) data.getSerializableExtra(Constants.POSITION);
                if (todo != null) {
                    if (position >= 0) {
                        todoAdapter.updateItem(position, todo);
                    }
                }
            }
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
        );
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initAdapter() {
        todoAdapter = new TodoAdapter(this);
        recyclerView.setAdapter(todoAdapter);
        todoList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void readFromDatabase() {
        getAllTodosAsync = new GetAllTodosAsync();
        getAllTodosAsync.execute();
    }

    class GetAllTodosAsync extends AsyncTask<Void, Void, List<Todo>> {

        @Override
        protected List<Todo> doInBackground(Void... voids) {
            todoList = todoDatabase.todoDao().getTodos();
            return todoList;
        }

        @Override
        protected void onPostExecute(List<Todo> todos) {
            super.onPostExecute(todos);
            todoAdapter.addAll(todos);
        }
    }
}
