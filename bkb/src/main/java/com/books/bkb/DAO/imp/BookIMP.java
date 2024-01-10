package com.books.bkb.DAO.imp;

import com.books.bkb.DAO.inter.BookDAO;
import com.books.bkb.Entity.Book;
import com.books.bkb.Entity.BookType;
import com.books.bkb.Repository.BookRepository;
import com.books.bkb.Repository.TypeRepository;
import com.books.bkb.Service.imply.BookImp;
import com.books.bkb.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
public class BookIMP implements BookDAO {
    BookRepository bookRepository;
    TypeRepository typeRepository;

    @Autowired
    BookIMP(BookRepository bookRepository, TypeRepository typeRepository) {
        this.bookRepository = bookRepository;
        this.typeRepository = typeRepository;
    }
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

    @Override
    public List<Book> findByType(String name) {
        List<BookType> list0 = typeRepository.findBookTypeByTypeNameLike(name);
        HashMap<Integer, Integer> map = new HashMap<>();
        List<Book> res_book = new ArrayList<>();
        for (BookType bookType : list0) {
            for (int j = 0; j < bookType.getBookIds().size(); j++) {
                int id = bookType.getBookIds().get(j);
                map.put(id, 1);
            }
        }

        for (var tp : list0) {
            String nm = tp.getTypeName();
            var sear = typeRepository.findNodeByType(nm);
            var sear2 = typeRepository.findNodeRelatedBookTypesDistance2(nm);
            for (var se : sear) {
                for (int j = 0; j < se.getBookIds().size(); j++) {
                    int id = se.getBookIds().get(j);
                    map.put(id, 1);
                }
            }

            for (var se : sear2) {
                for (int j = 0; j < se.getBookIds().size(); j++) {
                    int id = se.getBookIds().get(j);
                    map.put(id, j);
                }
            }
        }
        for (var id : map.keySet()) {
            res_book.add(findOneBook(id));
            System.out.println(id);
        }

        return res_book;
    }

    @Override
    public void test() {
        // 先删除所有的内容
        typeRepository.deleteAll();
        // 添加书籍类型
        BookType bookType1 = new BookType("高中教辅");
        BookType bookType2 = new BookType("科普");
        BookType bookType3 = new BookType("大学教材");
        BookType bookType4 = new BookType("名著");
        BookType bookType5 = new BookType("杂志");
        BookType bookType6 = new BookType("游戏");
        BookType bookType7 = new BookType("文学");
        System.out.println("here");

        // 数据准备
//        bookType1.addBookID(3);
//        bookType1.addBookID(21);
//        bookType1.addBookID(22);
//
//        bookType2.addBookID(4);
//        bookType2.addBookID(19);
//        bookType2.addBookID(20);
//
//        bookType3.addBookID(5);
//        bookType3.addBookID(7);
//        bookType3.addBookID(8);
//        bookType3.addBookID(9);
//        bookType3.addBookID(10);
//        bookType3.addBookID(11);
//        bookType3.addBookID(12);
//
//        bookType4.addBookID(13);
//        bookType5.addBookID(14);
//        bookType6.addBookID(15);
//        bookType6.addBookID(18);
//        bookType7.addBookID(16);
//        bookType7.addBookID(23);
        var books = findAll();
        for (var book : books) {
            if (Objects.equals(book.getTypes(), bookType1.getTypeName()))
                bookType1.addBookID(book.getId());
            if (Objects.equals(book.getTypes(), bookType2.getTypeName()))
                bookType2.addBookID(book.getId());
            if (Objects.equals(book.getTypes(), bookType3.getTypeName()))
                bookType3.addBookID(book.getId());
            if (Objects.equals(book.getTypes(), bookType4.getTypeName()))
                bookType4.addBookID(book.getId());
            if (Objects.equals(book.getTypes(), bookType5.getTypeName()))
                bookType5.addBookID(book.getId());
            if (Objects.equals(book.getTypes(), bookType6.getTypeName()))
                bookType6.addBookID(book.getId());
            if (Objects.equals(book.getTypes(), bookType7.getTypeName()))
                bookType7.addBookID(book.getId());
        }

        bookType1.add_related(bookType2);
        bookType1.add_related(bookType3);
        bookType1.add_related(bookType4);

        bookType2.add_related(bookType5);
        bookType2.add_related(bookType6);
        bookType3.add_related(bookType5);
        bookType3.add_related(bookType6);
        bookType4.add_related(bookType5);
        //bookType4.add_related(bookType6);

        bookType5.add_related(bookType7);
        bookType6.add_related(bookType7);
        bookType7.add_related(bookType1);


        typeRepository.save(bookType1);
        typeRepository.save(bookType2);
        typeRepository.save(bookType3);
        typeRepository.save(bookType4);
        typeRepository.save(bookType5);
        typeRepository.save(bookType6);
        typeRepository.save(bookType7);
    }

    @Override
    public Book findByName(String name) {
        return  bookRepository.findByTitle(name);
    }
}
