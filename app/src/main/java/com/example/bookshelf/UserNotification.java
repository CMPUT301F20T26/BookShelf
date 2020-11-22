package com.example.bookshelf;

import java.util.HashMap;
import java.util.Map;

/**
 * The abstract type User notification.
 *
 * All Requests and Agreements are UserNotifications. This abstract type contains all the
 * information needed to display a request or agreement in the notifications view.
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

}
