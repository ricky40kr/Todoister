package com.bawp.todoister.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.R;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Task> taskList;
    private final OnToDoClickListner todoClickListener;

    public RecyclerViewAdapter(List<Task> taskList, OnToDoClickListner onToDoClickListner) {
        this.taskList = taskList;
        this.todoClickListener =onToDoClickListner;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Task task=taskList.get(position);
        String formatted= Utils.formateDate(task.getDueDate());
        ColorStateList colorStateList=new ColorStateList(new int[][]{
            new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        },new int[]{
                Color.LTGRAY /* disabled*/,
                Utils.priorityColor(task)
        });

        holder.task.setText(task.getTask());
        holder.chip.setText(formatted);
        holder.chip.setTextColor(Utils.priorityColor(task));
        holder.chip.setChipIconTint(colorStateList);

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public AppCompatRadioButton radioButton;
        public AppCompatTextView task;
        public Chip chip;
        OnToDoClickListner onToDoClickListner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.todo_radio_button);
            task=itemView.findViewById(R.id.todo_row_todo);
            chip=itemView.findViewById(R.id.todo_row_chip);
            this.onToDoClickListner= todoClickListener;

            // on clicking a row
            itemView.setOnClickListener(this);

            // on click radio button in list
            radioButton.setOnClickListener(this);

        }

//        on click this function will be called
        @Override
        public void onClick(View view) {
            int id=view.getId();
            Task currTask=taskList.get(getAdapterPosition());

            if (id==R.id.todo_row_layout){
                onToDoClickListner.onToDoCLick(getAdapterPosition(), currTask);
            }
            else if (id==R.id.todo_radio_button){
                onToDoClickListner.onTodoRadioButtonClick(currTask, getAdapterPosition());
            }
        }
    }
}
