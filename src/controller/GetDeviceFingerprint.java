/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author DELL
 */
public class GetDeviceFingerprint {
    public GetDeviceFingerprint () {
        
    }
    public static String getFingerPrint() {
        String fingerprint = generateDeviceFingerprint();
        System.out.println("Device Fingerprint: " + fingerprint);
        return fingerprint;
    }

    public static String generateDeviceFingerprint() {
        StringBuilder fingerprintBuilder = new StringBuilder();

        // Get system properties
        fingerprintBuilder.append("OS: ").append(System.getProperty("os.name")).append("; ");
        fingerprintBuilder.append("Version: ").append(System.getProperty("os.version")).append("; ");
        fingerprintBuilder.append("Arch: ").append(System.getProperty("os.arch")).append("; ");

        // Get network information
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            fingerprintBuilder.append("Hostname: ").append(localHost.getHostName()).append("; ");
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            byte[] mac = networkInterface.getHardwareAddress();
            fingerprintBuilder.append("MAC Address: ").append(getHexString(mac)).append("; ");
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        // Additional information can be added based on your requirements

        // Calculate a hash of the fingerprint
        return calculateHash(fingerprintBuilder.toString());
    }

    private static String getHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

    private static String calculateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02X", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
