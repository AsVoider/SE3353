package com.books.bkb.Service.imply;

import com.books.bkb.DAO.inter.BookDAO;
import com.books.bkb.DAO.inter.OrderDAO;
import com.books.bkb.DAO.inter.OrderItemDAO;
import com.books.bkb.DTO.BookDTO;
import com.books.bkb.Entity.Book;
import com.books.bkb.Service.BookServe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class BookImp implements BookServe {

    BookDAO bookDAO;
    OrderDAO orderDAO;
    OrderItemDAO orderItemDAO;

    @Autowired
    public BookImp(BookDAO bookDAO, OrderDAO orderDAO, OrderItemDAO orderItemDAO) {
        this.bookDAO = bookDAO;
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
    }

    @Override
    public String getBooks()
    {
        List<Book> books = bookDAO.findAll();
        List<Map<String, Object>> bookList = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> bookMap = new HashMap<>();
            bookMap.put("id", book.getId());
            bookMap.put("title", book.getTitle());
            bookMap.put("isbn", book.getIsbn());
            bookMap.put("authors", Arrays.asList(book.getAuthors().split(",")));
            bookMap.put("types", Arrays.asList(book.getTypes().split(",")));
            bookMap.put("price", book.getPrice());
            bookMap.put("publishTime", book.getPublishTime());
            System.out.println(book.getPublishTime());
            bookMap.put("description", book.getDescription());
            bookMap.put("isExist", book.getIsExist());
            bookMap.put("brief", book.getBrief());
            bookMap.put("src", book.getSrc());
            bookList.add(bookMap);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(bookList);
        } catch (JsonProcessingException e) {
            e.fillInStackTrace();
            return "";
        }
    }
    @Override
    public List<Book> getBooksContains(String string)
    {
        return bookDAO.findBooksContains(string);
    }
    @Override
    public Book GetBookById(Integer id)
    {
        System.out.println("111");
        return bookDAO.findOneBook(id);
    }
    @Override
    public void addBook(Book book)
    {
        book.setId(null);
        bookDAO.saveBook(book);
    }
    @Override
    public void delBook(Integer id)
    {
        bookDAO.deleteBook(id);
    }
    @Override
    public void updateBook(Book book)
    {
        bookDAO.saveBook(book);
    }
    @Override
    public List<BookDTO> getAllSales(LocalDate str, LocalDate end)
    {
        var startDate = Date.from(str.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var endDate = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var orders = orderDAO.getAll();
        var sales = new HashMap<String, BookDTO>();
        for(var order : orders)
        {
            var date = order.getBuyTime();
            if(!(date.after(startDate) && date.before(endDate)))
                continue;
            for(var orderitem : orderItemDAO.findByOrderId(order.getId()))
            {
                var title = orderitem.getBook().getTitle();
                var num = orderitem.getNum();
                var price = orderitem.getPrice();
                if(sales.containsKey(title))
                {
                    var bookDTO = sales.get(title);
                    bookDTO.setNum(bookDTO.getNum() + num);
                    bookDTO.setPrice(bookDTO.getPrice().add(price.multiply(BigDecimal.valueOf(num))));
                }
                else
                {
                    var bookDTO = new BookDTO();
                    bookDTO.setTitle(title);
                    bookDTO.setNum(num);
                    bookDTO.setPrice(price.multiply(BigDecimal.valueOf(num)));
                    sales.put(title, bookDTO);
                }
            }
        }
        return new ArrayList<>(sales.values());
    }
    @Override
    public List<BookDTO> getUserSales(Integer id, LocalDate str, LocalDate end)
    {
        var startDate = Date.from(str.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var endDate = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
        var orders = orderDAO.getUserAllOrder(id);
        var sales = new HashMap<String, BookDTO>();
        for(var order : orders)
        {
            var date = order.getBuyTime();
            if(!(date.after(startDate) && date.before(endDate)))
                continue;
            for(var orderitem: orderItemDAO.findByOrderId(order.getId()))
            {
                var title = orderitem.getBook().getTitle();
                var num = orderitem.getNum();
                var price = orderitem.getPrice();
                if(sales.containsKey(title))
                {
                    var bookDTO = sales.get(title);
                    bookDTO.setNum(bookDTO.getNum() + num);
                    bookDTO.setPrice(bookDTO.getPrice().add(price.multiply(BigDecimal.valueOf(num))));
                }
                else
                {
                    var bookDTO = new BookDTO();
                    bookDTO.setTitle(title);
                    bookDTO.setNum(num);
                    bookDTO.setPrice(price.multiply(BigDecimal.valueOf(num)));
                    sales.put(title, bookDTO);
                }
            }
        }
        return new ArrayList<>(sales.values());
    }
}
