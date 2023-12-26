/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.RequestType;
import model.Sendable;
import model.account;
import model.machine;

/**
 *
 * @author DELL
 */
public class file_request_controller implements Sendable{
    private account user1;
    private machine machine1;

    public file_request_controller(account user1) {
        this.user1 = user1;
//        this.machine1 = machine1;
    }
    public void setRequestType(RequestType t){
        user1.setRequestType(t);
//        machine1.setRequestType(t);
    }
    public account getUser1() {
        return user1;
    }

    public void setUser1(account user1) {
        this.user1 = user1;
    }

    public machine getMachine1() {
        return machine1;
    }

    public void setMachine1(machine machine1) {
        this.machine1 = machine1;
    }
    
    @Override
    public String prepareDataToSend() {
        String jsonData = "";
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();

            jsonObject.add("account", gson.toJsonTree(user1));
//            jsonObject.add("machine", gson.toJsonTree(machine1));

            jsonData = gson.toJson(jsonObject);

            return jsonData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonData;
    }
    
}
