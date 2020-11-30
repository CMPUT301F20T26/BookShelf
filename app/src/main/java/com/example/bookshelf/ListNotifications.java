package com.example.bookshelf;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

/** Class for converting notification documents into Objects for ease of data manipulation */

public class ListNotifications extends UserNotification{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String owner;
    private String requester;
    private String book;

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

    /**
     * Return ListNotifications object from a notification document
     *
     * @param notifDoc Document Snapshot
     * @return
     */
    static ListNotifications get(final DocumentSnapshot notifDoc){
        final ListNotifications notification = new ListNotifications();

        if(notifDoc != null){
            notification.setNotificationID(notifDoc.getId());

            if(notifDoc.get("bookID") != null) {
                notification.setBook(notifDoc.get("bookID").toString());
            }
            if(notifDoc.get("date") != null) {
                notification.setDate(notifDoc.get("date").toString());
            }
            if(notifDoc.get("meetUpLocation") != null) {
                notification.setMeetUpLocation(notifDoc.get("meetUpLocation").toString());
            }
            if(notifDoc.get("ownerID") != null) {
                notification.setOwner(notifDoc.get("ownerID").toString());
            }
            if(notifDoc.get("requesterID") != null) {
                notification.setRequester(notifDoc.get("requesterID").toString());
            }
            if(notifDoc.get("status") != null) {
                notification.setStatus(notifDoc.get("status").toString());
            }
            return notification;
        }
        else
        {
            return null;
        }


    }

    /**
     * Sets notification status locally
     *
     * @param status notification status
     */
    public void setStatus(String status){
        if(status.equals("PENDING")) {
            this.status = RequestStatus.PENDING;
        } else if (status.equals("ACCEPTED")) {
            this.status = RequestStatus.ACCEPTED;
        }
    }

    /**
     * Sets notification ID
     *
     * @param id notification id
     */
    public void setNotificationID(String id){
        this.NotificationID = id;
    }

    /**
     * Sets date of notification
     *
     * @param date date of notification
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * Sets meetup location
     *
     * @param location location for meetup
     */
    public void setMeetUpLocation(String location){
        this.meetUpLocation = location;
    }

    /**
     * Sets owner id
     *
     * @param id owner id
     */
    public void setOwner(String id){
        this.ownerID = id;
        db.collection("users")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            setOwnerName(document.getData().get("username").toString());
                        }
                    }
                });
    }

    /**
     * Sets requester id
     *
     * @param id requester id
     */
    public void setRequester(String id){
        this.requesterID = id;
        db.collection("users")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            setRequesterName(document.getData().get("username").toString());
                        }
                    }
                });
    }

    /**
     * Sets book id
     *
     * @param id book id
     */
    public void setBook(String id){
        this.bookID = id;
        db.collection("books")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            setBookName(document.getData().get("title").toString());
                        }
                    }
                });
    }

    /**
     * Sets owner name
     *
     * @param name owner name
     */
    public void setOwnerName(String name) {
        this.owner = name;
    }

    /**
     * Sets requester name
     *
     * @param name requester name
     */
    public void setRequesterName(String name) {
        this.requester = name;
    }

    /**
     * Sets book name
     *
     * @param name book name
     */
    public void setBookName(String name) {
        this.book = name;
    }

    /**
     * Gets notification status
     *
     * @return
     */
    public RequestStatus getStatus(){
        return this.status;
    }

    /**
     * Gets notification id
     *
     * @return
     */
    public String getNotificationID(){
        return this.NotificationID;
    }

    /**
     * Gets notification date
     *
     * @return
     */
    public String getDate(){
        return this.date;
    }

    /**
     * Gets notification meet up location
     *
     * @return
     */
    public String getMeetUpLocation(){
        return this.meetUpLocation;
    }

    /**
     * Gets owner id
     *
     * @return
     */
    public String getOwnerID(){
        return this.ownerID;
    }

    /**
     * Gets requester id
     *
     * @return
     */
    public String getRequesterID(){
        return this.requesterID;
    }

    /**
     * Gets book id
     *
     * @return
     */
    public String getBookID(){
        return this.bookID;
    }

    /**
     * Gets owner name
     *
     * @return
     */
    public String getOwnerName(){return this.owner;}

    /**
     * Gets requester name
     *
     * @return
     */
    public String getRequesterName(){return this.requester;}

    /**
     * Gets book name
     *
     * @return
     */
    public String getBookName(){return this.book;}

}
