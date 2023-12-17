package client;

import ui.ClientUi;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    private static Socket socket;
    private static String clientId;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //서버가 열려있는지 확인한 후 프로그램 실행
            if (connect()) {
                System.out.println("서버가 열려있음");
                ClientUi clientUi = new ClientUi();
                //로그인 다이얼로그를 통해 id 입력받음
                clientId = clientUi.showLoginDialog();
                clientUi.setVisible(true);
                System.out.println("사용자 아이디 :"+clientId);
                //서버가 열려있고 id가 존재한다면, 서버에 파일을 전송함
                if (clientId != null) sendClientIdToServer();
            }
            //서버가 열려있지 않다면, 경고창을 띄움
            else showServerNotRunningDialog();


        });
    }
    public static void send(File selectedFile, String password) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            // 파일을 전송합니다.
            byte[] fileData = readBytesFromFile(selectedFile);

            TransferData transferData = new TransferData(clientId, password, selectedFile.getName(), fileData);

            outputStream.writeObject(transferData);
            outputStream.flush();

            // 파일 전송을 받았다면 ack를 수신받습니다.
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String response = (String) inputStream.readObject();
            System.out.println(response);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readBytesFromFile(File file) {
        //버퍼를 통해 파일을 전송합니다.
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean connect() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("서버 접속 확인 : true");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("서버 접속 확인 : false");
            return false;
        }
    }

    private static void sendClientIdToServer()  {
        //서버에 사용자 id를 전송합니다.
        try {
           ObjectOutputStream objectOutputStream =new ObjectOutputStream(socket.getOutputStream());
           objectOutputStream.writeObject(clientId);
           objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void showServerNotRunningDialog() {
        System.out.println("서버가 실행 중이지 않습니다.");
        JOptionPane.showMessageDialog(null, "서버가 실행 중이지 않습니다.", "서버 확인", JOptionPane.ERROR_MESSAGE);

    }
}
