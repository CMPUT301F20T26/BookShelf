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
        // declining is just deleting the notification
        FirebaseHelper helper = new FirebaseHelper();
        String[] rm = new String[1];
        rm[0] = this.NotificationID;
        // delete document
        helper.deleteDocument("notifications", this.NotificationID);
        // delete the owner and requester notifications
        helper.removeArrayItem("users", this.ownerID, "notifications", rm);
        helper.removeArrayItem("users", this.requesterID, "notifications", rm);
    };
}
