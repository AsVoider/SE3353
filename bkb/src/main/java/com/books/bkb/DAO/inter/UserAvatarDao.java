package com.books.bkb.DAO.inter;

import com.books.bkb.Entity.UserAvatar;

public interface UserAvatarDao {
    UserAvatar findByUserId(Integer userid);
//    UserAvatar update(String username, String src);
    UserAvatar save(UserAvatar userAvatar);
}
