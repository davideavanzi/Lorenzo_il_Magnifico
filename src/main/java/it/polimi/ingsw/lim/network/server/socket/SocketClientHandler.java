package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;

import static it.polimi.ingsw.lim.Log.*;
import static it.polimi.ingsw.lim.network.SocketConstants.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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
     * User's reference.
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

    /**
     * @return the user.
     */
    public User getUser() {
        return user;
    }



    /**
     * It's used for the updated board and ArrayList of connected (to the server) users.
     * @param board
     * @param players
     */
    public void sendGameToClient(Board board, ArrayList<Player> players) {
        try {
            sendObjectToClient(board);
            sendObjectToClient(players);
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not send game update to the client", e);
        }
    }

    /**
     * This method sends a chat message to the user
     * @param sender
     * @param message
     */
    public void chatMessageToClient(String sender, String message) {
        try {
            sendObjectToClient(CHAT + SPLITTER + sender + SPLITTER + message);
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not send chat message to the client", e);
        }
    }

    /**
     * Print notification to the client.
     * @param message
     */
    public void printToClient(String message) {
        try {
            sendObjectToClient(message);
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not send message to the client", e);
        }
    }

    /**
     * This method is the only that write object to the socket client.
     * @param obj
     * @throws IOException
     */
    private void sendObjectToClient(Object obj) throws IOException {
        objFromServer.writeObject(obj);
        objFromServer.flush();
        objFromServer.reset();
    }

    /**
     * Wait for input and pass it to a parser.
     */
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

    /**
     * Create the I/O socket stream, run until the login is successful then listen for a client command
     */
    public void run() {
        createStream();
        waitRequest();
    }
}