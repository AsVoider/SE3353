package com.books.bkb.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_auth")
public class UserAuth {
    @Id
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
