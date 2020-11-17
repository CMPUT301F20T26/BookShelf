package com.example.bookshelf;

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

}
