/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import model.Sendable;
import model.account;

/**
 *
 * @author DELL
 */
public class account_controller  implements Sendable{
    private account user1;

    public account_controller() {
    }

    public account_controller(account user1) {
        this.user1 = user1;
    }

    public account getUser1() {
        return user1;
    }

    public void setUser1(account user1) {
        this.user1 = user1;
    }

    @Override
    public String prepareDataToSend() {
        String jsonData = "";
        try {
            Gson gson = new Gson();
             jsonData = gson.toJson(user1);
//            System.out.println(jsonData);
            return jsonData;

        } catch (Exception e) {
        }
        return jsonData;
    }
    
}
