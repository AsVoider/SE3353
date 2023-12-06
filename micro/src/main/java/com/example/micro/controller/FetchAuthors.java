package com.example.micro.controller;

import com.example.micro.service.BookInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/micro")
public class FetchAuthors {

    BookInterface bookInterface;

    @Autowired
    public FetchAuthors(BookInterface bookInterface) {
        this.bookInterface = bookInterface;
    }

    @GetMapping("/getAuthors/{title}")
    public ResponseEntity<String> getAuthors(@PathVariable String title) {
        var authors = bookInterface.getAuthors(title);
        if (authors != null)
            return ResponseEntity.ok(authors);
        else
            return ResponseEntity.badRequest().body("no such books");
    }
}
