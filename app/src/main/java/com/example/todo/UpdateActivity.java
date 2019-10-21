package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity
        implements OnTodoCompleted{

    private EditText editTextTitleUpdate;
    private EditText editTextDescriptionUpdate;
    private Button buttonUpdate;
    private ImageView imageViewDelete;

    private Todo todo;
    private int todoPosition;
    private UpdateTodoAsync updateTodoAsync;
    private DeleteTodoAsync deleteTodoAsync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initViews();
        setTodoData();
    }

    private void initViews() {
        editTextTitleUpdate = findViewById(R.id.editTextTitleUpdate);
        editTextDescriptionUpdate = findViewById(R.id.editTextDescriptionUpdate);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        imageViewDelete = findViewById(R.id.imageViewDelete);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitleUpdate.getText().toString();
                String description = editTextDescriptionUpdate.getText().toString();
                updateTodo(title, description);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTodo(todo);
            }
        });
    }

    private void setTodoData() {
        if (getIntent().hasExtra(Constants.TODO) && getIntent().hasExtra(Constants.POSITION)) {
            todo = (Todo) getIntent().getSerializableExtra(Constants.TODO);
            todoPosition = (int) getIntent().getSerializableExtra(Constants.POSITION);
            if (todo != null) {
                editTextTitleUpdate.setText(todo.getTitle());
                editTextDescriptionUpdate.setText(todo.getDescription());
            }
        }
    }

    private void updateTodo(String title, String description) {
        if ((title != null && description != null) && (!title.equals("") && !description.equals(""))) {
            if (todo != null) {
                todo.setTitle(title);
                todo.setDescription(description);
            }
            updateTodoAsync = new UpdateTodoAsync(this);
            updateTodoAsync.execute(todo);
        } else {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteTodo(Todo todo) {
        if (todo != null) {
            deleteTodoAsync = new DeleteTodoAsync();
            deleteTodoAsync.execute(todo);
        }
    }

    @Override
    public void itemCreated(Todo todo) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TODO, todo);
        intent.putExtra(Constants.POSITION, todoPosition);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    class UpdateTodoAsync extends AsyncTask<Todo, Void, Todo> {
        private OnTodoCompleted listener;

        public UpdateTodoAsync(OnTodoCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Todo doInBackground(Todo... todos) {
            MainActivity.todoDatabase.todoDao().updateTodo(todos[0]);
            return todos[0];
        }

        @Override
        protected void onPostExecute(Todo todo) {
            super.onPostExecute(todo);
            if (listener != null) {
                listener.itemCreated(todo);
            }
        }
    }
    class DeleteTodoAsync extends AsyncTask<Todo, Void, Todo> {

        @Override
        protected Todo doInBackground(Todo... todos) {
            MainActivity.todoDatabase.todoDao().deleteTodo(todos[0]);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Todo todo) {
            super.onPostExecute(todo);

        }
    }
}

