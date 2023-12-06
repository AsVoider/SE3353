package com.example.micro.repo;

import com.example.micro.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<BookEntity, Integer> {
    BookEntity getByTitle(String title);
}
