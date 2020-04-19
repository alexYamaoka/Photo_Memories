package com.RY_Co.photo_memories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.RY_Co.photo_memories.model.Post;
import com.github.chrisbanes.photoview.PhotoView;
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
    private PhotoView postImage;
    private TextView location;
    private TextView date;
    private TextView description;
    private ImageView back;


    private ImageView edit;
    private ImageView save;
    private ImageView delete;
    //private ImageView settings;
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
        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);
        //settings = findViewById(R.id.settings);



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

                    if (post.getLocation() == null || post.getLocation().equals(""))
                    {
                        location.setText(post.getLocation());
                    }
                    else
                    {
                        location.setText("Location: " + post.getLocation());
                    }

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

        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Posts").child(postIdAsString).removeValue();
                finish();
                Toast.makeText(PostDetailsActivity.this, "Photo Deleted", Toast.LENGTH_SHORT).show();
            }
        });


        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //call back after permission granted
                PermissionListener permissionlistener = new PermissionListener()
                {
                    @Override
                    public void onPermissionGranted()
                    {
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
                    public void onPermissionDenied(List<String> deniedPermissions)
                    {
                        Toast.makeText(PostDetailsActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };

                //check all needed permissions together
                TedPermission.with(PostDetailsActivity.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });
    }
}
