package com.example.bookshelf;


/**
 * BorrowMyBook. Created when a RequestMyBook is accepted.
 *
 * Represents an agreement to let somebody borrow your book.
 */
public class BorrowMyBook extends UserNotification {
    /**
     * The book.
     */
    Book myBook;
    /**
     * Their username.
     */
    String theirUsername;
    // TODO: GPS LOCATION


    /**
     * Creates a new Borrow Agreement from a Borrow Request
     *
     * @param request the request
     */
    BorrowMyBook(RequestMyBook request)
    {
        myBook = request.myBook;
        theirUsername = request.theirUsername;
        // TODO: prompt GPS location upon accept
    }

    /**
     * Scan book to loan.
     */
    void scanBookToLoan() {};

    /**
     * Scan book to confirm return.
     */
    void scanBookConfirmReturn() {};
}
