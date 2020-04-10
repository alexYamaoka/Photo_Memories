package com.example.photo_memories.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photo_memories.R;
import com.example.photo_memories.model.Memory;

import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder>
{
    private Context context;
    private List<Memory> memoryList;

    public MemoryAdapter(Context context, List<Memory> memoryList)
    {
        this.context = context;
        this.memoryList = memoryList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.memory_item, parent, false);

        return new MemoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return memoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView date;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }
}
