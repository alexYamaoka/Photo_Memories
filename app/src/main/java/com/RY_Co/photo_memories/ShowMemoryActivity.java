package com.RY_Co.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.RY_Co.photo_memories.Fragments.PhotosGridFragment;
import com.RY_Co.photo_memories.model.Memory;
import com.RY_Co.photo_memories.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowMemoryActivity extends AppCompatActivity
{

    private TextView title;
    private TextView location;
    private TextView date;
    private FloatingActionButton addNewImage;


    private ImageView edit;
    private ImageView save;
    private ImageView delete;

    private String memoryId;
    private ImageView home;
    //private TextView edit;
    private String dateAsString;
    BitmapDrawable drawable;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memory);


        final Intent intent = getIntent();
        memoryId = intent.getStringExtra("MemoryId");


        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        home = findViewById(R.id.home);
        //edit = findViewById(R.id.edit);
        addNewImage = findViewById(R.id.add_new_photo);

        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);


        Bundle bundle = new Bundle();
        bundle.putString("memoryId", memoryId);
        bundle.putBoolean("isViewAll", false);
        PhotosGridFragment photosGridFragment = new PhotosGridFragment();
        photosGridFragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, photosGridFragment).commit();


        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
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
                    location.setText("Location: " + memory.getLocation());
                    date.setText(memory.getDate());
                    dateAsString = memory.getDate();
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


        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intentEdit = new Intent(ShowMemoryActivity.this, EditMemoryActivity.class);
                intentEdit.putExtra("memoryId", memoryId);
                intentEdit.putExtra("date", dateAsString);
                startActivity(intentEdit);
            }
        });


        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference referencePosts = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Posts");
                referencePosts.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            Post post = snapshot.getValue(Post.class);

                            if (post.getMemoryId().equals(memoryId))
                            {
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Posts").child(post.getPostId()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });


               FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Memories").child(memoryId).removeValue();
                finish();
            }
        });


    }

}
