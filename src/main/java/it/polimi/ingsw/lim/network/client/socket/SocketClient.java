package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.MainClient;
import it.polimi.ingsw.lim.network.client.ServerInteface;
import it.polimi.ingsw.lim.ui.UIController;

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
            objFromClient.writeObject(LOGIN+SPLITTER+username);
            objFromClient.flush();
            objFromClient.reset();
        } catch (IOException e) {
            throw new ClientNetworkException("Could not send login information to server", e);
        }
    }

    public void sendChatMessage(String sender, String message) throws ClientNetworkException{
        try {
            objFromClient.writeObject("CHAT"+SPLITTER+sender+SPLITTER+message);
            objFromClient.flush();
            objFromClient.reset();
        } catch (IOException e) {
            throw new ClientNetworkException("Could not send chat message to server", e);
        }
    }
    private void waitFromServer() throws ClientNetworkException {
        while(true) {
            try {
                Object command = objToClient.readObject();
                commandHandler.requestHandler(command);
                command = null;
            } catch (IOException | ClassNotFoundException e) {
                throw new ClientNetworkException("Could not get command from server", e);
                //return;
            }
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
            objFromClient.flush();
            objToClient = new ObjectInputStream(socketClient.getInputStream());
        } catch(IOException e) {
            throw new ClientNetworkException("Could not create I/O stream", e);
        }
    }

    public void run() {
        try {
            connect();
            waitFromServer();
        } catch (ClientNetworkException e) {
            UIController.getClientUI().printMessageln(e.getMessage());
        }
    }

    private boolean isConnected() {
        return (this.objToClient != null);
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