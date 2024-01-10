package com.books.bkb.Service.inter;

import com.books.bkb.DTO.BookDTO;
import com.books.bkb.Entity.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookServe {
    List<Book> getBooks();
    Book getBookByName(String name);
    List<Book> getBooksContains(String string);
    Book GetBookById(Integer id);
    List<BookDTO> getAllSales(LocalDate str, LocalDate end);
    List<BookDTO> getUserSales(Integer id, LocalDate str, LocalDate end);
    List<Book> findByRela(String type);
    void test();
    void addBook(Book book);
    void delBook(Integer id);
    void updateBook(Book book);

}
