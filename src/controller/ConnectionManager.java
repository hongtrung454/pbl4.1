/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import static controller.calculateMD5.calculateMD5OfFile;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
    public boolean RegisterStatus = false;
//    public boolean deviceFingerprint = false;
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

                    } else if(serverResponse.startsWith("Register")) {
                        RegisterStatus = (serverResponse.equals("Register successful") ? true : false);
                    } else if (serverResponse.contains("GET_ALL_FILES_RESPONSE")) {
                        System.out.println("server response: " + serverResponse);
                        handleGetAllFilesResponse(serverResponse);
                    } else if (serverResponse.contains("MARKED_FILES_INFO")) {
                        try {
                            handleMarkedFilesInfo(new Gson().fromJson(serverResponse, JsonObject.class), reader, new PrintWriter(socket.getOutputStream(), true));

                        } catch (Exception e) {
                        }
                    } else if (serverResponse.contains("SEND_UPDATE_FROM_SERVER")) {
                        handleReceiveUpdateResponse(serverResponse, new PrintWriter(socket.getOutputStream(), true));
                        System.out.println(serverResponse);
                    } else if (serverResponse.contains("SEND_MARKED_FILES_FROM_SERVER")) {
                        handleReceiveMarkedFilesFromServer(serverResponse);
                    } else if (serverResponse.contains("REQUEST_FILE_INFO")) {
                        startRequest(1);
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
            int dataLength = dataToSend.getBytes().length;
            outputStream.writeInt(dataLength);
            outputStream.write(dataToSend.getBytes());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startRequest(int i) {
        Thread sendThread = new Thread(() -> {
            startSending(i);
        });
        sendThread.start();
    }
    private void startSending(int i) {
       
        try {
            
            //gửi file
            File directory = new File(path);
            File[] files = directory.listFiles();

            //tao doi tuong JSON de bieu dien list cac tap tin
            JsonObject jsonFiles = new JsonObject();
            if (i == 1) {
                jsonFiles.addProperty("requestType", "SEND_FILES_INFO_FROM_CLIENT");

            } else {
                jsonFiles.addProperty("requestType", "SEND_FILES_FROM_CLIENT");
            }
            jsonFiles.addProperty("fileCount", files.length);

            // tao mot array luu thong tin cho moi file
            JsonArray filesArray = new JsonArray();
            for (File file: files) {
                JsonObject fileInfo = new JsonObject();
                fileInfo.addProperty("fileName", file.getName());
                System.out.println(file.getName() + " test gui file tu client");
                fileInfo.addProperty("fileSize", file.length());
                fileInfo.addProperty("fileMD5", calculateMD5OfFile(file.getAbsolutePath()));
                filesArray.add(fileInfo);
                
            }
            jsonFiles.add("files", filesArray);
            
            String jsonData = new Gson().toJson(jsonFiles);
//            System.out.println("json Data duoc gui di laaaaaaaaaaaaaa:"+ jsonData );
            byte[] jsonDataBytes = jsonData.getBytes(StandardCharsets.UTF_8);
            String jsonData1 = new String(jsonDataBytes, StandardCharsets.UTF_8);
            System.out.println("jsondata duoc gui di laaaaaaaaaaaaaa:" + jsonData1);
            int dataLength = jsonDataBytes.length;
            outputStream.writeInt(dataLength);
            outputStream.write(jsonDataBytes);
            outputStream.flush();
//            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
//            writer.println(jsonData); // đây là gửi thông tin file
//            
            // gui moi file cho client 
//            for(File file: files) {
//                sendFileToClient(file);
//            }
        } catch (Exception e) {
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

//    private boolean handleReceivedFingerprintData(String data) {
//        if (data.equals("Fingerprint right")) {
//            return true;
//        }
//        return false;
//    }

    private void handleGetAllFilesResponse(String data) {
        try {
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
            // Read file data into a byte array
            byte[] fileData = new byte[(int) fileSize];
            int bytesRead;
            int offset = 0;

            while (offset < fileSize && (bytesRead = inputStream.read(fileData, offset, (int) (fileSize - offset))) != -1) {
                offset += bytesRead;
            }

            // Save the file
            saveFile(fileData, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile(byte[] fileData, String fileName) throws IOException {
        FileOutputStream fileOutputStream = null;

        try {
            // Construct the file path
            String filePath = path + "\\" + fileName;

            // Create a FileOutputStream to save the file
            fileOutputStream = new FileOutputStream(filePath);

            // Write the file data to the FileOutputStream
            fileOutputStream.write(fileData);

            System.out.println("File saved to: " + filePath);

        } finally {
            // Close the FileOutputStream
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

//    private void receiveFile(InputStream inputStream, String fileName, long fileSize) {
//        try {
//            
//            // doc du lieu cua file vao byte array
//            
//            byte[] fileData = new byte[(int) fileSize];
//            int bytesRead = inputStream.read(fileData, 0, (int) fileSize);
//            saveFile(fileData, fileName);
//        } catch (Exception e) {
//        }
//    }
//    private void saveFile(byte[] fileData, String fileName) throws IOException  {
//        FileOutputStream fileOutputStream = null;
//        try {
//            System.out.println(path);
//            String filePath = path + "\\" + fileName;
//            System.out.println(path);
//            
//            // mo mot file output stream de luu file
//            fileOutputStream = new FileOutputStream(filePath);
//            
//            //viet file data vao fileoutputstream
//            fileOutputStream.write(fileData);
//            System.out.println("File saved to: " + filePath);
//
//        } finally {
//            if (fileOutputStream != null) {
//                fileOutputStream.close();
//            }
//        }
//    }

    private void handleMarkedFilesInfo(JsonObject receivedData, BufferedReader reader, PrintWriter writer) {
        try {
            JsonArray markedFilesArray = receivedData.getAsJsonArray("markedFiles");

            // Tạo danh sách các file đã được ghi chú lại từ server
            JsonObject jsonFiles = new JsonObject();
            Map<String, String> markedFiles = new HashMap<>();
            JsonArray fileDetailsArray = new JsonArray(); // Tạo một mảng mới cho thông tin về các file

            for (int i = 0; i < markedFilesArray.size(); i++) {
                JsonObject fileInfo = markedFilesArray.get(i).getAsJsonObject();
                String fileName = fileInfo.get("fileName").getAsString();
                String fileMD5 = fileInfo.get("fileMD5").getAsString();
                String filePath = Paths.get(path, fileName).toString();

                File file = new File(filePath);
                JsonObject fileDetails = new JsonObject();
                fileDetails.addProperty("fileName", fileName);
                fileDetails.addProperty("fileSize", file.length());
                fileDetailsArray.add(fileDetails);
                markedFiles.put(fileName, fileMD5);
                System.out.println("testttttttttttttttttttttttttttt000000000");
            }

            System.out.println("testttttttttttttttttttttttttttt");
            jsonFiles.addProperty("requestType", "SEND_MARKED_FILES");
            jsonFiles.addProperty("fileCount", markedFilesArray.size());
            jsonFiles.add("files", fileDetailsArray);

            String jsonData = new Gson().toJson(jsonFiles);
            byte[] jsonDataBytes = jsonData.getBytes(StandardCharsets.UTF_8);
//            String jsonData1 = new String(jsonDataBytes, StandardCharsets.UTF_8);
//System.out.println("jsondata duoc gui di laaaaaaaaaaaaaa:" +jsonData1);
            int dataLength = jsonDataBytes.length;
            outputStream.writeInt(dataLength);
            outputStream.write(jsonDataBytes);
            outputStream.flush();
//            writer.println(jsonData);
//            writer.flush();
            
            sendMarkedFilesToServer(path, markedFiles);
            // Gửi các file đã được ghi chú lại đến server để nhận từ server
            // sendFilesToServer(reader, writer, markedFiles);
        } catch (Exception e) {
            // Xử lý các exception phù hợp
            e.printStackTrace();
        }
    }

    
    private void sendMarkedFilesToServer(String path, Map<String, String> markedFiles) {
        for (Map.Entry<String, String> entry : markedFiles.entrySet()) {
            String fileName = entry.getKey();
            String fileMD5 = entry.getValue();
            
            // Construct the full path of the file
            String filePath = Paths.get(path, fileName).toString();

            // Check if the file exists
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("Sending file: " + fileName);
                sendFileToServer(file);
            } else {
                System.out.println("File not found: " + fileName);
            }
        }
    }
//    private void sendFileToServer(File file, String fileName) {
//        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            byte[] fileData = new byte[(int) file.length()]; //đlà gửi file :f  
//            fileInputStream.read(fileData);
//            fileInputStream.close();
//            
//            this.socket.getOutputStream().write(fileData);
//            this.socket.getOutputStream().flush();
//        } catch (Exception e) {
//        }
//    }
    private void sendFileToServer(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192]; //đlà gửi file :f  
            int bytesRead;
            while((bytesRead = fileInputStream.read(buffer)) != -1) {
                this.socket.getOutputStream().write(buffer, 0, bytesRead);
                this.socket.getOutputStream().flush();
            }
            fileInputStream.close();
//            fileInputStream.read(fileData);
//            fileInputStream.close();
//            this.output.write(fileData);
//            this.output.flush();
        } catch (Exception e) {
        }
    }

    private String prepareDataToSend(Sendable sendable) {
        return sendable.prepareDataToSend();
        
    }
    private void handleReceiveUpdateResponse(String data, PrintWriter writer) {
        try {
//                                        
            JsonObject jsonFiles = new Gson().fromJson(data, JsonObject.class);
            int fileCount = jsonFiles.get("fileCount").getAsInt();
            JsonArray filesArray = jsonFiles.getAsJsonArray("files");
            Map<String, String> clientFiles = getClientFiles();
            Map<String, String> markedFiles = new HashMap<>();

            for (int i = 0; i < fileCount; i++) {
                JsonObject fileInfo = filesArray.get(i).getAsJsonObject();
                String fileName = fileInfo.get("fileName").getAsString();
                System.out.println(fileName);
                long fileSize = fileInfo.get("fileSize").getAsLong();
                String fileMD5 = fileInfo.get("fileMD5").getAsString();
                // Kiểm tra file trên server
                if (clientFiles.containsKey(fileName)) {
                    String clientFileMD5 = clientFiles.get(fileName);

                    if (!clientFileMD5.equals(fileMD5)) {
                        // Nếu MD5 khác nhau, ghi chú lại thông tin file
                        markedFiles.put(fileName, fileMD5);
                    }
                    // Nếu MD5 giống nhau, không cần làm gì cả
                } else {
                    // Nếu file không tồn tại, ghi chú lại thông tin file
                    markedFiles.put(fileName, fileMD5);
                }
            }
            System.out.println("Da clean up chua ?");
            cleanupClientFiles(filesArray);
            // Gửi thông tin về các file đã được ghi chú lại cho client
            sendMarkedFilesInfoToServer(markedFiles, writer);

            // Nhận các file từ client và thực hiện ghi đè hoặc tải về
//            receiveFilesFromClient(reader, markedFiles);
        } catch (Exception e) {
        }
    }
    private  Map<String, String> getClientFiles() {
        Map<String, String> clientFiles = new HashMap<>();
        File clientDirectory = new File(path);
        File[] files = clientDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                String md5 = calculateMD5.calculateMD5OfFile(file.getAbsolutePath());
                clientFiles.put(file.getName(), md5);
            }
        }

        return clientFiles;
    }
        private void cleanupClientFiles(JsonArray serverFilesArray) {
        File clientDirectory = new File(path);
        File[] clientFiles = clientDirectory.listFiles();

        if (clientFiles != null) {
            for (File clientFile : clientFiles) {
                String clientFileName = clientFile.getName();
                boolean found = false;

                // Kiểm tra xem file trên server có trong danh sách gửi từ client không
                for (JsonElement serverFileElement : serverFilesArray) {
                    String serverFileName = serverFileElement.getAsJsonObject().get("fileName").getAsString();
                    if (clientFileName.equals(serverFileName)) {
                        found = true;
                        break;
                    }
                }

                // Nếu không tìm thấy trong danh sách gửi từ client, xóa file trên server
                if (!found) {
                    clientFile.delete();
                }
            }
        }
    }
        private void sendMarkedFilesInfoToServer(Map<String, String> markedFiles, PrintWriter writer) {
        JsonObject markedFilesInfo = new JsonObject();
        markedFilesInfo.addProperty("responseType", "MARKED_FILES_INFO_FROM_CLIENT");

        JsonArray markedFilesArray = new JsonArray();
        for (Map.Entry<String, String> entry : markedFiles.entrySet()) {
            JsonObject fileInfo = new JsonObject();
            fileInfo.addProperty("fileName", entry.getKey());
            fileInfo.addProperty("fileMD5", entry.getValue());
            markedFilesArray.add(fileInfo);
        }

        markedFilesInfo.add("markedFiles", markedFilesArray);
        String jsonData = new Gson().toJson(markedFilesInfo);
        byte[] jsonDataBytes = jsonData.getBytes(StandardCharsets.UTF_8);
//            String jsonData1 = new String(jsonDataBytes, StandardCharsets.UTF_8);
//            System.out.println("jsondata duoc gui di laaaaaaaaaaaaaa:" + jsonData1);
        int dataLength = jsonDataBytes.length;
        try {
            outputStream.writeInt(dataLength);
            outputStream.write(jsonDataBytes);
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

//            writer.println(new Gson().toJson(markedFilesInfo));
    }
        private void handleReceiveMarkedFilesFromServer(String data) {
        try {
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
                                System.out.println(String.valueOf(fileSize));


                receiveFile(socket.getInputStream(), fileName, fileSize);
            }
        } catch (Exception e) {
        }
    }
       

}
