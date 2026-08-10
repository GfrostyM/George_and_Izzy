package com.example.georgeandizzy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.georgeandizzy.Adapter.ToDoAdapter;
import com.example.georgeandizzy.Model.ToDoModel;
import com.example.georgeandizzy.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Schedule extends AppCompatActivity implements OnDialogCloseListener{

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;

    private Button backButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        myDB = new DataBaseHelper(Schedule.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB, Schedule.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        addButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
                                         }
                                     });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        backButton1 = findViewById(R.id.backButton1);

        backButton1.setOnClickListener(v -> {
            Intent intent = new Intent(Schedule.this, HomePage.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }
}