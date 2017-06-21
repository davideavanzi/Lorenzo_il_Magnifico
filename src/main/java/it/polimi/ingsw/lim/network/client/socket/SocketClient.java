package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.ServerInteface;
import it.polimi.ingsw.lim.network.client.UIController;

import java.io.*;
import java.net.Socket;
import static it.polimi.ingsw.lim.network.SocketConstants.*;

/**
 * Created by nico.
 */
public class SocketClient implements Runnable, ServerInteface {
    private String address = "localhost";
    private int port = 8989;
    ObjectOutputStream objFromClient;
    ObjectInputStream objToClient;
    ServerCommandHandler commandHandler;
    /**
     * Socket client constructor
     */
    public SocketClient(UIController uiCallback) {
        this.commandHandler = new ServerCommandHandler(this, uiCallback);
        uiCallback.setClientProtocol(this);
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
            objFromClient.writeObject(LOGIN+" "+username);
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
    public void sendChatMessage(String sender, String message) throws ClientNetworkException{
        try {
            objFromClient.writeObject("CHAT "+sender+" "+message);
        } catch (IOException e) {
            throw new ClientNetworkException("Could not send chat message to server", e);
        }
    }

    private void waitFromServer() {
        int tries = 0;
        while(true) {
            System.out.println("IN DA LOOP");
            try {
                Object command = objToClient.readObject();
                commandHandler.requestHandler(command);
            }catch (IOException | ClassNotFoundException e) {
                //TODO: HANDLE
                tries++;
                if (tries == 3) return;
            }
            System.out.println("END OF THE LOOP");
        }
    }

    public void run() {
        try {
            connect();
            System.out.print("Connected, waiting from server.");
            waitFromServer();
        } catch (ClientNetworkException e) {
            //TODO: Handle
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