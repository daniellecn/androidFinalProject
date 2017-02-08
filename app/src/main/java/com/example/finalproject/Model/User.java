package com.example.finalproject.Model;

/**
 * Created by Danielle Cohen on 17/01/2017.
 */

public class User {
    private String name;
    private String password;
    private String mail;
    private boolean admin;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.admin = false;
    }

    public User(String name, String password, String mail) {
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.admin = false;
    }

    public User(String name, String password, String mail, boolean admin) {
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
