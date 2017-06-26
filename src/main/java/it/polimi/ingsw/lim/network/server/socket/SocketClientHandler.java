package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.User;

import static it.polimi.ingsw.lim.Log.*;
import static it.polimi.ingsw.lim.network.SocketConstants.SPLITTER;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Nico.
 * This class handles the connection to a socket client.
 */
public class SocketClientHandler implements Runnable {

    /**
     * Socket for communicate with client.
     */
    private Socket socketClient;

    /**
     *
     */
    private User user = null;

    /**
     * Input and output stream for the client-server communication.
     */
    private ObjectOutputStream objFromServer;
    private ObjectInputStream objToServer;

    /**
     * Socket client command handler.
     */
    private ClientCommandHandler clientCommandHandler;

    /**
     * Default constructor.
     * @param socketClient
     */
    SocketClientHandler(Socket socketClient) {
        this.socketClient = socketClient;
        clientCommandHandler = new ClientCommandHandler(this);
    }

    public User getUser() {
        return user;
    }

    /**
     * Create I/O stream for socket connection.
     */
    private void createStream() {
        try {
            // Input and output stream
            this.objFromServer = new ObjectOutputStream(socketClient.getOutputStream());
            objFromServer.flush();
            this.objToServer = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Could not create I/O stream", e);
        }
    }

    private void waitRequest() {
        int tries = 0;
        while(true) {
            try {
                Object command = objToServer.readObject();
                clientCommandHandler.requestHandler(command);
            }catch (IOException | ClassNotFoundException e) {
                getLog().log(Level.SEVERE,"[SOCKET]: Could not receive object from client, " +
                        "maybe client is offline?  \n Retrying "+(2-tries)+" times.");
                tries++;
                if (tries == 3) return;
            }
        }
    }

    /**
     * Create the I/O socket stream, run until the login is successful then listen for a client command
     */
    public void run() {
        createStream();
        waitRequest();
    }

    /**
     * This method sends a chat message to the user
     * @param sender
     * @param message
     */
    public void chatMessageToClient(String sender, String message) {
        try {
            objFromServer.writeObject("CHAT"+SPLITTER+sender+SPLITTER+message);
            objFromServer.flush();
            objFromServer.reset();
        } catch (IOException e) {
            getLog().log(Level.SEVERE, () -> "[SOCKET]: can't send chat message to client");
        }
    }

    public void printToClient(String message) {
        try {
            objFromServer.writeObject(message);
            objFromServer.flush();
            objFromServer.reset();
        } catch (IOException e) {
            getLog().log(Level.SEVERE, () -> "[SOCKET]: Could not send String to client");
        }

    }
}