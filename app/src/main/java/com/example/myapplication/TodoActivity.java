package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import database.RoomDb;
import database.Todo;
import database.TodoAdapter;

public class TodoActivity extends AppCompatActivity {
    //initialize variable
    private EditText editText;
    Button btAdd, btReset;
    RecyclerView recyclerView;


    List<Todo> todoList = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;
    RoomDb database;

    TodoAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


        //assign variable
        editText = findViewById(R.id.edit_text);
        btAdd = findViewById(R.id.add_btn);
        btReset = findViewById(R.id.reset_btn);
        recyclerView = findViewById(R.id.recycler_view_todo);


        //initialize database
        database = RoomDb.getInstance(this);
        //store datbase value in todo list

        todoList = database.todoDao().getAll();

        //initialize linear layout manage
        linearLayoutManager = new LinearLayoutManager(this);
        //set layout manage
        recyclerView.setLayoutManager(linearLayoutManager);
        //initialize adapter
        adapter = new TodoAdapter(TodoActivity.this, todoList);
        //set adapter
        recyclerView.setAdapter(adapter);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get straing from edit text
                String sText = editText.getText().toString().trim();
                //check condition
                if(!sText.equals(""))
                {
                    //when text is not empty
                    //initialize todo data
                    Todo todo = new Todo();
                    //set text on todo
                    todo.setText(sText);
                    //insert text in database
                    database.todoDao().insert(todo);
                    //clear edit text
                    editText.setText("");
                    //notify when data is inserted
                    todoList.clear();
                    todoList.addAll(database.todoDao().getAll());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete all data from database
                database.todoDao().reset(todoList);
                //notify when all data deleted
                todoList.clear();
                todoList.addAll(database.todoDao().getAll());
                adapter.notifyDataSetChanged();
            }
        });


    }
}