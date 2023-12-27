/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import com.google.gson.Gson;
import controller.ConnectionManager;
import controller.account_controller;
import controller.machine_controller;
import java.awt.AWTEventMulticaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import model.RequestType;
import model.account;
import model.machine;

/**
 *
 * @author DELL
 */
public class LoginForm extends javax.swing.JFrame {

    /**
     * Creates new form LoginForm
     */
    public LoginForm() throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
        }
        initComponents();
//        Socket socket1 = new Socket("localhost", 8000);
        this.setLocationRelativeTo(null);
        ConnectionManager.getInstance().getConnection();
        ConnectionManager.getInstance().startListening();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Sign up");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private account user1;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            
             user1 = new account( jTextField1.getText().trim(), jPasswordField1.getText(), RequestType.LOGIN);
//            System.out.println(user1.getUsername());
//            System.out.println(user1.getPassword());
            user1.setIs_active(true);
            Gson gson = new Gson();
            String jsonData = gson.toJson(user1);
            System.out.println(jsonData + "666666");
//            ConnectionManager.getInstance().outputStream.write(jsonData.getBytes());
            account_controller user_controller = new account_controller(user1);
            ConnectionManager.getInstance().startRequest(user_controller);
            final Timer timer = new Timer(1500, null);
            timer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(ConnectionManager.getInstance().loginStatus)
                    {
                        getFolderPath( user1);
                        timer.stop();
                    }
                    else {        
                        JOptionPane.showMessageDialog(null, "Sai tên tài khoản hoặc mật khẩu", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        timer.stop();

                    }
                }
            });
                    
            timer.start();
            

        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        RegisterForm rForm = new RegisterForm();
        rForm.show();
    }//GEN-LAST:event_jButton2ActionPerformed
//    private void checkFingerprint(machine_controller machine1_controller, account user1) {
//        JOptionPane.showMessageDialog(null, "Đăng nhập thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//        this.hide();
//        ConnectionManager.getInstance().startRequest(machine1_controller);
//        final Timer timer = new Timer(2000, null);
//        timer.addActionListener(new ActionListener() {
//           public void actionPerformed(ActionEvent e) {
//                if (ConnectionManager.getInstance().deviceFingerprint){
//                    // mở luôn form chính, nhớ truyền objects account với machine vào đây
//                    System.out.println("may da duoc dang ky");
//                    timer.stop();
//                    // đây là hàm để mà kiểu, xử lý nếu mà máy ni đã từng kết nối rồi, với có đường dẫn sẵn rồi, thì t chỉ 
//                    // đọc thay đổi trên server xong load về thôi
//                    // chơ k cần chọn đường dẫn folder nữa//
//                    
//                    // đều t chưa làm chi cả =))
//                    
//                }
//                else {
//                    getFolderPath(machine1_controller, user1);
//                    timer.stop();
//                }
//            } 
//        });
//        
//            
//        timer.start();
//        
//    }
    private void getFolderPath( account user1) {
        boolean selectedEmptyDirectory = false;
        JOptionPane.showMessageDialog(null, "Đăng nhập thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        this.hide();
        JOptionPane.showMessageDialog(null, "Vui lòng chọn đường dẫn đến thư mục muốn đồng bộ", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        while (!selectedEmptyDirectory) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = fileChooser.getSelectedFile();

                // Kiểm tra nếu thư mục là rỗng
                if (isEmptyDirectory(selectedDirectory)) {
                    String path = selectedDirectory.getAbsolutePath();
                    System.out.println(path);
                    ConnectionManager.getInstance().path = path;
//                    String userInput = JOptionPane.showInputDialog(null, "Nhập vào tên máy:", "Nhập dữ liệu", JOptionPane.QUESTION_MESSAGE);
//
//                    // Kiểm tra xem người dùng đã nhập hay chưa
//                    if (userInput != null && !userInput.isEmpty()) {
//                        // Người dùng đã nhập một chuỗi
//                        System.out.println("Bạn đã nhập: " + userInput);
//                    } else {
//                        // Người dùng có thể đã hủy hoặc không nhập gì cả
//                        System.out.println("Bạn đã hủy hoặc không nhập gì cả.");
//                    }
//                    machine _machine = machine1_controller.getMachine1();
//                    _machine.setFolder_path(path);
//                    //truyen path cho chuong trinh client
//                    ConnectionManager.getInstance().path = path;
//                    _machine.setMachine_name(userInput);
//                    _machine.setRequestType(RequestType.INSERT_DEVICE);
//                    machine1_controller.setMachine1(_machine);
//                    ConnectionManager.getInstance().startRequest(machine1_controller);
                    ClientMainForm  f1 = new ClientMainForm(path, user1);
                    f1.show();
//                    System.out.println(_machine.toString());
                    
                    // Đánh dấu là đã chọn thư mục rỗng và thoát khỏi vòng lặp
                    selectedEmptyDirectory = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một thư mục rỗng.", "Thông báo", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                 user1.setIs_active(false);
                account_controller logOutRequest = new account_controller(user1);
                logOutRequest.setRequestType(RequestType.LOG_OUT);
                ConnectionManager.getInstance().startRequest(logOutRequest);
                
                // Người dùng đã hủy chọn thư mục, có thể xử lý tùy ý
                break;
            }
        }
    }
    private boolean isEmptyDirectory(File directory) {
        File[] files = directory.listFiles();
        return files != null && files.length == 0;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new LoginForm().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
