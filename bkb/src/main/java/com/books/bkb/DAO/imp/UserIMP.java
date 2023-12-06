package com.books.bkb.DAO.imp;

import com.books.bkb.DAO.inter.UserDAO;
import com.books.bkb.Entity.User;
import com.books.bkb.Repository.UserAuthRepository;
import com.books.bkb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserIMP implements UserDAO {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAuthRepository userAuthRepository;

    @Override
    public User getById(Integer id)
    {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user)
    {
        return userRepository.save(user);
        //userRepository.flush();
    }

    @Override
    public User getByName(String name)
    {
        return userRepository.findByName(name).orElse(null);
    }

    @Override
    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    @Override
    public User getByUsername(String username)
    {
        var auth = userAuthRepository.findByUsername(username);
        return getById(auth.getId());
    }

    @Override
    public Integer findMaxId()
    {
        return userRepository.findMaxUserId();
    }
}
