package com.example.bookshelf;


/**
 * The type Borrow agreement. Created when a BorrowRequest is accepted.
 *
 * The Borrow Agreement is the agreement between the user and another user who requested to borrow
 * their book.
 */
public class BorrowAgreement extends UserNotification {
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
    BorrowAgreement(BorrowRequest request)
    {
        myBook = request.myBook;
        theirUsername = request.theirUsername;
        // TODO: prompt GPS location upon accept
    }

    /**
     * Scan book to loan.
     */
    void scanBookToLoan() {
        // TODO: implement scanBookToLoan
    };

    /**
     * Scan book to confirm return.
     */
    void scanBookConfirmReturn() {
        // TODO: implement scanBookToConfirmReturn
    };
}
