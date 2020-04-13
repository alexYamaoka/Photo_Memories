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

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            }
        });



//                save.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //call back after permission granted
//                PermissionListener permissionlistener = new PermissionListener()
//                {
//                    @Override
//                    public void onPermissionGranted()
//                    {
//                        Toast.makeText(PostDetailsActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                        drawable = (BitmapDrawable) postImage.getDrawable();
//                        bitmap = drawable.getBitmap();
//
//                        FileOutputStream outputStream = null;
//                        File sdCard = Environment.getExternalStorageDirectory();
//                        File directory = new File(sdCard.getAbsolutePath() + "/Memories");
//                        directory.mkdir();
//                        String fileName = String.format("%d.jpg", System.currentTimeMillis());
//                        File outFile = new File(directory, fileName);
//
//
//                        Toast.makeText(ShowMemoryActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
//
//                        try
//                        {
//                            outputStream = new FileOutputStream(outFile);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                            outputStream.flush();
//                            outputStream.close();
//                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//
//                            intent.setData(Uri.fromFile(outFile));
//                            sendBroadcast(intent);
//
//                        }
//                        catch (FileNotFoundException e)
//                        {
//                            e.printStackTrace();
//                        }
//                        catch (IOException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions)
//                    {
//                        Toast.makeText(ShowMemoryActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                };
//
//                //check all needed permissions together
//                TedPermission.with(PostDetailsActivity.this)
//                        .setPermissionListener(permissionlistener)
//                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .check();
//            }
//        });

    }
}
