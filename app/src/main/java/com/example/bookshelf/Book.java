package com.example.bookshelf;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Book.
 */
public class Book implements Serializable {
    private String Title;
    private String Author;
    private Long ISBN;
    private String photoURL;
    private BookStatus Status;
    private String ownerUsername;
    private String description;
    /**
     * The Id number.
     */
    int id_number = ThreadLocalRandom.current().nextInt();

    /**
     * The enum Book status.
     */
    public enum BookStatus{
        /**
         * Available book status.
         */
        Available,
        /**
         * Requested book status.
         */
        Requested,
        /**
         * Accepted book status.
         */
        Accepted,
        /**
         * Borrowed book status.
         */
        Borrowed,
        /**
         * Loaned book status.
         */
        Loaned};

    /**
     * Instantiates a new Book.
     *
     * @param title       the title
     * @param author      the author
     * @param ISBN        the isbn
     * @param photoURL    url to book cover
     * @param description short  description
     */
    public Book(String title, String author, Long ISBN, String photoURL, String description) {
        this.Title = title;
        this.Author = author;
        this.ISBN = ISBN;
        this.Status = BookStatus.Available;
        this.photoURL = photoURL;
        this.description = description;
    }

    /**
     * Instantiates a new Book.
     *
     * @param title    the title
     * @param author   the author
     * @param ISBN     the isbn
     * @param photoURL url to book cover
     */
    public Book(String title, String author, Long ISBN, String photoURL) {
        Title = title;
        Author = author;
        this.ISBN = ISBN;
        this.photoURL = photoURL;
        this.Status = BookStatus.Available;
    }

    /**
     * Instantiates a new Book.
     *
     * @param title  the title
     * @param author the author
     * @param ISBN   the isbn
     */
    public Book(String title, String author, Long ISBN) {
        this.Title = title;
        this.Author = author;
        this.ISBN = ISBN;
        this.Status = BookStatus.Available;
    }

    /**
     * Instantiates a new Book.
     *
     * @param title       the title
     * @param author      the author
     * @param description the description
     * @param ISBN        the isbn
     */
    public Book(String title, String author, String description, Long ISBN) {
        Title = title;
        Author = author;
        this.description = description;
        this.ISBN = ISBN;
        this.Status = BookStatus.Available;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return Author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        Author = author;
    }

    /**
     * Gets isbn.
     *
     * @return the isbn
     */
    public Long getISBN() {
        return ISBN;
    }

    /**
     * Sets isbn.
     *
     * @param ISBN the isbn
     */
    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Gets photo url.
     *
     * @return the photo url
     */
    public String getPhotoURL() {
        return photoURL;
    }

    /**
     * Sets photo url.
     *
     * @param photoURL the photo url
     */
    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public BookStatus getStatus() {
        return Status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(BookStatus status) {
        Status = status;
    }

    /**
     * Gets owner username.
     *
     * @return the owner username
     */
    public String getOwnerUsername() {
        return ownerUsername;
    }

    /**
     * Sets owner username.
     *
     * @param ownerUsername the owner username
     */
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
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

