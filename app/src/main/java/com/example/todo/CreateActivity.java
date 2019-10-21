package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class CreateActivity extends AppCompatActivity
        implements OnTodoCompleted {

    private EditText editTextTitleCreate;
    private EditText editTextDescriptionCreate;
    private Button buttonCreate;

    private Todo todo;
    private CreateTodoAsync createTodoAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initViews();
    }

    private void initViews() {
        editTextTitleCreate = findViewById(R.id.editTextTitleCreate);
        editTextDescriptionCreate = findViewById(R.id.editTextDescriptionCreate);
        buttonCreate = findViewById(R.id.buttonCreate);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitleCreate.getText().toString();
                String description = editTextDescriptionCreate.getText().toString();
                createTodo(title, description);
            }
        });
    }

    private void createTodo(String title, String description) {
        if ((title != null && description != null) && (!title.equals("") && !description.equals(""))) {
            if (todo == null) {
                todo = new Todo(title, description, new Date());
            }
            createTodoAsync = new CreateTodoAsync(this);
            createTodoAsync.execute(todo);
        } else {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void itemCreated(Todo todo) {
        Intent intent = new Intent();
        intent.putExtra(Constants.TODO, todo);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    class CreateTodoAsync extends AsyncTask<Todo, Void, Todo> {
        private OnTodoCompleted listener;

        public CreateTodoAsync(OnTodoCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Todo doInBackground(Todo... todos) {
            MainActivity.todoDatabase.todoDao().insertTodo(todos[0]);
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

}

