package com.books.bkb.Service;

import com.books.bkb.DTO.BookDTO;
import com.books.bkb.Entity.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookServe {
    String getBooks();
    List<Book> getBooksContains(String string);
    Book GetBookById(Integer id);
    void addBook(Book book);
    void delBook(Integer id);
    void updateBook(Book book);
    List<BookDTO> getAllSales(LocalDate str, LocalDate end);
    List<BookDTO> getUserSales(Integer id, LocalDate str, LocalDate end);
}
