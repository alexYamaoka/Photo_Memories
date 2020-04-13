package com.example.photo_memories.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photo_memories.Adapters.PhotoAdapter;
import com.example.photo_memories.R;
import com.example.photo_memories.ShowMemoryActivity;
import com.example.photo_memories.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PhotosGridFragment extends Fragment
{
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private List<Post> postList;
    private String memoryId;
    private boolean isViewAll;

    public PhotosGridFragment()
    {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photos_grid, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);       // sets 3 photos for width

        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(), postList);
        recyclerView.setAdapter(photoAdapter);


        memoryId = getArguments().getString("memoryId");
        isViewAll = getArguments().getBoolean("isViewAll");
        getMyPhotos();


        return view;
    }




    private void getMyPhotos()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Posts");


        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                postList.clear();

                if (! isViewAll)
                {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren())
                    {
                        Post post = snapshot.getValue(Post.class);

                        if (post.getMemoryId().equals(memoryId))
                        {
                            postList.add(post);
                        }
                    }
                }
                else
                {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren())
                    {
                        Post post = snapshot.getValue(Post.class);
                        postList.add(post);
                    }
                }

                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
