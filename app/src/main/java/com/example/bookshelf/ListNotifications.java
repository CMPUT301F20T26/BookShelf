package com.example.bookshelf;

import com.google.firebase.firestore.DocumentSnapshot;

public class ListNotifications extends UserNotification{


    public ListNotifications(RequestStatus status, String notifID, String date, String location, String ownerID, String requestID, String bookID){

        this.status = status;
        this.NotificationID = notifID;
        this.date = date;
        this.meetUpLocation = location;
        this.ownerID = ownerID;
        this.requesterID = requestID;
        this.bookID = bookID;
    }

    public ListNotifications(){
    }

    public ListNotifications get(DocumentSnapshot notifDoc){
        final ListNotifications notification = new ListNotifications();
        notification.setNotificationID(notifDoc.getId());

        if(notifDoc.getData().get("bookID") != null) {
            notification.setBookID(notifDoc.getData().get("bookID").toString());
        }
        if(notifDoc.getData().get("date") != null) {
            notification.setDate(notifDoc.getData().get("date").toString());
        }
        if(notifDoc.getData().get("meetUpLocation") != null) {
            notification.setMeetUpLocation(notifDoc.getData().get("meetUpLocation").toString());
        }
        if(notifDoc.getData().get("ownerID") != null) {
            notification.setOwnerID(notifDoc.getData().get("ownerID").toString());
        }
        if(notifDoc.getData().get("requesterID") != null) {
            notification.setBookID(notifDoc.getData().get("requesterID").toString());
        }
        if(notifDoc.getData().get("status") != null) {
            notification.setStatus(notifDoc.getData().get("status").toString());
        }

        return notification;
    }

    public void setStatus(String status){
        if(status == "PENDING") {
            this.status = RequestStatus.PENDING;
        } else if (status == "ACCEPTED") {
            this.status = RequestStatus.ACCEPTED;
        }
    }

    public void setNotificationID(String id){
        this.NotificationID = id;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setMeetUpLocation(String location){
        this.meetUpLocation = location;
    }

    public void setOwnerID(String id){
        this.ownerID = id;
    }

    public void setRequesterID(String id){
        this.requesterID = id;
    }

    public void setBookID(String id){
        this.bookID = id;
    }

    public RequestStatus getStatus(){
        return this.status;
    }

    public String getNotificationID(){
        return this.NotificationID;
    }

    public String getDate(){
        return this.date;
    }

    public String getMeetUpLocation(){
        return this.meetUpLocation;
    }

    public String getOwnerID(){
        return this.ownerID;
    }

    public String getRequesterID(){
        return this.requesterID;
    }

    public String getBookID(){
        return this.bookID;
    }
}
