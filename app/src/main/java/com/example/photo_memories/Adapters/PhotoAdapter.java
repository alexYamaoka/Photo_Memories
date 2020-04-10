package com.example.photo_memories.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photo_memories.R;
import com.example.photo_memories.model.Post;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>
{
    private Context context;
    private List<Post> postList;


    public PhotoAdapter(Context context, List<Post> postList)
    {
        this.context = context;
        this.postList = postList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);

        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final Post post = postList.get(position);

        Glide.with(context).load(post.getPostImage()).into(holder.postImage);

    }

    @Override
    public int getItemCount()
    {
        return postList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView postImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
        }
    }
}
