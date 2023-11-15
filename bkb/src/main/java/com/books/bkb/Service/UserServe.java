package com.books.bkb.Service;

import com.books.bkb.DTO.UserDTO;
import com.books.bkb.Entity.User;
import com.books.bkb.Entity.UserStat;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface UserServe {
    void createUser();
    ResponseEntity<String> Register(UserDTO userDTO);
    List<User> getUsers();
    void disableUser(Integer id);
    void enableUser(Integer id);
    User findByUsername(String username);
    User findById(Integer id);
    List<UserStat> getQuickBuyers(LocalDate start, LocalDate end);
}
