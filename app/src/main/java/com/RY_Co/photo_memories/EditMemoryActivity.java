package com.RY_Co.photo_memories;

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

import com.RY_Co.photo_memories.model.Memory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditMemoryActivity extends AppCompatActivity
{

    private TextView save;
    private EditText title;
    private EditText location;
    private EditText date;
    private ImageView close;
    private DatePicker datePicker;
    private DatabaseReference reference;
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memory);

        Intent intent = getIntent();
        final String memoryId = intent.getStringExtra("memoryId");
        final String dateAsString = intent.getStringExtra("date");

        save = findViewById(R.id.save);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        close = findViewById(R.id.close);

        datePicker = findViewById(R.id.date_picker);




        String [] dateParts = dateAsString.split("/");
        String month = dateParts[0];
        String day = dateParts[1];
        String year = dateParts[2];

        datePicker.updateDate(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));





        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId).child("Memories").child(memoryId);


        // when user inputs value to edit profile
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



        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                datePicker.updateDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String dateAsString = (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();
                updateMemory(title.getText().toString(), location.getText().toString(), dateAsString);
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
        Toast.makeText(this, "Memory Updated!", Toast.LENGTH_SHORT).show();
    }
}
