package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.server.MainServer;

import static it.polimi.ingsw.lim.Log.*;
import static it.polimi.ingsw.lim.network.ServerConstants.*;
import static it.polimi.ingsw.lim.network.server.MainServer.addUserToRoom;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Show if the client is logged.
     */
    private boolean isClientLogged = false;

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
    public User getUser() { return user; }

    void askClientServants(int minimum) {
        sendObjectToClient(SERVANT + SPLITTER + minimum);
    }

    /**
     * This method sends a chat message to the user
     * @param sender
     * @param message
     */
    void chatMessageToClient(String sender, String message) {
        sendObjectToClient(CHAT + SPLITTER + sender + SPLITTER + message);
    }

    void gameMessageToClient(String message) {
        sendObjectToClient(GAME_MSG + SPLITTER + message);
    }

    /**
     * It's used for the updated board and ArrayList of connected (to the server) users.
     * @param board
     * @param players
     */
    void sendGameToClient(Board board, ArrayList<Player> players) {
        sendObjectToClient(board);
        sendObjectToClient(players);
    }

    void sendIfUserPlaying(boolean isPlaying) {
        sendObjectToClient(TURN + SPLITTER + isPlaying);
    }

    /**
     * This method is the only that write object to the socket client.
     * @param obj
     * @throws IOException
     */
    private void sendObjectToClient(Object obj) {
        try {
            objFromServer.writeObject(obj);
            objFromServer.flush();
            objFromServer.reset();
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not send object to the client", e);
        }
    }

    public void login(String username, String password, SocketClientHandler handlerCallback) throws LoginFailedException {
        try {
            if (MainServer.getJDBC().isAlreadySelectedUserName(username)) {
                if (MainServer.getJDBC().isUserContained(username, password)) {
                    addUserToRoom(new SocketUser(username, handlerCallback));
                    Log.getLog().log(Level.INFO, "[LOGIN]: Login successful. Welcome back ".concat(username));
                } else {
                    Log.getLog().log(Level.SEVERE, "[LOGIN]: Bad password or username ".concat(username).concat("already selected?"));
                    return;
                }
            } else {
                MainServer.getJDBC().insertRecord(username, password);
                this.user = new SocketUser(username, handlerCallback);
                addUserToRoom(this.user);
                Log.getLog().log(Level.INFO, "[LOGIN]: Login successful");
            }
        } catch (SQLException e) {
            Log.getLog().log(Level.SEVERE, "[SQL]: Login failed");
            return;
        }
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
            } catch (IOException | ClassNotFoundException e) {
                getLog().log(Level.SEVERE, "[SOCKET]: Could not receive object from client, " +
                        "maybe client is offline?\nRetrying " + (2 - tries) + " times.", e);
                tries++;
                if (tries == 3) {
                    this.user.hasDied();
                    return;
                }
            }
        }
    }

    private boolean loginRequest() {
        int loginFailed = 0;
        while (true) {
            sendObjectToClient(LOGIN_REQUEST);
            try {
                Object loginInfo = objToServer.readObject();
                ArrayList<String> command = new ArrayList<>(Arrays.asList(((String) loginInfo).split(SPLITTER_REGEX)));
                if (command.get(0).equals(LOGIN)) {
                    login(command.get(1), command.get(2), this);
                    sendObjectToClient(LOGIN_SUCCESSFUL);
                    return isClientLogged = true;
                }
            } catch (IOException | ClassNotFoundException e) {
                getLog().log(Level.SEVERE, ("[SOCKET]: Could not receive login information from client, retrying "
                        + (2 - loginFailed) + " times"), e);
                loginFailed++;
                if (loginFailed == 3) {
                    return isClientLogged = false;
                }
            } catch (LoginFailedException e) {
                sendObjectToClient(LOGIN_FAILED + SPLITTER + e.getMessage());
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
            getLog().log(Level.SEVERE, "[SOCKET]: Could not create I/O stream", e);
        }
    }

    /**
     * Create the I/O socket stream, run until the login is successful then listen for a client command
     */
    public void run() {
        createStream();
        if (loginRequest())
            waitRequest();
    }
}