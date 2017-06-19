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

    private Socket socketClient;
    private User user = null;
    private boolean isClientConnected = true;
    private ObjectOutputStream objFromServer;
    private ObjectInputStream objToServer;

    SocketClientHandler(Socket socketClient) {
        this.socketClient = socketClient;
    }

    /**
     * If the login failed more than 3 times the thread is killed
     */
    public void run() {
        int count = 0;
        int maxTries = 3;

        try {
            // Input and output stream
            objFromServer = new ObjectOutputStream(socketClient.getOutputStream());
            objToServer = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Could not create I/O stream", e);
        }

        while (user == null) {
            try {
                manageLogin();
            } catch (IOException | ClassNotFoundException e) {
                if(++count == maxTries) {
                    getLog().log(Level.SEVERE, "Could not perform login", e);
                    return;
                }
            }
        }
    }

    /**
     * The authenticated user is added in the first available room
     * @param user is the authenticated user
     */
    private void addToRoom(User user) {
        ArrayList<Room> rooms = MainServer.getRoomList();
        if(rooms.isEmpty()) {
          rooms.add(new Room(user));
        } else {
            rooms.get(rooms.size()-1).addUser(user);
        }
    }

    private void manageLogin() throws IOException, ClassNotFoundException {
        String username = (String)objToServer.readObject();
        //TODO: sistema di autenticazione (salvare utenti in un file/db, se utente esistente se vuole caricare stat.)
        // abbiamo gia chiesto la password all'utente

        user = new User(username, this);
        addToRoom(user);
        System.out.println("added to room");
    }

    public void tellToClient(String message) {
        try {
            objFromServer.writeObject(message);
        } catch (Exception e) {
            //TODO: DAJE COSTE EXCEPTION
            e.printStackTrace();
        }

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
                tellToClient("EXECUTE LOGIN:");
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