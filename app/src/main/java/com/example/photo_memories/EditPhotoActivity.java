package com.example.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photo_memories.model.Memory;
import com.example.photo_memories.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditPhotoActivity extends AppCompatActivity
{

    private EditText date;
    private EditText location;
    private EditText description;
    private TextView delete;
    private TextView save;
    private ImageView close;
    private DatePicker datePicker;


    private DatabaseReference reference;
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        //date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        //delete = findViewById(R.id.delete);
        save = findViewById(R.id.save);
        close = findViewById(R.id.close);
        datePicker = findViewById(R.id.date_picker);


        final Intent intent = getIntent();
        final String postId = intent.getStringExtra("postId");
        final String dateAsString = intent.getStringExtra("date");


        String [] dateParts = dateAsString.split("/");
        String month = dateParts[0];
        String day = dateParts[1];
        String year = dateParts[2];

        datePicker.updateDate(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));


        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Posts").child(postId);



        // when user inputs value to edit profile
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Post post = dataSnapshot.getValue(Post.class);

                if (post != null)
                {
                    location.setText(post.getLocation());
                    description.setText(post.getDescription());
                }
                else
                {
                    System.out.println("post was null");
                    finish();
                }

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


//        delete.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Posts").child(postId).removeValue();
//                finish();
//                Toast.makeText(EditPhotoActivity.this, "Photo Deleted", Toast.LENGTH_SHORT).show();
//            }
//        });



        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                datePicker.updateDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String dateAsString = (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();
                updatePost(dateAsString, location.getText().toString(), description.getText().toString());
                finish();
            }
        });

    }


    private void updatePost(String date, String location, String description)
    {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("date", date);
        hashMap.put("location", location);
        hashMap.put("description", description);

        reference.updateChildren(hashMap);
    }
}
