/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filesync_client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class ConnectionManager {
    private static ConnectionManager instance;
    private static final String URL = "localhost";
    private static final int PORT = 8000;
    private Socket socket = new Socket();
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
        if ( socket== null) {
            try {
                // Initialize connection (lazy initialization)
                socket = new Socket(URL, PORT);
            } catch (IOException ex) {
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return socket;
    }

    
}