package com.example.bookshelf;

import java.util.HashMap;
import java.util.Map;

import static com.example.bookshelf.RequestStatus;

/**
 * The abstract type User notification.
 *
 * All Requests and Borrows are UserNotifications. This abstract type contains all the
 * information needed to display a request or borrow in the notifications view.
 *
 * There are four classes which extend UserNotification:
 *  - BorrowMyBook
 *  - BorrowTheirBook
 *  - RequestMyBook
 *  - RequestTheirBook
 * These are the four categories of notifications that the app user will see. Each of the four
 * classes contains additional fields and methods for interacting with the notification.
 */
public abstract class UserNotification {
    /**
     * The Notification text.
     */
    String NotificationText;
    RequestStatus status;
    String NotificationID;
    String date;
    String meetUpLocation;
    String ownerID;
    String requesterID;
    String bookID;
    

    Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status.name());
        map.put("ownerID", ownerID);
        map.put("requesterID", requesterID);
        map.put("meetUpLocation", meetUpLocation);
        map.put("date", date);
        map.put("bookID", bookID);
        return map;
    }

    UserNotification()
    {
        status = RequestStatus.PENDING;
        ownerID = "";
        requesterID = "";
    }

    void updateStatus(RequestStatus newStatus){
        this.status = newStatus;
    }

}
