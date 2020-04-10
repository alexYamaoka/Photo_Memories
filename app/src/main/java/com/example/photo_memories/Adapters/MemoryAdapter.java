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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        final Memory memory = memoryList.get(position);

        holder.title.setText(memory.getTitle());
        holder.location.setText("Location: " + memory.getLocation());
        holder.date.setText(memory.getDate());

    }

    @Override
    public int getItemCount()
    {
        return memoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView location;
        public TextView date;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
        }
    }

}
