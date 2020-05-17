package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.user.CrmUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

   User findByUserName(String userName);

   void save(CrmUser crmUser);
}