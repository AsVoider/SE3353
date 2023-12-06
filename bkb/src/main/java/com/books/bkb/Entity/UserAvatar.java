package com.books.bkb.Entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@Document(collection = "userAvatar")
public class UserAvatar {
    @Id
    @Field("_id")
    private String id;
    @Field("username")
    @Indexed
    private Integer userId;
    @Field("avatar")
    private String avatar;
}
