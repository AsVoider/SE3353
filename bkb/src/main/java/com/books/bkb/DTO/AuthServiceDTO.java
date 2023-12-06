package com.books.bkb.DTO;

import lombok.Getter;

@Getter
public class AuthServiceDTO {
    Integer id;
    String name;
    String username;
    boolean isAdmin;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
