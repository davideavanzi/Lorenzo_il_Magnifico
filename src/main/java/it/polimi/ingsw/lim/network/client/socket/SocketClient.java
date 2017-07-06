package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.ui.UIController;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.network.CommunicationConstants.*;

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
     * The command receive from the server
     */
    Object command;

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

    @Override
    public void optionalBpPick(boolean bpPayment, String username) throws ClientNetworkException {
        try {
            sendObjToServer(OPTIONAL_BP_PICK + SPLITTER + bpPayment);
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send battle point payment request to server", e);
        }
    }

    @Override
    public void favorChoice(ArrayList<Integer> favorChoice, String username) throws ClientNetworkException {
        try {
            sendObjToServer(CHOOSE_FAVOR + SPLITTER + favorChoice);
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send council's favor's choice to server", e);
        }
    }

    @Override
    public void excommunicationChoice(boolean choice, String username) throws ClientNetworkException {
        try {
            sendObjToServer(EXCOMMUNICATION + SPLITTER + choice);
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send excommunication's choice to server", e);
        }
    }

    @Override
    public void placeFM(String color, ArrayList<String> destination, String servants, String username) throws ClientNetworkException {
        try {
            if (destination.get(1) == null)
                sendObjToServer(FAMILY_MEMBER + SPLITTER + color + SPLITTER + destination.get(0) + SPLITTER + servants);
            else
                sendObjToServer(FAMILY_MEMBER + SPLITTER + color + SPLITTER + destination.get(0) + SPLITTER + destination.get(1) + SPLITTER + servants);
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not contact the server to place family member", e);
        }
    }

    /**
     * Send chat message through the client's output stream.
     * @param sender the username of the sender
     * @param message the chat message
     */
    @Override
    public void sendChatMessageToServer(String sender, String message) throws ClientNetworkException {
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
    public void login(String username, String password) throws ClientNetworkException {
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
    private void waitRequest() throws ClientNetworkException {
        lock.lock();
        while(true) {
            try {
                Object command = objToClient.readObject();
                commandHandler.requestHandler(command);
            } catch (IOException | ClassNotFoundException | LoginFailedException e) {
                throw new ClientNetworkException("[SOCKET]: Could not get command from server", e);
            }
        }
    }

    /**
     * This method is used for connect to the socket server
     * @throws ClientNetworkException raised if the client could not connect to server
     */
    @Override
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

    @Override
    public void run() {
        try {
            waitRequest();
        } catch (ClientNetworkException e) {
            uiController.getClientUI().printError(e.getMessage());
        }
    }
}