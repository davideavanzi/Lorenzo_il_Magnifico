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