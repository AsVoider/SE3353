package com.example.micro.service;

import com.example.micro.entity.BookEntity;
import com.example.micro.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookImp implements BookInterface{

    BookRepo bookRepo;

    @Autowired
    public BookImp(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public BookEntity get(String title) {
        return bookRepo.getByTitle(title);
    }

    @Override
    public String getAuthors(String title) {
        var book = get(title);
        return book != null ? book.getAuthor() : null;
    }
}
