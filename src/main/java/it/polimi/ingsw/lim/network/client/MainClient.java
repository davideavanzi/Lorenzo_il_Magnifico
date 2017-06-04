package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.network.ui.AbsUI;

import java.util.Scanner;

/**
 * Created by nico.
 */
public class MainClient {
    AbsUI uiType;
    Scanner userInput = new Scanner(System.in);

    public MainClient() {
        setUI();
    }


    private void setUI() {
        boolean exit = false;

        System.out.println("Please choose the user interface you prefer:");
        System.out.println("> CLI\n> GUI");
        while(!exit) {
            System.out.print("$");
            String ui = userInput.nextLine();
            switch (ui) {
                case "CLI":
                    uiType = new CLI();
                    exit = true;
                    break;
                case "GUI":
                    //uiType = new GUI();
                    exit = true;
                    break;
                default:
                    System.out.println("Not a valid user interface");
                    System.out.println("CLI -> Client Line Interface");
                    System.out.println("GUI -> Graphics User Interface");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Lorenzo Il Magnifico!");
        System.out.println();
        MainClient client = new MainClient();
    }
}
