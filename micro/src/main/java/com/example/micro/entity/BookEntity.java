package com.example.micro.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "book", schema = "bookstore", catalog = "")
public class BookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "isbn")
    private String isbn;
    @Basic
    @Column(name = "author")
    private String author;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "price")
    private BigDecimal price;
    @Basic
    @Column(name = "publish_time")
    private Date publishTime;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "is_exist")
    private int isExist;
    @Basic
    @Column(name = "brief")
    private String brief;
    @Basic
    @Column(name = "src")
    private String src;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsExist() {
        return isExist;
    }

    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return id == that.id && isExist == that.isExist && Objects.equals(title, that.title) && Objects.equals(isbn, that.isbn) && Objects.equals(author, that.author) && Objects.equals(type, that.type) && Objects.equals(price, that.price) && Objects.equals(publishTime, that.publishTime) && Objects.equals(description, that.description) && Objects.equals(brief, that.brief) && Objects.equals(src, that.src);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, isbn, author, type, price, publishTime, description, isExist, brief, src);
    }
}
