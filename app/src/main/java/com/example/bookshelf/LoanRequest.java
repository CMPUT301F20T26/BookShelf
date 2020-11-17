package com.example.bookshelf;

/**
 * The type Loan request.
 *
 * A loan request is when you ask to borrow another user's book.
 */
public class LoanRequest extends UserNotification {
    /**
     * Their book.
     */
    Book theirBook; // should contain owner username field (?)

    /**
     * Make new loan request.
     *
     * @param theirBook their book
     * @return the loan request
     */
    static LoanRequest makeNew(Book theirBook){
        // creates new notification, which should appear in both users' notification lists
        // RequestStatus pending
        return null;
    }

}
