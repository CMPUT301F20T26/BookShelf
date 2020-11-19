package com.example.bookshelf;

/**
 * The type RequestTheirBook
 *
 * The app user asks to borrow another user's book.
 */
public class RequestTheirBook extends UserNotification {
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
    static RequestTheirBook requestNew(Book theirBook){
        // creates new notification, which should appear in both users' notification lists
        // RequestStatus pending
        FirebaseHelper helper = new FirebaseHelper();
        RequestTheirBook rq = new RequestTheirBook();
        rq.theirBook = theirBook;
        rq.bookID = theirBook.getBookID();
        rq.status = RequestStatus.PENDING;

        String ID = helper.add("notifications", rq.asMap());
        return null;
    }


}
