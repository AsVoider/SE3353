package com.books.bkb.DAO.imp;

import com.books.bkb.DAO.inter.BookDAO;
import com.books.bkb.Entity.Book;
import com.books.bkb.Entity.BookType;
import com.books.bkb.Repository.BookRepository;
import com.books.bkb.Repository.TypeRepository;
import com.books.bkb.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class BookIMP implements BookDAO {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    TypeRepository typeRepository;
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
        bookType1.addBookID(1);
        bookType1.addBookID(21);
        bookType1.addBookID(22);

        bookType2.addBookID(2);
        bookType2.addBookID(18);
        bookType2.addBookID(20);

        bookType3.addBookID(3);
        bookType3.addBookID(5);
        bookType3.addBookID(6);
        bookType3.addBookID(7);
        bookType3.addBookID(8);
        bookType3.addBookID(9);
        bookType3.addBookID(10);

        bookType4.addBookID(11);
        bookType5.addBookID(12);
        bookType6.addBookID(13);
        bookType6.addBookID(17);
        bookType7.addBookID(14);
        bookType7.addBookID(23);

        bookType1.add_related(bookType2);
        bookType1.add_related(bookType3);
        bookType1.add_related(bookType4);

        bookType2.add_related(bookType5);
        bookType2.add_related(bookType6);
        bookType3.add_related(bookType5);
        bookType3.add_related(bookType6);
        bookType4.add_related(bookType5);
        bookType4.add_related(bookType6);

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
//        var l = typeRepository.findAll();
//        for (var ls : l) {
//            System.out.println(ls.getTypeName());
//        }
    }
}
