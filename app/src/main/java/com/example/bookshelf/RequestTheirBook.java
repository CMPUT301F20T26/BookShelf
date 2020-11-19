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

        String theirID = helper.getUserID(theirBook.getOwnerUsername());
        String myId = helper.getAppUserID();

        RequestTheirBook rq = new RequestTheirBook();
        rq.theirBook = theirBook;
        rq.bookID = theirBook.getBookID();
        rq.status = RequestStatus.PENDING;
        rq.owner = theirID;
        rq.requester = myId;

        // create new notification, store the ID as notifID
        String[] notifID = new String[1];
        notifID[0] = helper.add("notifications", rq.asMap());

        // add notifID to rq object
        rq.NotificationID = notifID[0];

        // apppend notifID to both the requester (app user) and book owner notifications lists
        helper.append("users",theirID, "notifications", notifID);
        helper.append("users", myId, "notifications", notifID);

        return null;
    }


}
