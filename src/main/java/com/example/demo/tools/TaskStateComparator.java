package com.example.demo.tools;

import com.example.demo.entity.Task;

import java.util.Comparator;

public class TaskStateComparator implements Comparator<Task> {

    @Override
    public int compare(Task task, Task t1) {
        return task.getState() - t1.getState();
    }
}