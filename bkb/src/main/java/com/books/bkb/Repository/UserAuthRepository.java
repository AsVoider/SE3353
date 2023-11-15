package com.books.bkb.Repository;

import com.books.bkb.Entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, Integer> {
    UserAuth findByUsername(String username);
    boolean existsByUsername(String username);
}
