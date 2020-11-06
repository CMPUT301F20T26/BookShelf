package com.example.bookshelf;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookFactory {

    public void Title(String title) {
        thisBook.setTitle(title);
        bookMap.put("title", title);
    }

    public void Author(String author) {
        thisBook.setAuthor(author);
        bookMap.put("author", author);
    }

    public void ISBN(Long ISBN) {
        thisBook.setISBN(ISBN);
        bookMap.put("isbn", ISBN);
    }

    public void PhotoURL(String photoURL) {
        thisBook.setPhotoURL(photoURL);
        bookMap.put("photoURL", photoURL);
    }

    public void Status(Book.BookStatus status) {
        thisBook.setStatus(status);
        bookMap.put("status", status);
    }

    public void OwnerUsername(String ownerUsername) {
        thisBook.setOwnerUsername(ownerUsername);
        bookMap.put("ownerUsername", ownerUsername);
    }

    public void Description(String description) {
        thisBook.setDescription(description);
        bookMap.put("description", description);
    }

    private Book thisBook;
    Map<String, Object> bookMap;

    BookFactory()
    {
        thisBook = new Book();
        bookMap = new HashMap<>();
    }

    Book build(){
        long now = new Date().getTime();
        String id = String.format("%x", Objects.hash(now, thisBook.getTitle()));
        thisBook.setBookID(id);
        bookMap.put("bookID", id);

        return thisBook;
    }

    static Book scanToCreate() {
        // to implement later...
        return null;
    }

    /*
    static Book buildNow(){

        return null;
    }
     */


}
