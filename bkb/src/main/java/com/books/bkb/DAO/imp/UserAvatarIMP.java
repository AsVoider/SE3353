package com.books.bkb.DAO.imp;

import com.books.bkb.DAO.inter.UserAvatarDao;
import com.books.bkb.Entity.UserAvatar;
import com.books.bkb.Repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserAvatarIMP implements UserAvatarDao {
    AvatarRepository avatarRepository;

    @Autowired
    public UserAvatarIMP(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

//    @Override
//    public UserAvatar findByUsername(Integer userid) {
//        return avatarRepository.findUserAvatarsByUsername(username);
//    }


    @Override
    public UserAvatar findByUserId(Integer userid) {
        return avatarRepository.findUserAvatarsByUserId(userid);
    }

    @Override
    public UserAvatar save(UserAvatar userAvatar) {
        return avatarRepository.save(userAvatar);
    }

//    @Override
//    public UserAvatar update(String username, String src) {
//        return avatarRepository.updateByUsernameAndAvatar(username, src);
//    }
}
