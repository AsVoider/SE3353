package com.books.bkb.Repository;

import com.books.bkb.Entity.UserAvatar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends MongoRepository<UserAvatar, String> {
    UserAvatar findByUserId(Integer userid);
    // UserAvatar updateByUsernameAndAvatar(String username, String src);

    //UserAvatar updateUserAvatarByUserId(Integer userId, String src);
}
