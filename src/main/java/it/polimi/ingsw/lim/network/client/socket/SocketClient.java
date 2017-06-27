package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.ui.UIController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static it.polimi.ingsw.lim.network.SocketConstants.*;

/**
 * Created by nico.
 */
public class SocketClient implements Runnable, ServerInterface {
    private String address = "localhost";
    private int port = 8989;
    ObjectOutputStream objFromClient;
    ObjectInputStream objToClient;
    ServerCommandHandler commandHandler;
    UIController uiController;
    Lock lock;

    /**
     * Socket client constructor
     */
    public SocketClient(UIController uiCallback) {
        this.uiController = uiCallback;
        this.commandHandler = new ServerCommandHandler(this, uiCallback);
        uiCallback.setClientProtocol(this);
        this.lock = new Lock();
        try {
            lock.lock();
        } catch (InterruptedException e) {
            uiCallback.getClientUI().printMessageln(e.getMessage());
        }

    }

    /**
     * @return the server's address
     */
    public String getAddress() {return address;}

    /**
     * @return the server's socket port
     */
    public int getPort() {return port;}

    public void chatMessageToServer(String sender, String message) throws ClientNetworkException {
        try {
            sendObjToServer(CHAT + SPLITTER + sender + SPLITTER + message);
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send chat message to server", e);
        }
    }

    public void sendLogin(String username) throws ClientNetworkException {
        try {
            sendObjToServer(LOGIN + SPLITTER + username);
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send login information to server", e);
        }
    }

    public void sendObjToServer(Object obj) throws IOException {
        objFromClient.writeObject(obj);
        objFromClient.flush();
        objFromClient.reset();
    }

    private void waitFromServer() throws ClientNetworkException {
        try {
            lock.lock();
        } catch (InterruptedException e) {
            uiController.getClientUI().printMessageln(e.getMessage());
        }
        while(true) {
            try {
                Object command = objToClient.readObject();
                commandHandler.requestHandler(command);
            } catch (IOException | ClassNotFoundException e) {
                throw new ClientNetworkException("Could not get command from server", e);
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
            lock.unlock();
        } catch(IOException e) {
            throw new ClientNetworkException("Could not create I/O stream", e);
        }
    }

    public void run() {
        try {
            waitFromServer();
        } catch (ClientNetworkException e) {
            uiController.getClientUI().printMessageln(e.getMessage());
        }
    }
}