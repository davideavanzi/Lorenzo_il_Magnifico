package it.polimi.ingsw.lim.network.client;

import static it.polimi.ingsw.lim.Log.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Created by nico.
 */
public class SocketClient {
    private Socket socketClient;
    private static String address = "localhost";
    private static int port = 8924;
    private ObjectOutputStream objFromClient;
    private ObjectInputStream objToClient;
    private boolean isClientRunning = true;
    private Scanner command;
    private String commandToSend;

    public SocketClient() {

    }

    public void connectSocket() {
        command = new Scanner(System.in);

        try {
            // Socket, input and output stream
            socketClient = new Socket(address, port);
            objFromClient = new ObjectOutputStream(socketClient.getOutputStream());
            objToClient = new ObjectInputStream(socketClient.getInputStream());

            while(isClientRunning) {
                System.out.println("Write a command: ");
                commandToSend = command.nextLine();
                objFromClient.writeObject(commandToSend);
                System.out.println("[Server]: "+objToClient.readObject());
            }
        } catch (IOException ioe) {
           System.out.println("Could not connect to "+address+" on port "+port);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("ClassNotFoundException raised");
        }
    }

    /**
     * MAIN DI TEST
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SocketClient client = new SocketClient();
        client.connectSocket();
    }
}
