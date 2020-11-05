package com.example.bookshelf;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Book implements Serializable {
    private String Title;
    private String Author;
    private Long ISBN;
    private String photoURL;
    private BookStatus Status;
    private String ownerUsername;
    private String description;
    int id_number = ThreadLocalRandom.current().nextInt();

    public enum BookStatus{Available, Requested, Accepted, Borrowed, Loaned};

        public Book(String title, String author, Long ISBN, String photoURL, String description) {
        this.Title = title;
        this.Author = author;
        this.ISBN = ISBN;
        this.Status = BookStatus.Available;
        this.photoURL = photoURL;
        this.description = description;
    }

    public Book(String title, String author, Long ISBN, String photoURL) {
        Title = title;
        Author = author;
        this.ISBN = ISBN;
        this.photoURL = photoURL;
        this.Status = BookStatus.Available;
    }

    public Book(String title, String author, Long ISBN) {
        this.Title = title;
        this.Author = author;
        this.ISBN = ISBN;
        this.Status = BookStatus.Available;
    }
    public Book(String title, String author, String description, Long ISBN) {
        Title = title;
        Author = author;
        this.description = description;
        this.ISBN = ISBN;
        this.Status = BookStatus.Available;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public Long getISBN() {
        return ISBN;
    }

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public BookStatus getStatus() {
        return Status;
    }

    public void setStatus(BookStatus status) {
        Status = status;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Title.equals(book.Title) &&
                Author.equals(book.Author) &&
                ISBN.equals(book.ISBN) &&
                ownerUsername.equals(book.ownerUsername) &&
                (id_number==book.id_number)&&
                Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Title, ISBN, id_number);
    }
}

