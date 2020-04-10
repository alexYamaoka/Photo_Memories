package com.example.photo_memories.Adapters;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photo_memories.model.Post;

import java.util.List;

public class PostAdapter
{
    private Context context;
    private List<Post> postList;


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
    }

}
