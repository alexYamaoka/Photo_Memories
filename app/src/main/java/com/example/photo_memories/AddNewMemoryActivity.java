package com.example.photo_memories;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewMemoryActivity extends AppCompatActivity
{

    private EditText title;
    private EditText location;
    private EditText description;
    private EditText date;
    private Button create;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_memory);


        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        create = findViewById(R.id.create);


        create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String titleAsString = title.getText().toString().trim();
                String locationAsString = location.getText().toString().trim();
                String descriptionAsString = description.getText().toString().trim();
                String dateAsString = date.getText().toString().trim();


                if (TextUtils.isEmpty(titleAsString) || TextUtils.isEmpty(locationAsString) || TextUtils.isEmpty(descriptionAsString) || TextUtils.isEmpty(dateAsString))
                {
                    Toast.makeText(AddNewMemoryActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    finish();
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