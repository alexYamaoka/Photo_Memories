package com.example.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photo_memories.Adapters.PhotoAdapter;
import com.example.photo_memories.model.Memory;
import com.example.photo_memories.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShowMemoryActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private TextView title;
    private TextView location;
    private TextView description;
    private TextView date;
    private FloatingActionButton addNewImage;
    private PhotoAdapter photoAdapter;
    private List<Post> postList;
    private String memoryId;
    private ImageView home;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memory);

        Intent intent = getIntent();
        memoryId = intent.getStringExtra("MemoryId");


        recyclerView = findViewById(R.id.recycler_view);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);

        home = findViewById(R.id.home);
        addNewImage = findViewById(R.id.add_new_photo);





        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(ShowMemoryActivity.this, 3);       // sets 3 photos for width
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(ShowMemoryActivity.this, postList);
        recyclerView.setAdapter(photoAdapter);

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ShowMemoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Memories").child(memoryId);
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Memory memory = dataSnapshot.getValue(Memory.class);

                if (memory != null)
                {
                    title.setText(memory.getTitle());
                    location.setText(memory.getLocation());
                    date.setText(memory.getDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        addNewImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intentAddPost = new Intent(ShowMemoryActivity.this, AddNewPostImageActivity.class);
                intentAddPost.putExtra("MemoryId", memoryId);
                startActivity(intentAddPost);
            }
        });




        getMyPhotos();
    }



    private void getMyPhotos()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Posts");


        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                postList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);

                    System.out.println("post location: " + post.getLocation());
                    System.out.println("post description: " + post.getDescription());
                    System.out.println("post memory id: " + post.getMemoryId());
                    if (post.getMemoryId().equals(memoryId))
                    {
                        postList.add(post);
                    }

                }

                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
