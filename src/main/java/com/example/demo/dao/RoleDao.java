package com.example.demo.dao;

import com.example.demo.entity.Role;

public interface RoleDao {

    Role findRoleByName(String theRoleName);

}