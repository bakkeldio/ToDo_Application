package com.example.demo.dao;

import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    public List<Task> findAllByStateAndUser(int state, User user);

    public void deleteAllByState(int state);
}
