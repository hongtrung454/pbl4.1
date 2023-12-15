/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author DELL
 */
public class machine {
    private String device_fingerprint;
    private String machine_name;
    private boolean is_active;
    private String folder_path;

    public machine(String device_fingerprint, String machine_name, boolean is_active, String folder_path, RequestType requestType) {
        this.device_fingerprint = device_fingerprint;
        this.machine_name = machine_name;
        this.is_active = is_active;
        this.folder_path = folder_path;
        this.requestType = requestType;
    }

    public String getFolder_path() {
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }
    private RequestType requestType;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
    public machine() {
    }

    public machine(String device_fingerprint, String machine_name, boolean is_active) {
        this.device_fingerprint = device_fingerprint;
        this.machine_name = machine_name;
        this.is_active = is_active;
    }

    public String getDevice_fingerprint() {
        return device_fingerprint;
    }

    public void setDevice_fingerprint(String device_fingerprint) {
        this.device_fingerprint = device_fingerprint;
    }

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
    
}
