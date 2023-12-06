package com.example.micro.service;

import com.example.micro.entity.BookEntity;

public interface BookInterface {
    BookEntity get(String title);

    String getAuthors(String title);
}
