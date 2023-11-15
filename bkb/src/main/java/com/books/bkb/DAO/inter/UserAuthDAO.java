package com.books.bkb.DAO.inter;

import com.books.bkb.Entity.UserAuth;

public interface UserAuthDAO {

    void save(UserAuth userAuth);
    UserAuth findByUsername(String username);
    boolean exist(String username);
}
