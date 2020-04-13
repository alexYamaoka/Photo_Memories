package com.example.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.photo_memories.model.Memory;
import com.example.photo_memories.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity
{
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ImageView postImage;
    private TextView location;
    private TextView date;
    private TextView description;
    private ImageView back;
    //private TextView edit;
    private ImageView settings;
    BitmapDrawable drawable;
    Bitmap bitmap;



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
        //edit = findViewById(R.id.edit);
        settings = findViewById(R.id.settings);



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



        settings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(PostDetailsActivity.this, v);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.edit:
                                Intent intent = new Intent(PostDetailsActivity.this, EditPhotoActivity.class);
                                intent.putExtra("postId", postIdAsString);
                                intent.putExtra("date", date.getText());
                                startActivity(intent);
                                return true;

                            case R.id.save_photo:

                                //call back after permission granted
                                PermissionListener permissionlistener = new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        Toast.makeText(PostDetailsActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                                        drawable = (BitmapDrawable) postImage.getDrawable();
                                        bitmap = drawable.getBitmap();

                                        FileOutputStream outputStream = null;
                                        File sdCard = Environment.getExternalStorageDirectory();
                                        File directory = new File(sdCard.getAbsolutePath() + "/Memories");
                                        directory.mkdir();
                                        String fileName = String.format("%d.jpg", System.currentTimeMillis());
                                        File outFile = new File(directory, fileName);


                                        Toast.makeText(PostDetailsActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();

                                        try
                                        {
                                            outputStream = new FileOutputStream(outFile);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                            outputStream.flush();
                                            outputStream.close();
                                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

                                            intent.setData(Uri.fromFile(outFile));
                                            sendBroadcast(intent);
                                            
                                        }
                                        catch (FileNotFoundException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onPermissionDenied(List<String> deniedPermissions) {
                                        Toast.makeText(PostDetailsActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                                    }


                                };

                                //check all needed permissions together
                                TedPermission.with(PostDetailsActivity.this)
                                        .setPermissionListener(permissionlistener)
                                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .check();

                                return true;

                            default:
                                return false;
                        }
                    }
                });

                popupMenu.inflate(R.menu.post_settings);

                popupMenu.show();
            }
        });

    }

}
