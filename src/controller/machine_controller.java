/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import model.Sendable;
import model.machine;

/**
 *
 * @author DELL
 */public class machine_controller implements Sendable{
    private machine machine1;

    public machine getMachine1() {
        return machine1;
    }

    public void setMachine1(machine machine1) {
        this.machine1 = machine1;
    }

    public machine_controller(machine machine1) {
        this.machine1 = machine1;
    }

    public machine_controller() {
    }

    @Override
    public String prepareDataToSend() {
        String jsonData = "";
        try {
            Gson gson = new Gson();
             jsonData = gson.toJson(machine1);
             
//            System.out.println(jsonData);
            return jsonData;

        } catch (Exception e) {
        }
        return jsonData;
    }
}
