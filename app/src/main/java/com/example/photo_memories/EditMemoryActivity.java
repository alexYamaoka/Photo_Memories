package com.example.photo_memories;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditMemoryActivity extends AppCompatActivity
{

    private TextView save;
    private TextView delete;
    private EditText title;
    private EditText location;
    private EditText date;
    private ImageView close;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memory);


        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        close = findViewById(R.id.close);

    }
}
