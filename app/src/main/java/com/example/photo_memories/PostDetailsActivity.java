package com.example.photo_memories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PostDetailsActivity extends AppCompatActivity
{
    private ImageView postImage;
    private TextView location;
    private TextView date;
    private TextView description;
    private ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);


        postImage = findViewById(R.id.post_image);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);
        back = findViewById(R.id.back);


        Intent intent = getIntent();
        String postImageAsString = intent.getStringExtra("postImage");
        String locationAsString = intent.getStringExtra("location");
        String dateAsString = intent.getStringExtra("date");
        String descriptionAsString = intent.getStringExtra("description");


        Glide.with(PostDetailsActivity.this).load(postImageAsString).into(postImage);
        location.setText(locationAsString);
        date.setText(dateAsString);
        description.setText(descriptionAsString);



        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               finish();
            }
        });


    }
}
