package com.books.bkb.Controller;

import com.books.bkb.DTO.BookDTO;
import com.books.bkb.Entity.Book;
import com.books.bkb.Service.inter.BookServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class BookController {
    BookServe bookServe;

    @Autowired
    BookController(BookServe bookServe) {
        this.bookServe = bookServe;
    }

    @GetMapping("/public/Books")
    @ResponseBody
    public List<Book> getBooks()
    {
        return bookServe.getBooks();
    }

    @GetMapping("/public/Books/search/{string}")
    @ResponseBody
    public List<Book> getBooksContains(@PathVariable String string)
    {
        System.out.println(string);
        return bookServe.getBooksContains(string);
    }

    @GetMapping("/public/Books/{id}")
    @ResponseBody
    public Book getBookById(@PathVariable Integer id)
    {
        return bookServe.GetBookById(id);
    }

    @PostMapping("/admin/add")
    @ResponseBody
    public void addBook(@RequestBody Book book)
    {
        bookServe.addBook(book);
    }

    @DeleteMapping ("/public/Book/del/{id}")
    @ResponseBody
    public void delBook(@PathVariable Integer id)
    {
        bookServe.delBook(id);
    }

    @PostMapping("/admin/update")
    @ResponseBody
    public void updateBook(@RequestBody Book book)
    {
        bookServe.updateBook(book);
    }

    @GetMapping("/public/sales/{admin}/{id}")
    @ResponseBody
    public List<BookDTO> getSales(@PathVariable Integer admin, @PathVariable Integer id,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end)
    {
        if(admin != 0)
        {
            return bookServe.getAllSales(start.toLocalDate(), end.toLocalDate());
        }
        else
        {
            return bookServe.getUserSales(id, start.toLocalDate(), end.toLocalDate());
        }
    }

    @RequestMapping("/neo4j")
    public void testNeo4j(){
        bookServe.test();
        System.out.println("here1");
    }

    @GetMapping("/public/neo/{name}")
    public ResponseEntity<?> getRelations(@PathVariable String name) {
        return ResponseEntity.ok(bookServe.findByRela(name));
    }

    @GetMapping("/public/booktest/{name}")
    @ResponseBody
    public Book findByName(@PathVariable String name) {
        System.out.println(name + "here");
        return bookServe.getBookByName(name);
    }

}
