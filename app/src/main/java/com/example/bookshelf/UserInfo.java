package com.example.bookshelf;

import java.util.ArrayList;
import java.util.HashMap;

public class UserInfo {
    private String email;
    private String fullname;
    private ArrayList<String> notifications;
    private ArrayList<String> ownedBooks;
    private String phone;
    private String picture;
    private String username;

    public UserInfo(String email, String fullname, String phone, String username){
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.username = username;
        this.notifications = new ArrayList<String>();
        this.ownedBooks = new ArrayList<String>();
        this.picture = "";
    }

    public UserInfo(String email, String fullname, String phone, String username, ArrayList<String> notifications, ArrayList<String> ownedBooks){
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.username = username;
        this.notifications = notifications;
        this.ownedBooks = ownedBooks;
        this.picture = "";
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<String> getOwnedBooks() {
        return ownedBooks;
    }

    public void setOwnedBooks(ArrayList<String> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashMap<String, Object> getUserMap(){
        HashMap<String, Object> userInfoMap = new HashMap<String, Object>();
        userInfoMap.put("email", this.email);
        userInfoMap.put("fullname", this.fullname);
        userInfoMap.put("notifications", this.notifications);
        userInfoMap.put("ownedBooks", this.ownedBooks);
        userInfoMap.put("phone", this.phone);
        userInfoMap.put("picture", this.picture);
        userInfoMap.put("username", this.username);
        return userInfoMap;

    }
}
