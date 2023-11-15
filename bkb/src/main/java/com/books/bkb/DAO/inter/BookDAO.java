package com.books.bkb.DAO.inter;

import com.books.bkb.Entity.Book;
import java.util.List;

public interface BookDAO {
    Book findOneBook(Integer id);
    List<Book> findAll();
    Book saveBook(Book book);
    void deleteBook(Integer id);
    List<Book> findBooksContains(String string);
}
