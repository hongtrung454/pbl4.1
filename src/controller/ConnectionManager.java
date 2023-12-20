/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RequestType;
import model.Sendable;

/**
 *
 * @author DELL
 */
public class ConnectionManager {

    private static ConnectionManager instance;
    private static final String URL = "localhost";
    private static final int PORT = 8000;
    private Socket socket = new Socket();
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    public boolean loginStatus = false;
    public boolean deviceFingerprint = false;
    public String path = "";
    private ConnectionManager() {
        // private constructor to prevent instantiation
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public synchronized Socket getConnection() {

        try {
            // Initialize connection (lazy initialization)
            socket = new Socket(URL, PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return socket;
    }

    public void startListening() {
        Thread receiveThread = new Thread(() -> {
            startReceiving();
        });
        receiveThread.start();
    }

    private void startReceiving() {
        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String serverResponse = reader.readLine();

                if (serverResponse != null) {
                    if (serverResponse.startsWith("Login")) {
                        loginStatus = handleReceivedLoginData(serverResponse);

                    } else if (serverResponse.startsWith("Fingerprint")) {
                        deviceFingerprint = handleReceivedFingerprintData(serverResponse);
                    } else if (serverResponse.contains("GET_ALL_FILES_RESPONSE")) {
                        System.out.println("server response: " + serverResponse);
                        handleGetAllFilesResponse(serverResponse);
                        
                    }

                }
                //System.out.println("Server response: " + serverResponse);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void startRequest(Sendable sendable) {
        Thread sendThread = new Thread(() -> {
            startSending(sendable);
        });
        sendThread.start();
    }

    private void startSending(Sendable sendable) {
        try {

            String dataToSend = prepareDataToSend(sendable);
            System.out.println(dataToSend + "11111");

            outputStream.write(dataToSend.getBytes());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void startListening(Sendable sendable) {
//        Thread receiveThread = new Thread(() -> {
//            while (true) {
//                try {
//                    // nhan va xu ly du lieu
////                    String receivedData = inputStream.readUTF();
////                    
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    String serverResponse = reader.readLine();
//                    
//                    if(serverResponse != null) {
//                        if(serverResponse.startsWith("Login")) {
//                            if(loginStatus = handleReceivedLoginData(serverResponse)) break;
//                            
//                        }
//                        else if (serverResponse.startsWith("Fingerprint")) {
//                            if (deviceFingerprint = handleReceivedFingerprintData(serverResponse)) break;
//                            else break;
//                        }
//                    }
//                    System.out.println("Server response: " + serverResponse);
//                    System.out.println(loginStatus);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }
//    });
//        Thread sendThread = new Thread(() -> {
//            
//                try {
//                    // Gửi dữ liệu ở đây
//                    String dataToSend = prepareDataToSend(sendable);
//                    System.out.println(dataToSend + "11111");
//                    
//                    outputStream.write(dataToSend.getBytes());
//                    outputStream.flush();
//                    Thread.sleep(1000);  // Đợi 1 giây trước khi gửi tiếp
//                } catch (IOException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//            
//        });
//        receiveThread.start();
//        sendThread.start();
//    }
    private boolean handleReceivedLoginData(String data) {
        if (data.equals("Login successful")) {
            return true;
        }
        return false;
    }

    private boolean handleReceivedFingerprintData(String data) {
        if (data.equals("Fingerprint right")) {
            return true;
        }
        return false;
    }

    private void handleGetAllFilesResponse(String data) {
        try {
//                                        System.out.println("aloaloaloaloa111111111111");
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                                        System.out.println("aloaloaloaloa222222222222222");
//
//            String jsonMetaData = reader.readLine();
            JsonObject jsonFiles = new Gson().fromJson(data, JsonObject.class);
                                        System.out.println("aloaloaloaloa1111");

            int fileCount = jsonFiles.get("fileCount").getAsInt();
            JsonArray filesArray = jsonFiles.getAsJsonArray("files");
            System.out.println(String.valueOf(fileCount));
            // nhan moi file tu server
            for (int i = 0; i < fileCount; i++) {
                JsonObject fileInfo = filesArray.get(i).getAsJsonObject();
                String fileName = fileInfo.get("fileName").getAsString();
                
                System.out.println(fileName);

                long fileSize = fileInfo.get("fileSize").getAsLong();

                receiveFile(socket.getInputStream(), fileName, fileSize);
            }
        } catch (Exception e) {
        }
    }
    private void receiveFile(InputStream inputStream, String fileName, long fileSize) {
        try {
            
            // doc du lieu cua file vao byte array
            byte[] fileData = new byte[(int) fileSize];
            int bytesRead = inputStream.read(fileData, 0, (int) fileSize);
            saveFile(fileData, fileName);
        } catch (Exception e) {
        }
    }
    private void saveFile(byte[] fileData, String fileName) throws IOException  {
        FileOutputStream fileOutputStream = null;
        try {
            System.out.println(path);
            String filePath = path + "\\" + fileName;
            System.out.println(path);
            
            // mo mot file output stream de luu file
            fileOutputStream = new FileOutputStream(filePath);
            
            //viet file data vao fileoutputstream
            fileOutputStream.write(fileData);
            System.out.println("File saved to: " + filePath);

        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    private String prepareDataToSend(Sendable sendable) {
        return sendable.prepareDataToSend();
        // Chuẩn bị dữ liệu để gửi
        //return "Hello from client!";
//        switch (requestType) {
//        case LOGIN:
//            // Chuẩn bị dữ liệu cho đăng nhập
//            // ...
//            return "Login data";
//
//        case REGISTER:
//            // Chuẩn bị dữ liệu cho đăng ký
//            // ...
//            return "Registration data";
//
//        // Thêm các trường hợp xử lý khác nếu cần
//
//        default:
//            return "Default data";
//        }
    }

}
