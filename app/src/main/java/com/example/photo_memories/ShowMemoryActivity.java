package com.example.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photo_memories.Adapters.PhotoAdapter;
import com.example.photo_memories.Fragments.PhotosGridFragment;
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

    private TextView title;
    private TextView location;
    private TextView description;
    private TextView date;
    private FloatingActionButton addNewImage;
    private ImageView optionsMenu;

    private String memoryId;
    private ImageView home;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memory);

        Intent intent = getIntent();
        memoryId = intent.getStringExtra("MemoryId");



        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        optionsMenu = findViewById(R.id.options_menu);
        home = findViewById(R.id.home);
        addNewImage = findViewById(R.id.add_new_photo);


        Bundle bundle = new Bundle();
        bundle.putString("memoryId", memoryId);
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



        optionsMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(ShowMemoryActivity.this, v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.view_all:
                                return true;

                            case R.id.logout:
                                finish();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(ShowMemoryActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                return true;

                            default:
                                return false;
                        }
                    }
                });

                popupMenu.inflate(R.menu.options_menu);

                popupMenu.show();
            }
        });

    }

}
