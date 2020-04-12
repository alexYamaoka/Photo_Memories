package com.example.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photo_memories.model.Memory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class AddNewMemoryActivity extends AppCompatActivity
{

    private EditText title;
    private EditText location;
    private EditText description;
    private EditText date;
    private Button create;
    private DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_memory);


        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        create = findViewById(R.id.create);
        datePicker = findViewById(R.id.date_picker);




        create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String titleAsString = title.getText().toString().trim();
                String locationAsString = location.getText().toString().trim();
                String dateAsString = date.getText().toString().trim();


                if (TextUtils.isEmpty(titleAsString) || TextUtils.isEmpty(locationAsString) || TextUtils.isEmpty(dateAsString))
                {
                    Toast.makeText(AddNewMemoryActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Memory memory = new Memory(titleAsString, locationAsString, dateAsString);

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Memories").child(memory.getId());

                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("id", memory.getId());
                    hashMap.put("title", memory.getTitle());
                    hashMap.put("location", memory.getLocation());
                    hashMap.put("date", memory.getDate());

                    // add the newly created user account to Firebase database
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(AddNewMemoryActivity.this, "Memory created!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(AddNewMemoryActivity.this, "Unable to create Memory", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }





    // 2 functions to hide keyboard after clicking somewhere else
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            View view = getCurrentFocus();

            if (view != null && view instanceof EditText)
            {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    hideSoftKeyboard(AddNewMemoryActivity.this);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
