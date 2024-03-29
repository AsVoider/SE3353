package com.books.bkb.DAO.inter;

import com.books.bkb.Entity.Book;
import java.util.List;

public interface BookDAO {
    Book findOneBook(Integer id);
    Book findByName(String name);
    List<Book> findAll();
    Book saveBook(Book book);
    void deleteBook(Integer id);
    List<Book> findBooksContains(String string);
    List<Book> findByType(String name);
    void test();
}
