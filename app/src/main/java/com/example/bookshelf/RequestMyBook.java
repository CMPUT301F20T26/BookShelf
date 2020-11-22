package com.example.bookshelf;

/**
 * The type RequestMyBook
 *
 * Another user requests to borrow the app user's book.
 */
public class RequestMyBook extends UserNotification {
    /**
     * The book.
     */
    Book myBook;
    /**
     * The borrower's username.
     */
    String theirUsername;

    /**
     * Accept the borrow request.
     */
    void accept() {
        // request -> agreement
        // set GPS location
        // notify borrower
    };

    /**
     * Decline the borrow request.
     */
    void decline() {
        // delete notification in firebase
    };
}
