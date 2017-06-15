package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.AbsClient;
import it.polimi.ingsw.lim.network.client.CLI;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Created by nico.
 */
public class SocketClient extends AbsClient {

    /**
     * Socket client constructor
     */
    public SocketClient() {
        super();
    }


    public SocketClient(String address, int port) {
        super(address, port);
    }

    public void connect() throws ClientNetworkException {
        try {
            // socket, input and output stream
            Socket socketClient = new Socket();
            ObjectOutputStream objFromClient = new ObjectOutputStream(socketClient.getOutputStream());
            ObjectInputStream objToClient = new ObjectInputStream(socketClient.getInputStream());
        } catch(IOException e) {
           throw new ClientNetworkException(e);
        }
    }
}

/*
            Scanner command = new Scanner(System.in);
            String commandToSend;
            while(true) {
                System.out.println("Write a command: ");
                commandToSend = command.nextLine();
                objFromClient.writeObject(commandToSend);
                System.out.println("[Server]: "+ objToClient.readObject());
            }
*/