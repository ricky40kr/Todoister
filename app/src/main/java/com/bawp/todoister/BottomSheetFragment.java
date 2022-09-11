package com.bawp.todoister;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enterToDo;
    private ImageButton calenderButton;
    private RadioGroup priorityRadioGroup;
    private ImageButton priorityButton;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate;
    private RadioButton low;
    private RadioButton high;
    private RadioButton medium;
    private Priority priority;
    private SharedViewModel sharedViewModel;
    private boolean isEdit;

    Calendar calendar = Calendar.getInstance();

    public BottomSheetFragment() {

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calenderButton = view.findViewById(R.id.today_calendar_button);
        enterToDo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
        low = view.findViewById(R.id.radioButton_low);
        medium = view.findViewById(R.id.radioButton_med);
        high = view.findViewById(R.id.radioButton_high);

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
//        'this' represents onClick() which is passed to setOnClickListener()
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue() != null) {
            isEdit = sharedViewModel.getIsEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            enterToDo.setText(task.getTask());

        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        calenderButton.setOnClickListener(v -> {
                    calenderVisibility();
                    Utils.hideSoftKeyboard(v);
                }
        );

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
//            Log.d("Cal","On Create : -> month "+month+", dayOfMonth -> "+dayOfMonth+", year -> "+year);
        });

        low.setOnClickListener(v -> priority = Priority.LOW);

        medium.setOnClickListener(v -> priority = Priority.MEDIUM);

        high.setOnClickListener(v -> priority = Priority.HIGH);

        saveButton.setOnClickListener(v -> {
            String task = enterToDo.getText().toString().trim();
            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null) {
                Task myTask = new Task(task, priority, dueDate, Calendar.getInstance().getTime(), false);

                if (isEdit) {
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();

                    assert updateTask != null;
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);

                    TaskViewModel.update(updateTask);
                    sharedViewModel.setIsEdit(false);

                }else{
                    TaskViewModel.insert(myTask);
                }
                if (this.isVisible()){
                    this.dismiss();
                }
            } else {
                Toast.makeText(getContext(), "No Task!", Toast.LENGTH_SHORT).show();
            }
        });

        priorityButton.setOnClickListener(v ->priorityVisibility());
    }

    public void calenderVisibility(){
        calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        priorityRadioGroup.setVisibility(View.GONE);
    }

    public void priorityVisibility(){
        priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        calendarGroup.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.today_chip) {
            // Set data for today
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            // i1 -> number of days you want to increment or decrement
            dueDate = calendar.getTime();

        } else if (id == R.id.tomorrow_chip) {
            // Set data
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();

        } else if (id == R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
        }
    }
}