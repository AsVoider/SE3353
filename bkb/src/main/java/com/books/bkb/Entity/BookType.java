package com.books.bkb.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Node
@Getter
@Setter
public class BookType {
    @GeneratedValue
    @Id
    Long id;
    String typeName;
    List<Integer> bookId;
    @Relationship(type = "relateBooks")
    @JsonBackReference
    public Set<BookType> relate;

    public BookType() {}
    public BookType(String type) {
        this.typeName = type;
    }

    public void add_related(BookType bookType) {
        if (relate == null) {
            relate = new HashSet<>();
        }
        relate.add(bookType);
    }

    public void addBookID(Integer id) {
        if (bookId == null) {
            bookId = new ArrayList<>();
        }
        for (var i : bookId) {
            if (i.equals(id))
                return;
        }
        bookId.add(id);
    }

    public List<Integer> getBookIds() {
        return bookId;
    }
}
