package com.example.photo_memories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPhotoActivity extends AppCompatActivity
{

    private EditText date;
    private EditText location;
    private EditText description;
    private TextView delete;
    private TextView save;
    private ImageView close;

    private DatabaseReference reference;
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        delete = findViewById(R.id.delete);
        save = findViewById(R.id.save);
        close = findViewById(R.id.close);

        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Posts").child(postId);


    }
}
