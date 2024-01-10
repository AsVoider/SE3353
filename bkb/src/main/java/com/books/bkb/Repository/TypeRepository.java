package com.books.bkb.Repository;

import com.books.bkb.Entity.Book;
import com.books.bkb.Entity.BookType;
import io.lettuce.core.dynamic.annotation.Param;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface TypeRepository extends Neo4jRepository<BookType, Long> {
    @Query("MATCH (a:BookType)-[:relateBooks]->(b) " +
            "WHERE a.typeName = $name " +
            "RETURN b ")
    List<BookType> findNodeByType(@Param("name") String name);

    @Query("MATCH (a:BookType)-[:relateBooks]->(b)-[:relateBooks]->(c) " +
            "WHERE a.typeName = $name " +
            "RETURN c "
    )
    List<BookType> findNodeRelatedBookTypesDistance2(@Param("name") String name);

    List<BookType> findBookTypeByTypeNameLike(String name);
    @NotNull List<BookType> findAll();
}
