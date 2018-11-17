package com.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(3333);
            System.out.println("접속 대기...");
            socket = server.accept();

            InputStream input = socket.getInputStream();
            DataInputStream dis = new DataInputStream(input);

            OutputStream output = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(output);

            dos.writeInt(100);
            dos.writeDouble(123.123);
            dos.writeUTF("서버가 보낸 문자열");
            System.out.println("데이터 전송 완료...");

            int data1 = dis.readInt();
            double data2 = dis.readDouble();
            String data3 = dis.readUTF();
            System.out.println("데이터 수신 완료...");

            System.out.printf("data1 = %d \n", data1);
            System.out.printf("data2 = %f \n", data2);
            System.out.printf("data3 = %s \n", data3);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(socket != null) {
                try {
                    socket.close();
                }
                catch (IOException e) { }
            }
            if(server != null) {
                try {
                    server.close();
                }
                catch (IOException e) { }
            }
        }
    }
}
