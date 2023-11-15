package com.books.bkb.Entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Entity
@Table(name = "book")
@DynamicInsert
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn;

    @Column(name = "author")
    private String authors;

    @Column(name = "type")
    private String types;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishTime;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "is_exist", nullable = false)
    private Integer isExist;

    @Column(name = "brief", length = 500)
    private String brief;

    @Column(name = "src")
    private String src;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsExist(Integer isExist) {
        this.isExist = isExist;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    // getters and setters
    public String[] getAuthorsArray() {
        if (authors == null) {
            return null;
        }
        return authors.split(",");
    }

    public String[] getTypesArray() {
        if (types == null) {
            return null;
        }
        return types.split(",");
    }
}