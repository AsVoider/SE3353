package com.books.bkb.DAO.inter;

import com.books.bkb.Entity.User;

import java.util.List;

public interface UserDAO {

    User getById(Integer id);
    User save(User user);
    User getByName(String name);
    //void setUserPriority();
    List<User> getUsers();
    User getByUsername(String username);
    Integer findMaxId();
}
