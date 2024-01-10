package com.books.bkb.Controller;

import com.books.bkb.Entity.Book;
import com.books.bkb.Service.inter.BookServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphqlController {
    @Autowired
    BookServe bookServe;

    @QueryMapping
    public Book bookByTitle(@Argument String title) {
        System.out.println(title);
        return bookServe.getBookByName(title);
    }
}
