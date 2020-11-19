package com.example.bookshelf;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * THe UserInfo class
 * Stores Information as saved in firebase.
 */
public class UserInfo {
    private String email;
    private String fullname;
    private ArrayList<String> notifications;
    private ArrayList<String> ownedBooks;
    private String phone;
    private String picture;
    private String username;

    /**
     * Instantiates a new User info.
     * Important for firebase to deserialize object with a no argument constructor.
     */
    public UserInfo(){}

    /**
     * Instantiates a new User info.
     *
     * @param email    the email
     * @param fullname the fullname
     * @param phone    the phone
     * @param username the username
     */
    public UserInfo(String email, String fullname, String phone, String username){
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.username = username;
        this.notifications = new ArrayList<String>();
        this.ownedBooks = new ArrayList<String>();
        this.picture = "";
    }

    /**
     * Instantiates a new User info.
     *
     * @param email         the email
     * @param fullname      the fullname
     * @param phone         the phone
     * @param username      the username
     * @param notifications the notifications
     * @param ownedBooks    the owned books
     */
    public UserInfo(String email, String fullname, String phone, String username, ArrayList<String> notifications, ArrayList<String> ownedBooks){
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.username = username;
        this.notifications = notifications;
        this.ownedBooks = ownedBooks;
        this.picture = "";
    }


    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets fullname.
     *
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Sets fullname.
     *
     * @param fullname the fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Gets notifications.
     *
     * @return the notifications
     */
    public ArrayList<String> getNotifications() {
        return notifications;
    }

    /**
     * Sets notifications.
     *
     * @param notifications the notifications
     */
    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    /**
     * Gets owned books.
     *
     * @return the owned books
     */
    public ArrayList<String> getOwnedBooks() {
        return ownedBooks;
    }

    /**
     * Sets owned books.
     *
     * @param ownedBooks the owned books
     */
    public void setOwnedBooks(ArrayList<String> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets picture.
     *
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets picture.
     *
     * @param picture the picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get user map hash map.
     *
     * @return the hash map of user data to send to the database.
     */
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
