package com.books.bkb.DAO.imp;

import com.books.bkb.DAO.inter.BookDAO;
import com.books.bkb.Entity.Book;
import com.books.bkb.Repository.BookRepository;
import com.books.bkb.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookIMP implements BookDAO {
    @Autowired
    BookRepository bookRepository;
    @Override
    //@Cacheable(cacheNames = "OneBook", key = "'book-' + #id")
    public Book findOneBook(Integer id) {
        Object p = RedisUtil.get("book-"+id);
        if(p != null) {
            System.out.println(((Book)p).getId() + "这是缓存");
            return (Book) p;
        }
        System.out.println("here" + "这是数据库");
        Book b = bookRepository.findById(id).orElse(null);
        RedisUtil.set("book-" + id, b);
        return b;
    }
    @Override
    //@CacheEvict(cacheNames = "book")
    //@CacheEvict(cacheNames = {"OneBook", "book"}, key = "'book-' + #id")
    public void deleteBook(Integer id) {
        var book = bookRepository.findById(id).orElse(null);
        if(RedisUtil.get("book-" + id) != null) {
            RedisUtil.del("book-" + id);
        }
        if(RedisUtil.get("Book") != null) {
            RedisUtil.del("Book");
        }
        if(book != null) {
            book.setIsExist(-1);
            bookRepository.save(book);
        }
    }
    @Override
    //@CacheEvict(cacheNames = "book", allEntries = true)
    //@CachePut(cacheNames = "OneBook", key = "'book-' + #book.getId()")
    public Book saveBook(Book book) {
        if(RedisUtil.get("book-" + book.getId()) != null) {
            RedisUtil.set("book-" + book.getId(), book);
            System.out.println("缓存修改成功");
        }
        if(RedisUtil.get("Book") != null) {
            RedisUtil.del("Book");
        }
        return bookRepository.save(book);
    }
    @Override
    //@Cacheable(key = "'Book'", cacheNames = "book", sync = true)
    public List<Book> findAll() {
        List<Book> list1 = bookRepository.findAll();
        return list1;
    }

    @Override
    public List<Book> findBooksContains(String string)
    {
        return bookRepository.findByTitleContainingIgnoreCase(string);
    }
}
