package com.example.demo.dao;

import com.example.demo.entity.User;

public interface UserDao {

    public User findByUserName(String userName);

    public void save(User user);

}
