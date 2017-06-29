package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.ui.UIController;

import java.io.*;
import java.net.Socket;

import static it.polimi.ingsw.lim.network.SocketConstants.*;

/**
 * Created by nico.
 */
public class SocketClient implements Runnable, ServerInterface {

    /**
     * The socket server's address.
     */
    private String address = "localhost";

    /**
     * The socket server's port.
     */
    private int port = 8989;

    /**
     * The socket client's output stream.
     */
    ObjectOutputStream objFromClient;

    /**
     * The socket client's input stream.
     */
    ObjectInputStream objToClient;

    /**
     * Reference to the server command handler.
     */
    ServerCommandHandler commandHandler;

    /**
     * Reference to the UI controller.
     */
    UIController uiController;

    /**
     * Declaration of the Lock.
     */
    Lock lock;

    /**
     * Socket client constructor
     */
    public SocketClient(UIController uiCallback) {
        this.uiController = uiCallback;
        this.commandHandler = new ServerCommandHandler(this, uiCallback);
        uiCallback.setClientProtocol(this);
        this.lock = new Lock();
        lock.lock();

    }

    /**
     * @return the server's address
     */
    private String getAddress() {return address;}

    /**
     * @return the server's socket port
     */
    private int getPort() {return port;}

    /**
     * Send chat message through the client's output stream.
     * @param sender the username of the sender
     * @param message the chat message
     * @throws ClientNetworkException
     */
    public void chatMessageToServer(String sender, String message) throws ClientNetworkException {
        try {
            sendObjToServer(CHAT + SPLITTER + sender + SPLITTER + message);
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send chat message to server", e);
        }
    }

    /**
     * Send login information to server.
     * @param username
     * @throws ClientNetworkException
     */
    public void sendLogin(String username, String password) throws ClientNetworkException {
        try {
            sendObjToServer(LOGIN + SPLITTER + username + SPLITTER + password);
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send login information to server", e);
        }
    }

    /**
     * This method is the only that write object to the socket server.
     * @param obj to send
     * @throws IOException
     */
    private void sendObjToServer(Object obj) throws IOException {
        objFromClient.writeObject(obj);
        objFromClient.flush();
        objFromClient.reset();
    }

    /**
     * Wait forever for object from server.
     * @throws ClientNetworkException
     */
    private void waitFromServer() throws ClientNetworkException {
        lock.lock();
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