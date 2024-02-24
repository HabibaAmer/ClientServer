/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package appClasses;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.io.*;

/**
 *
 * @author Habiba
 */
public class CheckType extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String connectionType = request.getParameter("connectionType");
        String message = request.getParameter("message");

        PrintWriter pen = response.getWriter();

        if (connectionType.equals("udp")) {
            // Execute UDP client-server communication
            executeUDP(name, message);
        } else if (connectionType.equals("tcp")) {
            // Execute TCP client-server communication
            executeTCP(name, message);
        }

    }

    private void executeUDP(String name, String message) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("127.0.0.1");

            // Send the user's name to the UDP server
            byte[] nameBytes = name.getBytes();
            DatagramPacket namePacket = new DatagramPacket(nameBytes, nameBytes.length, address, 9876);
            socket.send(namePacket);

            // Send the message to the UDP server
            byte[] messageBytes = message.getBytes();
            DatagramPacket messagePacket = new DatagramPacket(messageBytes, messageBytes.length, address, 9876);
            socket.send(messagePacket);

            // Receive response from the UDP server
            byte[] buffer = new byte[100];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(responsePacket);
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Received from UDP server: " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeTCP(String name, String message) {
        try {
            Socket socket = new Socket("127.0.0.1", 5001);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the user's name to the TCP server
            out.writeUTF(name);

            // Send the message to the TCP server
            out.writeUTF(message);

            // Receive response from the TCP server
            String response = in.readLine();
            System.out.println("Received from TCP server: " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
