package com.books.bkb.Repository;

import com.books.bkb.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
    @Query("SELECT MAX(u.id) FROM User u")
    Integer findMaxUserId();
    Optional<User> findById(Integer id);
}
