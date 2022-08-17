package com.bawp.todoister.adapter;

import com.bawp.todoister.model.Task;

public interface OnToDoClickListner {
    void onToDoCLick(int adapterPosition, Task task);
    void onTodoRadioButtonClick(Task task, int adapterPosition);
}
