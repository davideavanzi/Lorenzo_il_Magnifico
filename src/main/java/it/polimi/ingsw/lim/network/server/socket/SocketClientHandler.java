package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.network.server.ClientInterface;
import it.polimi.ingsw.lim.network.server.MainServer;

import static it.polimi.ingsw.lim.Log.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Nico.
 * This class handles the connection to a socket client.
 */
public class SocketClientHandler implements Runnable, ClientInterface {

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
     * Default constructor.
     * @param socketClient
     */
    SocketClientHandler(Socket socketClient) {
        this.socketClient = socketClient;
    }

    /**
     * Create I/O stream for socket connection.
     */
    private void createStream() {
        try {
            // Input and output stream
            objFromServer = new ObjectOutputStream(socketClient.getOutputStream());
            objToServer = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Could not create I/O stream", e);
        }
    }

    /**
     * This is the login method.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void login() throws IOException, ClassNotFoundException {
        String username = (String)objToServer.readObject();
        //TODO: sistema di autenticazione (salvare utenti in un file/db, se utente esistente se vuole caricare stat.)
        user = new User(username, this);
        addUserToRoom(user);
        System.out.println("added to room");
    }

    /**
     * The authenticated user is added to the first available room, if no room is available a new room is created
     * @param user is the authenticated user
     */
    private void addUserToRoom(User user) {
        ArrayList<Room> rooms = MainServer.getRoomList();
        if(rooms.isEmpty()) {
          rooms.add(new Room(user));
        } else {
            rooms.get(rooms.size()-1).addUser(user);
        }
    }

    /*public void requestHandler() {
        while(true) {
            objToServer.readObject();
        }
    }*/

    /**
     * Create the I/O socket stream, run until the login is successful then listen for a client command
     */
    public void run() {
        int loginFailed = 0;

        createStream();
        // If the login failed more than 3 times the thread exit
        while(user == null || loginFailed < 3) {
            try {
                login();
            } catch (IOException | ClassNotFoundException e) {
                loginFailed++;
                getLog().log(Level.SEVERE, "[SOCKET]: Could not perform login", e);
            }
        }
        //requestHandler();
    }

    public void printToClient(String message) {
        try {
            objFromServer.writeObject(message);
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not send String to client", e);
        }

    }

    public int askForServants(int minimum) {
        return 0;
    }

}

/*
        String commandReceived;

        try {
            // Input and output stream
            objFromServer = new ObjectOutputStream(socketClient.getOutputStream());
            objToServer = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE,"Could not open I/O stream", ioe);
        }

        while (isClientConnected) {
            try {
                printToClient("EXECUTE LOGIN:");
                // Read incoming command from the client
                commandReceived = (String)objToServer.readObject();
                System.out.println("Login from user with name: "+commandReceived);

                // Check if server is still running
                if (!(SocketServer.getIsServerRunning())) {
                    getLog().log(Level.INFO,"Server already stopped, killing the client thread");
                    objFromServer.writeObject("Server already stopped");
                    isClientConnected = false;
                }

                // Command List
                switch (commandReceived.toLowerCase()) {
                    case "quit":
                    case "exit":
                        isClientConnected = false;
                        getLog().log(Level.INFO,"Quitting from thread");
                        objFromServer.writeObject("Quitting...");
                        break;

                    case "help":
                        objFromServer.writeObject("Help command");
                        break;

                    default:
                        objFromServer.writeObject("Command not found: "+commandReceived);
                }
            } catch (IOException ioe) {
                getLog().log(Level.SEVERE,"Could not reading incoming command", ioe);
            } catch (ClassNotFoundException cnfe) {
                getLog().log(Level.SEVERE,"ClassNotFoundException raised", cnfe);
            }
        }

        // Close the stream and the client socket
        try {
            objToServer.close();
            objFromServer.close();
            getLog().log(Level.INFO, "The input and output stream are closed");
            socketClient.close();
            getLog().log(Level.INFO, "The server socket is closed");
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE,"Could not close stream or socket", ioe);
        }
*/