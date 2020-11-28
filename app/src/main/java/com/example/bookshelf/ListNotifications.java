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

    public ListNotifications get(DocumentSnapshot notifDoc){
        final ListNotifications notification = new ListNotifications();
        notification.setNotificationID(notifDoc.getId());

        if(notifDoc.getData().get("bookID") != null) {
            notification.setBook(notifDoc.getData().get("bookID").toString());
        }
        if(notifDoc.getData().get("date") != null) {
            notification.setDate(notifDoc.getData().get("date").toString());
        }
        if(notifDoc.getData().get("meetUpLocation") != null) {
            notification.setMeetUpLocation(notifDoc.getData().get("meetUpLocation").toString());
        }
        if(notifDoc.getData().get("ownerID") != null) {
            notification.setOwner(notifDoc.getData().get("ownerID").toString());
        }
        if(notifDoc.getData().get("requesterID") != null) {
            notification.setRequester(notifDoc.getData().get("requesterID").toString());
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

    public void setOwner(String id){
        this.ownerID = id;
        db.collection("users")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Book Document
                            DocumentSnapshot document = task.getResult();
                            setOwnerName(document.getData().get("username").toString());
                        }
                    }
                });
    }

    public void setRequester(String id){
        this.requesterID = id;
        db.collection("users")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Book Document
                            DocumentSnapshot document = task.getResult();
                            setRequesterName(document.getData().get("username").toString());
                        }
                    }
                });
    }

    public void setBook(String id){
        this.bookID = id;
        db.collection("books")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Book Document
                            DocumentSnapshot document = task.getResult();
                            setBookName(document.getData().get("title").toString());
                        }
                    }
                });
    }

    public void setOwnerName(String name) {
        this.owner = name;
    }

    public void setRequesterName(String name) {
        this.requester = name;
    }

    public void setBookName(String name) {
        this.book = name;
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

    public String getOwnerName(){return this.owner;}

    public String getRequesterName(){return this.requester;}

    public String getBookName(){return this.book;}

}
