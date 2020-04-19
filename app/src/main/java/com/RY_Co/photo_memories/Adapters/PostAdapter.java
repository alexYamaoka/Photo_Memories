package com.RY_Co.photo_memories.Adapters;


import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RY_Co.photo_memories.model.Post;

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
