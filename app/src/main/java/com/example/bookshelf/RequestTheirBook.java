package com.example.bookshelf;

import java.time.Instant;

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
     */
    static void requestNew(Book theirBook){
        final FirebaseHelper helper = new FirebaseHelper();
        final RequestTheirBook rq = new RequestTheirBook(); // new blank object just for ease
        String myId = helper.getAppUserID();                // get current user id

        // populating the RequestTheirBook object fields initially:
        rq.theirBook = theirBook;
        rq.bookID = theirBook.getBookID();
        rq.status = RequestStatus.PENDING;
        rq.requesterID = myId;
        rq.date = Instant.now().toString();

        // create new notification, store the ID as notifID
        final String[] notifID = new String[1];
        notifID[0] = helper.add("notifications", rq.asMap());

        // add notifID to RequestTheirBook object
        rq.NotificationID = notifID[0];


        /*
            Since we need to add the notification to the book owner's notification list, we need to
            look up the book owner's user ID, which requires a firebase call.

            So we set up this IHelper "listener" which will do that upon success of the getUserID()
            call (the last line of code in this method)
        */
        FirebaseHelper.IHelper listener = new FirebaseHelper.IHelper() {
            @Override
            public void onSuccess(Object o) {
                String ownerID = (String) o;
                rq.ownerID = ownerID;
                helper.append("users",ownerID, "notifications", notifID);
                helper.edit("notifications", notifID[0], "ownerID", ownerID);
            }

            @Override
            public void onFailure(Object o) {

            }
        };

        // append notifID to requester notification list (i.e. the app user)
        helper.append("users", myId, "notifications", notifID);

        // finally, getUserID() of the book owner. the callback 'listener' will then
        helper.getUserID(theirBook.getOwnerUsername(), listener);
    }


}
