/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author DELL
 */
public class account {
    private int user_id;
    private String username;
    private String password;
    private RequestType requestType;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public account(String username, String password, RequestType requestType) {
        this.username = username;
        this.password = password;
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "account [userid = " + user_id + ", username =" + username + ", password = " + password + ", isactive = " + is_active; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    public account() {
    }

    public account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public account(int user_id, String username, String password, boolean is_active) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.is_active = is_active;
    }
    private boolean is_active;
}
