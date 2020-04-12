package com.example.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.photo_memories.model.Memory;
import com.example.photo_memories.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDetailsActivity extends AppCompatActivity
{
    private ImageView postImage;
    private TextView location;
    private TextView date;
    private TextView description;
    private ImageView back;
    private TextView edit;



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
        edit = findViewById(R.id.edit);


        Intent intent = getIntent();
        final String postIdAsString = intent.getStringExtra("postId");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Posts").child(postIdAsString);
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Post post = dataSnapshot.getValue(Post.class);

                if (post != null)
                {
                    Glide.with(PostDetailsActivity.this).load(post.getPostImage()).into(postImage);
                    date.setText(post.getDate());
                    location.setText("Location: " + post.getLocation());
                    description.setText(post.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });



        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               finish();
            }
        });


        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(PostDetailsActivity.this, EditPhotoActivity.class);
                intent.putExtra("postId", postIdAsString);
                intent.putExtra("date", date.getText());
                startActivity(intent);
            }
        });
    }
}
