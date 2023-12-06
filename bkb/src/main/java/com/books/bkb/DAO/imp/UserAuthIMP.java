package com.books.bkb.DAO.imp;


import com.books.bkb.DAO.inter.UserAuthDAO;
import com.books.bkb.Entity.UserAuth;
import com.books.bkb.Repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserAuthIMP implements UserAuthDAO {

    @Autowired
    UserAuthRepository userAuthRepository;
    @Override
    public void save(UserAuth userAuth)
    {
        userAuthRepository.save(userAuth);
    }

    @Override
    public UserAuth findByUsername(String username)
    {
        return userAuthRepository.findByUsername(username);
    }

    @Override
    public boolean exist(String username)
    {
        return userAuthRepository.existsByUsername(username);
    }
}
