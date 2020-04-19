package com.example.photo_memories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity
{

    private TextView privacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        privacyPolicy = findViewById(R.id.privacy_policy);

        privacyPolicy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AboutActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
    }
}
