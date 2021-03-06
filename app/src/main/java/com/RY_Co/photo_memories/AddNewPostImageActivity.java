package com.RY_Co.photo_memories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;

public class AddNewPostImageActivity extends AppCompatActivity
{
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    private ImageView close;
    private ImageView imageAdded;
    private TextView post;
    private EditText location;
    private EditText description;
    private DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post_image);


        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        datePicker = findViewById(R.id.date_picker);

        ConnectivityManager cm = (ConnectivityManager) AddNewPostImageActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected)
        {
            Toast.makeText(this, "Internet connection is Required!", Toast.LENGTH_SHORT).show();
        }


        storageReference = FirebaseStorage.getInstance().getReference("Posts");
        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uploadImage();
            }
        });


        PermissionListener permissionlistener = new PermissionListener()
        {
            @Override
            public void onPermissionGranted()
            {
                Toast.makeText(AddNewPostImageActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                try
                {
                    // from image cropper library
                    CropImage.activity()
                            //.setAspectRatio(1,1)
                            .start(AddNewPostImageActivity.this);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions)
            {
                Toast.makeText(AddNewPostImageActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        //check all needed permissions together
        TedPermission.with(AddNewPostImageActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }


    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage()
    {
        if (imageUri != null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation()
            {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (! task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Posts");
                        String postId = reference.push().getKey();

                        Intent intent = getIntent();
                        String memoryId = intent.getStringExtra("MemoryId");

                        String dateAsString = datePicker.getMonth() + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postId", postId);
                        hashMap.put("memoryId", memoryId);
                        hashMap.put("postImage", myUrl);
                        hashMap.put("date", dateAsString);
                        hashMap.put("location", location.getText().toString());
                        hashMap.put("description", description.getText().toString());

                        reference.child(postId).setValue(hashMap);
                        Toast.makeText(AddNewPostImageActivity.this, "Post Added!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(AddNewPostImageActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(AddNewPostImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Upload Cancelled!", Toast.LENGTH_SHORT).show();

            finish();
        }
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
                    hideSoftKeyboard(AddNewPostImageActivity.this);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
