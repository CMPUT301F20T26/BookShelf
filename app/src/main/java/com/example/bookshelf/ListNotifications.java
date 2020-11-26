package com.example.bookshelf;

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

    public void setStatus(RequestStatus status){
        this.status = status;
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
