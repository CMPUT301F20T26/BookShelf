package com.example.bookshelf;

/**
 * The type BorrowTheirBook.
 *
 * Another user agrees to loan you their book.
 */
public class BorrowTheirBook extends UserNotification {
    /**
     * Their book.
     */
    Book theirBook;
    /**
     * The meet up location.
     */
    String theirLocation; // TODO: proper datatype for location

    /**
     * Scan to borrow the other person's book.
     */
    void scanToBorrow() {}

    /**
     * Scan to return the other person's book.
     */
    void scanToReturn() {}
}
