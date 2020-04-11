package com.example.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photo_memories.model.Memory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditMemoryActivity extends AppCompatActivity
{

    private TextView save;
    private TextView delete;
    private EditText title;
    private EditText location;
    private EditText date;
    private ImageView close;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memory);

        Intent intent = getIntent();
        final String memoryId = intent.getStringExtra("memoryId");

        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        close = findViewById(R.id.close);


        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Memories").child(memoryId);


        // when user inputs value to edit profile
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Memory memory = dataSnapshot.getValue(Memory.class);

                title.setText(memory.getTitle());
                location.setText(memory.getLocation());
                date.setText(memory.getDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });



        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateMemory(title.getText().toString(), location.getText().toString(), date.getText().toString());
                finish();
            }
        });


    }


    private void updateMemory(String title, String location, String date)
    {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("title", title);
        hashMap.put("location", location);
        hashMap.put("date", date);

        reference.updateChildren(hashMap);
    }
}
