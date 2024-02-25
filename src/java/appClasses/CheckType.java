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
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Habiba
 */
public class CheckType extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String name = request.getParameter("name");
        String connectionType = request.getParameter("connectionType");
        String message = request.getParameter("message");
        boolean messageSent = false;

        PrintWriter pen = response.getWriter();

        if (connectionType.equals("udp")) {
            messageSent = runUDP(name, message);

//            runUDP(name, message);
        } else if (connectionType.equals("tcp")) {
            messageSent = runTCP(name, message);

//            runTCP(name, message);
        }

        pen.println("<!DOCTYPE html>");
        pen.println("<html>");
        pen.println("<head>");
        pen.println("<meta charset=\"UTF-8\">");
        pen.println("<title>Message Sent</title>");
        pen.println("</head>");
        pen.println("<body>");

        if (messageSent) {
            pen.println("<h1>Message Sent Successfully!</h1>");
            pen.println("<p><strong>Sender Name:</strong> " + name + "</p>");
            pen.println("<p><strong>Timestamp:</strong> " + new Timestamp(new Date().getTime()).toString() + "</p>");
            pen.println("<p><strong>Message:</strong> " + message + "</p>");
        } else {
            pen.println("<h1>Failed to Send Message</h1>");
            pen.println("<p>An error occurred while sending the message.</p>");
        }

    }

    private boolean runUDP(String name, String message) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("127.0.0.1");

            byte[] nameBytes = name.getBytes();
            DatagramPacket namePacket = new DatagramPacket(nameBytes, nameBytes.length, address, 9876);
            socket.send(namePacket);

            byte[] messageBytes = message.getBytes();
            DatagramPacket messagePacket = new DatagramPacket(messageBytes, messageBytes.length, address, 9876);
            socket.send(messagePacket);

//            byte[] buffer = new byte[100];
//            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
//            socket.receive(responsePacket);
//            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
//            System.out.println("Received from UDP server: " + response);
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean runTCP(String name, String message) {
        try {
            Socket socket = new Socket("127.0.0.1", 5001);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.writeUTF(name);

            out.writeUTF(message);
//            String response = in.readLine();
//            System.out.println("Received from TCP server: " + response);

            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
