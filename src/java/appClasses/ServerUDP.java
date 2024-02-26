/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appClasses;

/**
 *
 * @author Habiba
 */
import java.net.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

class ServerUDP {

    public static void main(String[] args) throws Exception {
        DatagramSocket datagramSocket = new DatagramSocket(9876);

        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/ClientServer";
        String username = "postgres";
        String password = "12345";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            System.out.println("Connected to the database.");

            byte[] dataToReceive = new byte[100];
            byte[] name = new byte[20];

            System.out.println("Waiting For DatagramPacket...");
            DatagramPacket receivedname = new DatagramPacket(name, name.length);
            datagramSocket.receive(receivedname);
            String nameToPrint = new String(receivedname.getData(), 0, receivedname.getLength());
            System.out.println("Welcome " + nameToPrint);


            DatagramPacket receivedPacket = new DatagramPacket(dataToReceive, dataToReceive.length);

            datagramSocket.receive(receivedPacket);

            String sentence = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

            datagramSocket.receive(receivedPacket);
            String path = new String(receivedPacket.getData(), 0, receivedPacket.getLength());


            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO chat_messages (name, message, filepath, timestamp ) VALUES (?, ?,?,CURRENT_TIMESTAMP) RETURNING timestamp")) {
                preparedStatement.setString(1, nameToPrint);
                preparedStatement.setString(2, sentence);
                preparedStatement.setString(3, path);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String dbTimestamp = resultSet.getString("timestamp");
                        System.out.println(dbTimestamp + " " + nameToPrint + " : " + sentence + ".");
                    }
                }
            }

            InetAddress addressIP = receivedPacket.getAddress();
            int port = receivedPacket.getPort();

            datagramSocket.close();
        }
    }
}
