package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ClientUi extends JFrame {
    private JTextField passwordField;
    private JTextField selectedFileField;

    private File selectedFile;

    public ClientUi() {
        setTitle("File Transfer Client");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        //ui 기본설정
        passwordField = new JTextField(20);

        JButton selectFileButton = new JButton("Select File");
        selectFileButton.addActionListener(e -> selectFile());

        selectedFileField = new JTextField(20);
        selectedFileField.setEditable(false);

        JButton sendButton = new JButton("Send File");
        sendButton.addActionListener(e -> sendFile());

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Receiver ID:"));
        panel.add(passwordField);
        panel.add(selectFileButton);
        panel.add(selectedFileField);
        panel.add(sendButton);
        add(panel);
    }

    private void selectFile() {
        //버튼을 누르면 파일을 고를 수 있도록 함.파일이름을 보여줄수 있도록 함
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            selectedFileField.setText(selectedFile.getName());
        }
    }

    private void sendFile() {
        if (selectedFile != null) {
            String password = passwordField.getText();

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
            } else {
                // 비밀번호화 함께 패스워드 전송
                client.Client.send(selectedFile, password);
            }
        } else {
            JOptionPane.showMessageDialog(this, "파일을 먼저 골라주세요");
        }
    }

    public String showLoginDialog() {
        JTextField clientIdField = new JTextField();
        Object[] message = {
                "사용자의 ID를 입력하세요 :", clientIdField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Return the entered client ID
            return clientIdField.getText();
        } else {
            // Exit the program if the user cancels
            System.exit(0);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUi().setVisible(true));
    }
}
