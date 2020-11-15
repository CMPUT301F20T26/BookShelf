package com.example.bookshelf;

/**
 * The type Borrow request.
 *
 * A borrow request is when another user requests to borrow the app user's book.
 */
public class BorrowRequest extends UserNotification {
    /**
     * The book.
     */
    Book myBook;
    /**
     * The requester's username.
     */
    String theirUsername;

    /**
     * Accept the borrow request.
     */
    void accept() {};

    /**
     * Decline the borrow request.
     */
    void decline() {};
}
