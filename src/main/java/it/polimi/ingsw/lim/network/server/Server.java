package it.polimi.ingsw.lim.network.server;

/**
 * Created by Nico.
 * The Server Class invoke the main method.
 */

public class Server {

    public static void main(String[] args) {

        ServerCLI cli = new ServerCLI();
        cli.startCLI();
    }
}
