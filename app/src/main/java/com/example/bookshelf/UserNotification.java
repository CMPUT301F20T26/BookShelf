package com.example.bookshelf;

import android.content.res.ObbInfo;

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
    String owner;
    String requester;
    String bookID;

    Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("owner", owner);
        map.put("requester", requester);
        map.put("meetUpLocation", meetUpLocation);
        map.put("date", date);
        map.put("bookID", bookID);
        return map;
    }

}
