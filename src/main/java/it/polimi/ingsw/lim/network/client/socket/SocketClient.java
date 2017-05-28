package it.polimi.ingsw.lim.network.client.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by nico.
 */
public class SocketClient {
    private boolean isClientRunning = true;

    public SocketClient() {

    }

    public void connectSocket(String address, int port) {
        Scanner command = new Scanner(System.in);
        String commandToSend;

        try {
            // socket, input and output stream
            Socket socketClient = new Socket(address, port);
            ObjectOutputStream objFromClient = new ObjectOutputStream(socketClient.getOutputStream());
            ObjectInputStream objToClient = new ObjectInputStream(socketClient.getInputStream());

            while(isClientRunning) {
                System.out.println("Write a command: ");
                commandToSend = command.nextLine();
                objFromClient.writeObject(commandToSend);
                System.out.println("[Server]: "+ objToClient.readObject());
            }
        } catch(IOException ioe) {
           System.out.println("Could not connect to "+address+" on port "+port);
        } catch(ClassNotFoundException cnfe) {
            System.out.println("ClassNotFoundException raised");
        }
    }
}
