/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package filesync_client;

/**
 *
 * @author DELL
 */
public class FileSync_Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
           String n1=     GetDeviceFingerprint.getFingerPrint();
           if(n1.equals(GetDeviceFingerprint.getFingerPrint())) System.out.println("true");
    }
    
}
