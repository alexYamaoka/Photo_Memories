package com.example.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.photo_memories.Adapters.MemoryAdapter;
import com.example.photo_memories.model.Memory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private MemoryAdapter memoryAdapter;
    private List<Memory> memoryList;
    private FloatingActionButton addNewMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNewMemory = findViewById(R.id.add_new_memory);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        loadMemories();

        for (Memory m: memoryList)
        {
            System.out.println("Title: " + m.getTitle());
            System.out.println("Location: " + m.getLocation());
        }


        memoryAdapter = new MemoryAdapter(this, memoryList);
        recyclerView.setAdapter(memoryAdapter);



        addNewMemory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, AddNewMemoryActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loadMemories()
    {
        memoryList = new ArrayList<>();

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("Memories");

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                memoryList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Memory memory = snapshot.getValue(Memory.class);
                    memoryList.add(memory);
                }

                memoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }
}
