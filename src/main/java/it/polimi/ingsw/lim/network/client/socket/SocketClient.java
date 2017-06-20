package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.ServerInteface;

import java.io.*;
import java.net.Socket;

/**
 * Created by nico.
 */
public class SocketClient implements ServerInteface {
    private String address = "localhost";
    private int port = 8989;
    ObjectOutputStream objFromClient;
    ObjectInputStream objToClient;

    /**
     * Socket client constructor
     */
    public SocketClient() {

    }

    /**
     * @return the server's address
     */
    public String getAddress() {return address;}

    /**
     * @return the server's socket port
     */
    public int getPort() {return port;}


    public void sendLogin(String username) throws ClientNetworkException {
        try {
            objFromClient.writeObject(username);
            objFromClient.flush();
        } catch (IOException e) {
            throw new ClientNetworkException("Could not send login information to server", e);
        }
    }

    /**
     * This method is used for connect to the socket server
     * @throws ClientNetworkException raised if the client could not connect to server
     */
    public void connect() throws ClientNetworkException {
        try {
            // socket, input and output stream
            Socket socketClient = new Socket(getAddress(), getPort());
            objFromClient = new ObjectOutputStream(socketClient.getOutputStream());
            objToClient = new ObjectInputStream(socketClient.getInputStream());
        } catch(IOException e) {
           throw new ClientNetworkException("Could not create I/O stream", e);
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