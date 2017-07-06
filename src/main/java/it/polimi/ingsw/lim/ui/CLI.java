package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.exceptions.InvalidInputException;
import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.model.cards.*;
import it.polimi.ingsw.lim.model.excommunications.*;
import it.polimi.ingsw.lim.model.immediateEffects.*;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableServer.Servant;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.parser.Writer.*;
import static it.polimi.ingsw.lim.ui.UIController.*;
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.*;

/**
 * This is the client commandInput line interface
 */
public class CLI extends AbsUI {

    /**
     * Scanner for stdin.
     */
    private Scanner userInput = new Scanner(System.in);

    /**
     * This contain the input command.
     */
    private String input;

    /**
     * If the input is a int it'll store here.
     */
    private Integer inputNum;

    private UIController uiCallback;

    private Lock lock;

    protected CLI(UIController uiCallback) {
        lock = new Lock();
        lock.lock();
        this.uiCallback = uiCallback;
        initializeCommandLine();
    }

    private void askForBpPick() {
        printGameMessageln("You have picked a card that can be payed in two ways, using resources or battle points.\n" +
                "How do you prefer to pay?\n1) Battle Points\n2) Resources");
        do {
            inputNum = userInput.nextInt();
        } while (!inputNum.equals(1) && !inputNum.equals(2));
        availableCmdList.remove(OPTIONAL_BP_PICK);
        uiCallback.sendOptionalBpPick(inputNum.equals(1));
        lock.unlock();
    }

    private void askForFavor() {
        ArrayList<Integer> favorChoice = new ArrayList<>();
        printGameMessage("You can choose between" + uiCallback.getFavorAmount() + "council's favors: ");
        printCouncilFavors();
        for (int count = 0; userInput.hasNext() && count < uiCallback.getFavorAmount(); count++)
            favorChoice.add(userInput.nextInt());
        availableCmdList.remove(CHOOSE_FAVOR);
        uiCallback.sendFavorChoice(favorChoice);
        lock.unlock();
    }

    private void askForExcommunication() {
        printGameMessageln("Do you want to suffer of the excommunication?");
        printMessageln("1) yes\n2) no");
        do {
            inputNum = userInput.nextInt();
        } while (!inputNum.equals(1) && !inputNum.equals(2));
        availableCmdList.remove(EXCOMMUNICATION);
        uiCallback.sendExcommunicationChoice(inputNum.equals(1));
        lock.unlock();
    }

    private void leaderCardManager() {

    }


    private String fmServant() {
        printGameMessage("How many servants would you like to put here? ");
        do {
            inputNum = userInput.nextInt();
        } while (inputNum >= 0);
        return inputNum.toString();
    }

    private ArrayList<String> fmDestination() {
        String[] board4Player =
                {"Green Tower", "Yellow Tower", "Blue Tower", "Purple Tower", "Council", "Production", "Harvest", "Market"};
        String[] board5Player =
                {"Green Tower", "Yellow Tower", "Blue Tower", "Purple Tower", "Black Tower", "Council", "Production", "Harvest", "Market"};
        ArrayList<String> destination = new ArrayList<>();
        int count = 1;
        printGameMessageln("Where would you like to put it?");

        //Two players
        if (uiCallback.getLocalPlayers().size() < 3) {
            for (String pos : board4Player)
                printMessageln(count + ") " + pos);
            do {
                inputNum = userInput.nextInt();
            } while (inputNum-1 >= 0 && inputNum-1 < 8);
            destination.add(board4Player[inputNum]);
            if (inputNum-1 >= 0 && inputNum-1 < 4) { //If tower, select the floor
                printGameMessage("Please select the floor: (1/2/3/4) ");
                do {
                    inputNum = userInput.nextInt();
                } while (inputNum-1 >= 0 && inputNum-1 < 4);
                destination.add(inputNum.toString());
            } else if (inputNum == 8) { //If market, select the slot
                printGameMessage("Please select the market slot: (1/2) ");
                do {
                    inputNum = userInput.nextInt();
                } while (inputNum-1 >= 0 && inputNum-1 < 2);
                destination.add(inputNum.toString());
            }
            return destination;
        }

        //Three players
        if (uiCallback.getLocalPlayers().size() < 4) {
            for (String pos : board4Player)
                printMessageln(count + ") " + pos);
            do {
                inputNum = userInput.nextInt();
            } while (inputNum-1 >= 0 && inputNum-1 < 8);
            destination.add(board4Player[inputNum]);
            if (inputNum-1 >= 0 && inputNum-1 < 4) { //If tower, select the floor
                printGameMessage("Please select the floor: (1/2/3/4) ");
                do {
                    inputNum = userInput.nextInt();
                } while (inputNum-1 >= 0 && inputNum-1 < 4);
                destination.add(inputNum.toString());
            } else if (inputNum == 8) { //If market, select the slot
                printGameMessage("Please select the market slot: (1/2/3) ");
                do {
                    inputNum = userInput.nextInt();
                } while (inputNum-1 >= 0 && inputNum-1 < 3);
                destination.add(inputNum.toString());
            }
            return destination;
        }

        //Four players
        if (uiCallback.getLocalPlayers().size() < 5) {
            for (String pos : board4Player)
                printMessageln(count + ") " + pos);
            do {
                inputNum = userInput.nextInt();
            } while (inputNum-1 >= 0 && inputNum-1 < 8);
            destination.add(board4Player[inputNum]);
            if (inputNum-1 >= 0 && inputNum-1 < 4) { //If tower, select the floor
                printGameMessage("Please select the floor: (1/2/3/4) ");
                do {
                    inputNum = userInput.nextInt();
                } while (inputNum-1 >= 0 && inputNum-1 < 4);
                destination.add(inputNum.toString());
            } else if (inputNum == 8) { //If market, select the slot
                printGameMessage("Please select the market slot: (1/2/3/4) ");
                do {
                    inputNum = userInput.nextInt();
                } while (inputNum-1 >= 0 && inputNum-1 < 4);
                destination.add(inputNum.toString());
            }
            return destination;
        }

        //Five players
        if (uiCallback.getLocalPlayers().size() < 6) {
            for (String pos : board5Player)
                printGameMessage(count + ") " + pos);
            do {
                inputNum = userInput.nextInt();
            } while (inputNum-1 >= 0 && inputNum-1 < 9);
            destination.add(board5Player[inputNum]);
            if (inputNum-1 >= 0 && inputNum-1 < 5) { //If tower, select the floor
                printGameMessage("Please select the floor: (1/2/3/4) ");
                do {
                    inputNum = userInput.nextInt();
                } while (inputNum-1 >= 0 && inputNum-1 < 5);
                destination.add(inputNum.toString());
            } else if (inputNum == 9) { //If market, select the slot
                printGameMessage("Please select the market slot: (1/2/3/4/5) ");
                do {
                    inputNum = userInput.nextInt();
                } while (inputNum-1 >= 0 && inputNum-1 < 5);
                destination.add(inputNum.toString());
            }
            return destination;
        }
        return null;
    }

    private String fmColor() {
        printGameMessageln("What family member would you like to place?");
        int count = 1;
        for (FamilyMember fm : uiCallback.getPlayer(uiCallback.getUsername()).getFamilyMembers()) {
            printMessageln(count + ") " + fm.getDiceColor());
            count++;
        }
        do {
            inputNum = userInput.nextInt();
        } while (inputNum > 0 && inputNum <= count);
        return uiCallback.getPlayer(uiCallback.getUsername()).getFamilyMembers().get(inputNum - 1).getDiceColor();
    }

    private void placeFamilyMember() {
        availableCmdList.remove(FAMILY_MEMBER);
        uiCallback.sendPlaceFM(fmColor(), fmDestination(), fmServant());
        lock.unlock();
    }

    private void showPersonalInfo() {
        printGameMessage("Enter the username of the player of which you want to see information: ");
        do {
            input = userInput.nextLine().trim();
        } while (uiCallback.getPlayer(input) != null);
        printPlayer(uiCallback.getPlayer(input));
    }

    /**
     * Print the currently turn order.
     */
    private void turnOrder() {
        printGameMessageln("Turn order: ");
        int count = 1;
        for (Player pl : uiCallback.getLocalPlayers()) {
            printMessage(count + ") " + pl.getNickname() + " ");
            count++;
        }
    }

    /**
     * Send a chat message to the server.
     */
    private void chat() {
        printMessage("Enter a chat message: ");
        userInput.nextLine();
        input = userInput.nextLine().trim();
        uiCallback.sendChatMessage(input);
        lock.unlock();
    }

    @Override
    public void commandAdder(String command) {
        availableCmdList.put(command, cmdList.get(command));
    }

    @Override
    public void commandManager(String command, String message, boolean outcome) {
        printMessageln(("[").concat(command).concat("]: ").concat(message));
        if (!outcome)
            availableCmdList.put(command, cmdList.get(command));
    }

    private void cmdExecutor(String command) throws InvalidInputException {
        if (cmdDescr.get(command) == null)
            throw new InvalidInputException(("[COMMAND_LINE]: Command not found: ").concat(command));
        if (availableCmdList.get(command) == null)
            throw new InvalidInputException(("[COMMAND_LINE]: Command not available: ").concat(command));
        availableCmdList.get(command).run();
    }

    private void printCmd() {
        printGameMessageln("Available Command:");
        printMessageln("");
        availableCmdList.keySet().forEach(command -> System.out.printf("%-30s%s%n", command, cmdDescr.get(command)));
        printMessage("");
    }

    @Override
    public void waitForRequest() {
        while (true) {
            printCmd();
            printGameMessage("Enter a command: ");
            input = userInput.next().toLowerCase().trim();
            printBoard(); //todo only for test
            try {
                cmdExecutor(input);
            } catch (InvalidInputException e) {
                printError(e.getMessage());
            }
            lock.lock();
        }
    }

    private void initializeAvailableCmdList() {
        availableCmdList.put(CHAT, () -> chat());
        availableCmdList.put(TURN, () -> turnOrder());
        availableCmdList.put(INFO, () -> showPersonalInfo());
    }

    private void initializeCmdList() {
        cmdList.put(FAMILY_MEMBER, () -> placeFamilyMember());
        //cmdList.put(LEADER_CARD, () -> );
        cmdList.put(EXCOMMUNICATION, () -> askForExcommunication());
        cmdList.put(CHOOSE_FAVOR, () -> askForFavor());
        cmdList.put(OPTIONAL_BP_PICK, () -> askForBpPick());
        //cmdList.put(CHOOSE_PRODUCTION, () -> );
        //cmdList.put(CHOOSE_HARVEST, () -> );
    }

    private void initializeCmdDescr() {
        cmdDescr.put(CHAT, CHAT_DESCR);
        cmdDescr.put(TURN, TURN_DESCR);
        cmdDescr.put(INFO, INFO_DESCR);
        cmdDescr.put(FAMILY_MEMBER, FAMILY_MEMBER_DESCR);
        cmdDescr.put(LEADER_CARD, LEADER_CARD_DESCR);
        cmdDescr.put(EXCOMMUNICATION, EXCOMMUNICATION_DESCR);
        cmdDescr.put(CHOOSE_FAVOR, CHOOSE_FAVOR_DESCR);
        cmdDescr.put(OPTIONAL_BP_PICK, OPTIONAL_BP_PICK_DESCR);
        cmdDescr.put(CHOOSE_PRODUCTION, CHOOSE_PRODUCTION_DESCR);
    }

    private void initializeCommandLine() {
        cmdDescr = new HashMap<>();
        availableCmdList = new HashMap<>();
        cmdList = new HashMap<>();
        initializeCmdDescr();           //Populate Description HashMap
        initializeCmdList();            //Populate Command HashMap
        initializeAvailableCmdList();   //Populate Available Command HashMap
        if(uiCallback.getIsMyTurn()) {
            commandAdder(FAMILY_MEMBER);
            commandAdder(LEADER_CARD);
        }
    }

    /**
     * Enter the login information.
     * @return the login information.
     */
    @Override
    public String[] loginForm() {
        String[] loginInfo = new String[2];
        printMessage("Enter a username: ");
        loginInfo[0] = userInput.nextLine().trim();
        printMessage("Enter a password: ");
        Console console;
        char[] passwd;
        if((console = System.console()) != null) {
            if ((passwd = console.readPassword()) != null) {
                loginInfo[1] = String.valueOf(passwd);
            }
        } else {
            loginInfo[1] = userInput.nextLine().trim();
        }
        return loginInfo;
    }

    /**
     * Choose the connection protocol and connect to the server
     */
    @Override
    public String setNetworkSettings() {
        printMessage("Please select the network protocol: (socket/rmi): ");
        while (true) {
            input = userInput.nextLine().toLowerCase();
            switch (input) {
                case "socket":
                case "s":
                    return "socket";
                case "rmi":
                case "r":
                    return "rmi";
                default:
                    printError("Not a valid choice!");
            }
        }
    }

    /**
     * Print to stdout a chat message.
     * @param sender the sender's username.
     * @param message the chat message.
     */
    @Override
    public void printChatMessage(String sender, String message) {
        printMessageln("[CHAT]: message from "+sender+": "+message);
    }

    @Override
    public void printGameMessageln(String message) {
        printMessageln("[GAME]: "+message);
    }

    @Override
    public void printGameMessage(String message) {
        printMessage("[GAME]: "+message);
    }

    @Override
    public void printError(String errorMessage) {
        System.out.println(errorMessage);
    }

    /**
     * Print on stdout a message
     * @param message
     */
    @Override
    public void printMessageln(String message) {
        System.out.println(message);
    }

    /**
     * Print on stdout a message
     * @param message
     */
    @Override
    public void printMessage(String message) {
        System.out.print(message);
    }

    /**
     * Print all the board info in CLI (Towers, Market, VictoryPointsTrack)
     */
    public void printBoard(){
        this.printTowers();
        this.printMarket();
        this.printExcommunications();
        //this.printFaithPointsTrack();
        //this.printVictoryPointsTrack();
    }

    private void printExcommunications(){
        ArrayList<Excommunication> excommunications = this.uiCallback.getLocalBoard().getExcommunications();
        for(Excommunication excommunication: excommunications){
            printSingleExcommunication(excommunication);
        }
    }

    private void printSingleExcommunication(Excommunication excommunication){
        String format = "||%-20s||\n";
        String s = "________________________";
        String sRid = "_  _  _  _  _  _  _ ";
        printMessageln(s);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center("Excomm. Age: " + excommunication.getAge(), 20));
        System.out.format(format, sRid);
        System.out.format(format, "");
        if(excommunication instanceof AssetsExcommunication){
            System.out.format(format, StringUtils.center("Assets",20));
            System.out.format(format, StringUtils.center("Excommunication",20));
            printSingleExcommunication((AssetsExcommunication)excommunication);
        }
        else if(excommunication instanceof EndGameAssetsExcommunication){
            System.out.format(format, StringUtils.center("End Game Bonus",20));
            System.out.format(format, StringUtils.center("Excommunication",20));
            printSingleExcommunication((EndGameAssetsExcommunication)excommunication);
        }
        else if(excommunication instanceof EndGameCardsExcommunication){
            System.out.format(format, StringUtils.center("End Game Card",20));
            System.out.format(format, StringUtils.center("Excommunication",20));
            printSingleExcommunication((EndGameCardsExcommunication)excommunication);
        }
        else if(excommunication instanceof StrengthsExcommunication){
            System.out.format(format, StringUtils.center("Strengths",20));
            System.out.format(format, StringUtils.center("Excommunication",20));
            printSingleExcommunication((StrengthsExcommunication)excommunication);
        }
        else if(excommunication instanceof MarketExcommunication){
            System.out.format(format, StringUtils.center("Market",20));
            System.out.format(format, StringUtils.center("Excommunication",20));
        }
        else if(excommunication instanceof ServantsExcommunication){
            System.out.format(format, StringUtils.center("Servants",20));
            System.out.format(format, StringUtils.center("Excommunication",20));
        }
        else if(excommunication instanceof RoundExcommunication){
            System.out.format(format, StringUtils.center("Round",20));
            System.out.format(format, StringUtils.center("Excommunication",20));
        }
        for(Player player: this.uiCallback.getLocalPlayers()){
            for(String excommPlayerColor: excommunication.getExcommunicated()){
                if(player.getColor().equals(excommPlayerColor)){
                    System.out.format(format, sRid);
                    System.out.format(format, "");
                    System.out.format(format, StringUtils.center("Excommunicated Player:", 20));
                    System.out.format(format, StringUtils.center(player.getNickname(), 20));
                }
            }
        }
        System.out.format(format, "");
        printMessageln(s);
    }

    private void printSingleExcommunication(AssetsExcommunication excommunication){
        String format = "||%-20s||\n";
        String sRid = "_  _  _  _  _  _  _ ";
        System.out.format(format, sRid);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center("Malus:", 20));
        System.out.format(format, "");
        printAsset(excommunication.getMalus());
    }

    private void printSingleExcommunication(EndGameAssetsExcommunication excommunication){
        String format = "||%-20s||\n";
        String sRid = "_  _  _  _  _  _  _ ";
        System.out.format(format, sRid);
        System.out.format(format, "");
        if(excommunication.getOnAssetsMalus(0).isNotNull()) {
            System.out.format(format, StringUtils.center("For Each:", 20));
            System.out.format(format, "");
            printAsset(excommunication.getOnAssetsMalus(0));
        }
        if(excommunication.getProductionCardCostMalus().isNotNull()) {
            System.out.format(format, StringUtils.center("For Each:", 20));
            System.out.format(format, StringUtils.center("of Yellow Card:", 20));
            System.out.format(format, "");
            printAsset(excommunication.getProductionCardCostMalus());
        }
        if(excommunication.getOnAssetsMalus(1).isNotNull()) {
            System.out.format(format, sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center("Malus:", 20));
            System.out.format(format, "");
            printAsset(excommunication.getOnAssetsMalus(1));
        }
    }

    private void printSingleExcommunication(EndGameCardsExcommunication excommunication){
        String format = "||%-20s||\n";
        String sRid = "_  _  _  _  _  _  _ ";
        System.out.format(format, sRid);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center("Blocked Color:", 20));
        System.out.format(format, "");
        System.out.format(format, StringUtils.center(excommunication.getBlockedCardColor(),20));
    }

    private void printSingleExcommunication(StrengthsExcommunication excommunication){
        String format = "||%-20s||\n";
        String sRid = "_  _  _  _  _  _  _ ";
        System.out.format(format, sRid);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center("Malus:", 20));
        System.out.format(format, "");
        printStrengths(excommunication.getMalus(), "Malus");
    }

    /**
     * Print Player Personal Board info (for each player)
     */
    public void printPlayerBoard(){
        ArrayList<Player> players = this.uiCallback.getLocalPlayers();
        for(Player player: players){
                printPlayer(player);
        }
    }

    /**
     * Print the specific info of a player (all the Card (color by color), the Assets of the Player (like a wallet
     * of resource), and the Strengths (all the bonuses that the Player takes getting Cards)
     * @param player is the player to print
     */
    private void printPlayer(Player player){
        printPlayerCards(GREEN_COLOR, player);
        printPlayerCards(BLUE_COLOR, player);
        printPlayerCards(YELLOW_COLOR, player);
        printPlayerCards(PURPLE_COLOR, player);
        if(this.uiCallback.getLocalPlayers().size() == 5) {
            printPlayerCards(BLACK_COLOR, player);
        }
        String format = "||%-20s||\n";
        String s = "________________________";
        String sRid = "_  _  _  _  _  _  _  _  ";
        printMessageln(s);
        printMessageln("");
        System.out.format(format, StringUtils.center("ASSETS PLAYER: ",20));
        System.out.format(format, StringUtils.center(player.getNickname(), 20));
        printMessageln(sRid);
        if(player.getResources().isNotNull()) {
            printAsset(player.getResources());
        }
        else{
            System.out.format(format, StringUtils.center("No Assets", 20));
        }
        printMessage("");
        printMessage("");
        printMessage("");
        printMessageln(s);
        printMessageln("");
        System.out.format(format, StringUtils.center("STRENGTHS PLAYER: ",20));
        System.out.format(format, StringUtils.center(player.getNickname(), 20));
        printMessageln(sRid);
        if(player.getStrengths().isNotNull()) {
            printStrengths(player.getStrengths(), "Bonus");
        }
        else{
            System.out.format(format, StringUtils.center("No Strengths", 20));
        }
        printMessage("");
        printMessage(s);
        printMessageln("");
        printMessageln("");
    }


    /**
     * Print all the name of the Card that a Player have of a specific color
     * @param color the color Card to print
     * @param player the player that have these Cards
     */
    private void printPlayerCards(String color, Player player){
        String format = "||%-142s||\n";
        String s = ("__________________________________________________________________________________________________________________________________________________");
        String sRid = ("+_  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  +");
        printMessageln(s);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center("PLAYER ".concat(player.getNickname()).concat(": ").concat(color).concat(" CARDS"), 142));
        System.out.format(format, "");
        printMessageln(sRid);
        System.out.format(format, "");
        if(!(player.getCardsOfColor(color).isEmpty())){
            format = "|%1$-40s|";
            int i = 0;
            for(Card card: player.getCardsOfColor(color)){
                System.out.format(format, StringUtils.center(card.getName(), 40));
                i++;
                if(i % 3 == 0) {
                    System.out.println("|\n");
                }
            }
        }
        else{
            System.out.format(format, StringUtils.center("No cards to show", 142));
        }
        printMessageln(s);
        printMessageln("");
        printMessageln("");
    }

    /**
     * Print all the Council Favours Bonus
     */
    public void printCouncilFavors() {
        ArrayList<Assets> councilFavours = this.uiCallback.getLocalBoard().getCouncil().getFavorBonuses();
        String format = "||%-20s||\n";
        String s = "________________________";
        for (Assets a : councilFavours) {
            printMessageln(s);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Council Favour: "), 20));
            System.out.format(format, "_  _  _  _  _  _  _ ");
            System.out.format(format, "");
            printAsset(a);
            printMessageln(s);
        }
    }

    /**
     * Print a specific Card owned by a Player (by the Card's name)
     * @param name the name of the Card to print
     * @param player the Player that have this card
     */
    private void printPlayerSingleCard(String name, Player player){
        for(Card card: player.getCardsOfColor(GREEN_COLOR)){
            if(card.getName().equalsIgnoreCase(name)){
                printCard(card);
                return;
            }
        }
        for(Card card: player.getCardsOfColor(BLUE_COLOR)){
            if(card.getName().equalsIgnoreCase(name)){
                printCard(card);
                return;
            }
        }
        for(Card card: player.getCardsOfColor(YELLOW_COLOR)){
            if(card.getName().equalsIgnoreCase(name)){
                printCard(card);
                return;
            }
        }
        for(Card card: player.getCardsOfColor(PURPLE_COLOR)){
            if(card.getName().equalsIgnoreCase(name)){
                printCard(card);
                return;
            }
        }
        for(Card card: player.getCardsOfColor(BLACK_COLOR)){
            if(card.getName().equalsIgnoreCase(name)){
                printCard(card);
                return;
            }
        }
    }

    /**
     * Print a specific Card in a specific Tower (identified through its color and its floor)
     * @param color the color of the Tower
     * @param floor the floor in which the card is
     */
    public void printCardInTower(String color, int floor){
        if(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(floor).hasCard()) {
            printCard(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(floor).getCardSlot());
        }
        else {
            printMessageln(StringUtils.center(("N o    C a r d    t o    S h o w"), 140));
        }
    }

    /**
     * Print a specific card (this method print all the general info (name, age, cost, ImmediateEffects, color) and
     * then call another method that print all the specific info of the color Card
     * @param card is the card to print
     */
    public void printCard(Card card){
        String format = "||%-20s||\n";
        String s = "________________________";
        String sRid = "||_  _  _  _  _  _  _ ||";
        printMessageln(s);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center(card.getName(), 20));
        System.out.format(format, "");
        System.out.format(format, StringUtils.center("Age: " + card.getAge(), 20));
        if(card.hasCost()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center("Card cost:", 20));
            printAsset(card.getCost());
        }
        if(!(card.getImmediateEffects().isEmpty())){
            for(ImmediateEffect immediateEffect: card.getImmediateEffects()){
                printImmediateEffct(immediateEffect);
            }
        }
        if (card instanceof GreenCard) {
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Green Card"), 20));
            printCard((GreenCard) card);
        }else if (card instanceof BlueCard) {
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Blue Card"), 20));
            printCard((BlueCard) card);
        }else if (card instanceof YellowCard) {
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Yellow Card"), 20));
            printCard((YellowCard) card);
        }else if (card instanceof PurpleCard) {
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Purple Card"), 20));
            printCard((PurpleCard) card);
        } else if (card instanceof BlackCard) {
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Black Card"), 20));
        }
        printMessageln(s);
        printMessageln("");
        printMessageln("");
    }

    /**
     * Print one ImmediateEffect of a Card (call the specific method of the ImmediateEffect type
     * @param immediateEffect the ImmediateEffect to print
     */
    private void printImmediateEffct(ImmediateEffect immediateEffect){
        String format = "||%-20s||\n";
        String sRid = "||_  _  _  _  _  _  _ ||";
        if(immediateEffect instanceof AssetsEffect){
            if(((AssetsEffect)immediateEffect).getBonus().isNotNull()) {
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center("Immediate Bonus:", 20));
                printAsset(((AssetsEffect) immediateEffect).getBonus());
            }
        }else if(immediateEffect instanceof ActionEffect){
            if(((ActionEffect)immediateEffect).getStrength().isNotNull()) {
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center("Immediate Bonus:", 20));
                printStrengths(((ActionEffect) immediateEffect).getStrength(), "Bonus");
                if (((ActionEffect) immediateEffect).hasDiscount()) {
                    printMessageln(sRid);
                    System.out.format(format, "");
                    System.out.format(format, StringUtils.center("Immediate Discount:", 20));
                    printAsset(((ActionEffect) immediateEffect).getDiscount());
                }
            }
        }else if(immediateEffect instanceof AssetsMultipliedEffect){
            if(((AssetsMultipliedEffect)immediateEffect).getMultiplier().isNotNull() &&
                    ((AssetsMultipliedEffect)immediateEffect).getBonus().isNotNull()) {
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center("For Each:", 20));
                printAsset(((AssetsMultipliedEffect) immediateEffect).getMultiplier());
                System.out.format(format, "");
                System.out.format(format, StringUtils.center("You Will Have:", 20));
                printAsset(((AssetsMultipliedEffect) immediateEffect).getBonus());
            }
        }else if(immediateEffect instanceof CardMultipliedEffect){
            if(((CardMultipliedEffect)immediateEffect).getBonus().isNotNull()) {
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center("For Each:", 20));
                System.out.format(format, ((CardMultipliedEffect) immediateEffect).getMultiplierColor().concat(" cards"));
                System.out.format(format, "");
                printAsset(((CardMultipliedEffect) immediateEffect).getBonus());
            }
        }else if(immediateEffect instanceof CouncilFavorsEffect){
            if(((CouncilFavorsEffect)immediateEffect).getAmount() != 0) {
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center("Council Favours:", 20));
                System.out.format(format, StringUtils.center(Integer.toString(((CouncilFavorsEffect) immediateEffect).getAmount()), 20));
            }
        }
    }

    /**
     * Print a GreenCard.
     * The Action Cost is the value (by default Harvest) that the Family Member of the player must afford
     * The Harvest Result are the bonuses that the Player will take activating the Harvest
     * @param greenCard is the Green Card to print
     */
    private void printCard(GreenCard greenCard){
        String format = "||%-20s||\n";
        String sRid = "||_  _  _  _  _  _  _ ||";
        if(greenCard.getHarvestResult().isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Harvest Result"), 20));
            printAsset(greenCard.getHarvestResult());
        }
        if(greenCard.getActionStrength().isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Action Cost"), 20));
            printStrengths(greenCard.getActionStrength(), "Cost");
        }
    }

    /**
     * Print a Blue Card.
     * The Permanent Discount is a discount that the Player will have form the moment that he pick the Card (e.g.
     * the Discount for pick a specific color Card in a Tower)
     * The Pick Discount is a discount (in term of Resources) (e.g. a discount of 2 coins in buying a Blue Card)
     * The Tower Bonus Allowed is a boolean value that said if the player can take the Tower Bonus or not
     * @param blueCard is the BlueCard to print
     */
    private void printCard(BlueCard blueCard){
        String format = "||%-20s||\n";
        String sRid = "||_  _  _  _  _  _  _ ||";
        if (blueCard.getPermanentBonus().isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Permanent Bonus:"), 20));
            printStrengths(blueCard.getPermanentBonus(), "Bonus");
        }
        if(blueCard.getPickDiscount(BLUE_COLOR).isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Blue Discount:"), 20));
            printAsset(blueCard.getPickDiscount(BLUE_COLOR));
        }
        if(blueCard.getPickDiscount(YELLOW_COLOR).isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Yellow Discount:"), 20));
            printAsset(blueCard.getPickDiscount(YELLOW_COLOR));
        }
        if(blueCard.getPickDiscount(PURPLE_COLOR).isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Purple Discount:"), 20));
            printAsset(blueCard.getPickDiscount(PURPLE_COLOR));
        }
        if(blueCard.getPickDiscount(BLACK_COLOR).isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Black Discount:"), 20));
            printAsset(blueCard.getPickDiscount(BLACK_COLOR));
        }
        if(!blueCard.getTowerBonusAllowed()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Tower Bonus"), 20));
            System.out.format(format, StringUtils.center(("not Allowed"), 20));
        }

    }

    /**
     * Print a Yellow Card.
     * The Production Cost is the cost (in term of resource that the player MUST afford (at lest only one if many))
     * (e.g. 1 coin) to activate the production
     * The Production Result is the cost (in term of resource that the player will take if he activates the Production)
     * (e.g. 5 coins)
     * The Action Strengths is the value (in term of Production by default) that the FamilyMember of the Player should
     * have
     * The Card Multiplier is the color of the Cards whose number will be multiplied with a Resources (e.g. if the
     * player has 4 Purple card and he can take 4x1 victoryPoints)
     * @param yellowCard
     */
    private void printCard(YellowCard yellowCard){
        String format = "||%-20s||\n";
        String sRid = "||_  _  _  _  _  _  _ ||";
        if(!(yellowCard.getProductionCosts().isEmpty())){
            for(Assets a: yellowCard.getProductionCosts()){
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center(("Production Cost:"), 20));
                printAsset(a);
            }
        }

        if(!(yellowCard.getProductionResults().isEmpty())){
            for(Assets a: yellowCard.getProductionResults()){
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center(("Production Result:"), 20));
                printAsset(a);
            }
        }
        if(yellowCard.getActionStrength().isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Action Cost:"), 20));
            printStrengths(yellowCard.getActionStrength(), "Cost");
        }
        if(!yellowCard.getCardMultiplier().isEmpty()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Card Multiplier:"), 20));
            System.out.format(format, StringUtils.center((yellowCard.getCardMultiplier().concat(" cards")), 20));
        }
    }

    /**
     * Print a Purple Card.
     * The End Game Bonus is the bonus (by default Victory Points) that the player take in the ending of the game
     * The Battle Point Requirement is the value of Battle Point that the player MUST have at least to can afford this
     * card (is an optional value)
     * The Battle Point Cost is the the cost (in Battle Points)
     * @param purpleCard the Purple Card to print
     */
    private void printCard(PurpleCard purpleCard){
        String format = "||%-20s||\n";
        String sRid = "||_  _  _  _  _  _  _ ||";
        if(purpleCard.getEndgameBonus().isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("End Game Bonus:"), 20));
            printAsset(purpleCard.getEndgameBonus());
        }
        if(purpleCard.getOptionalBpRequirement() != 0){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Battle Point"), 20));
            System.out.format(format, StringUtils.center(("Requirement:"), 20));
            System.out.format(format, StringUtils.center(Integer.toString(purpleCard.getOptionalBpRequirement()), 20));
        }
        if(purpleCard.getOptionalBpCost() != 0){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Battle Point Cost:"), 20));
            System.out.format(format, StringUtils.center(Integer.toString(purpleCard.getOptionalBpRequirement()), 20));
        }
    }

    /**
     * Print a specific Tower by its color. (all card and family member slot)
     * @param color is the color of the tower to print
     */
    private void printTower(String color){
        String format = "||%-142s||\n";
        String s = ("__________________________________________________________________________________________________________________________________________________");
        String sRid = ("+_  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  +");
        printMessageln(s);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center(color.concat(" TOWER"), 142));
        System.out.format(format, "");
        printMessageln(sRid);
        String tab1 = "\t";
        String tab2 = "\t";
        String tab3 = "\t";
        String tab4 = "\t";
        format = "||%1$-30s|".concat(tab4).concat("|%2$-30s|").concat(tab3).concat("|%3$-30s|").concat(tab2).concat("|%4$-30s|").concat(tab1).concat("||\n");
        System.out.format(
                format,
                StringUtils.center("4th Floor", 30),
                StringUtils.center("3rd Floor", 30),
                StringUtils.center("2nd Floor", 30),
                StringUtils.center("1th Floor", 30)
        );
        System.out.format(format, "", "", "", "");
        String fourthString;
        String thirdString;
        String secondString;
        String firstString;
        if(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).hasCard()){
            fourthString = "Name: ".concat(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).getCardSlot().getName());
        }
        else{
            fourthString = "Card Already Selected";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).isOccupied()){
                tab4 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).getFamilyMemberSlot().getOwnerColor().substring(0 , 1);/*Taking the first char*/
                format = "||%1$-30s|".concat(StringUtils.center(tab4, 4)).concat("|%2$-30s|").concat(tab3).concat("|%3$-30s|").concat(tab2).concat("|%4$-30s|").concat(tab1).concat("||\n");
            }
        }
        if(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(3).hasCard()){
            thirdString = "Name: ".concat(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(3).getCardSlot().getName());
        }
        else{
            thirdString = "Card Already Selected";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(3).isOccupied()){
                tab3 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(3).getFamilyMemberSlot().getOwnerColor().substring(0 , 1);/*Taking the first char*/
                format = "||%1$-30s|".concat(tab4).concat("|%2$-30s|").concat(StringUtils.center(tab3, 4)).concat("|%3$-30s|").concat(tab2).concat("|%4$-30s|").concat(tab1).concat("||\n");
            }
        }
        if(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(2).hasCard()){
            secondString = "Name: ".concat(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(2).getCardSlot().getName());
        }
        else{
            secondString = "Card Already Selected";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(24).isOccupied()){
                tab2 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(2).getFamilyMemberSlot().getOwnerColor().substring(0 , 1);/*Taking the first char*/
                format = "||%1$-30s|".concat(tab4).concat("|%2$-30s|").concat(tab3).concat("|%3$-30s|").concat(StringUtils.center(tab2, 4)).concat("|%4$-30s|").concat(tab1).concat("||\n");
            }
        }
        if(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(1).hasCard()){
            firstString = "Name: ".concat(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(1).getCardSlot().getName());
        }
        else{
            firstString = "Card Already Selected";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(1).isOccupied()){
                tab1 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(1).getFamilyMemberSlot().getOwnerColor().substring(0 , 1);/*Taking the first char*/
                format = "||%1$-30s|".concat(tab4).concat("|%2$-30s|").concat(tab3).concat("|%3$-30s|").concat(tab2).concat("|%4$-30s|").concat(StringUtils.center(tab1, 4)).concat("||\n");
            }
        }
        System.out.format(
                format,
                StringUtils.center(fourthString, 30),
                StringUtils.center(thirdString, 30),
                StringUtils.center(secondString, 30),
                StringUtils.center(firstString, 30)
        );
        System.out.format(format, "", "", "", "");
        System.out.format(format, "", "", "", "");
        printMessageln(s);
        printMessageln("");
        printMessageln("");
    }

    /**
     * Print all the market slot (family Member Slot and bonuses (resources) of this game (depending by the number
     * of players).
     */
    private void printMarket(){
        Object [] market = this.uiCallback.getLocalBoard().getMarket().getBonuses();
        FamilyMember[] familyMembers = this.uiCallback.getLocalBoard().getMarket().getSlots();
        String format = "||%-20s||\n";
        String s = "________________________";
        int marketSize = 2;
        if(this.uiCallback.getLocalPlayers().size() >= 4 && this.uiCallback.getLocalPlayers().size() < 6){
            marketSize = this.uiCallback.getLocalPlayers().size();
        }
        for(int i = 0; i < marketSize/*the num of the player == dim of the market*/; i++){
            printMessageln(s);
            System.out.format(format, "", 20);
            System.out.format(format, StringUtils.center(("MARKET " + i), 20));
            System.out.format(format, "_  _  _  _  _  _  _ ");
            System.out.format(format, "");
            if(market[i] instanceof Assets){
                printAsset((Assets)market[i]);
            }
            else{
                System.out.format(format, StringUtils.center((" " + ((int)market[i])), 20));
            }
            System.out.format(format, "");
            printMessageln(s);
            if(this.uiCallback.getLocalBoard().getMarket().isPositionOccupied(i)){
                printMessageln("");
                printMessageln("");
                printMessageln(s);
                System.out.format(format, StringUtils.center(familyMembers[i].getOwnerColor(), 20));
                printMessageln(s);
            }
        }
        printMessageln("");
        printMessageln("");
    }

    /**
     * Print all the towers (calling the sub method to print one specific tower)
     */
    private void printTowers(){
        this.printTower(GREEN_COLOR);
        printMessageln("");
        this.printTower(BLUE_COLOR);
        printMessageln("");
        this.printTower(YELLOW_COLOR);
        printMessageln("");
        this.printTower(PURPLE_COLOR);
        printMessageln("");
    }

    /**
     * Print the Strengths Bonus (e.g. +3 in the Production move)
     * @param strengths
     */
    private void printStrengths(Strengths strengths, String type){
        String format = "|| %-17s%-2s||\n";  //todo 20 o 19
        if(strengths.getTowerStrength(GREEN_COLOR) != 0 ){
            System.out.format(format, GREEN_COLOR.concat(" Tower:"), Integer.toString(strengths.getTowerStrength(GREEN_COLOR)));
        }
        if(strengths.getTowerStrength(BLUE_COLOR) != 0 ){
            System.out.format(format, BLUE_COLOR.concat(" Tower:"), Integer.toString(strengths.getTowerStrength(BLUE_COLOR)));
        }
        if(strengths.getTowerStrength(YELLOW_COLOR) != 0 ){
            System.out.format(format, YELLOW_COLOR.concat(" Tower:"), Integer.toString(strengths.getTowerStrength(YELLOW_COLOR)));
        }
        if(strengths.getTowerStrength(PURPLE_COLOR) != 0 ){
            System.out.format(format, PURPLE_COLOR.concat(" Tower:"), Integer.toString(strengths.getTowerStrength(PURPLE_COLOR)));
        }

        if(strengths.getHarvestBonus() != 0){
            System.out.format(format, "Harvest ".concat(type).concat(":"), Integer.toString(strengths.getHarvestBonus()));
        }
        if(strengths.getProductionBonus() != 0){
            System.out.format(format, "Production ".concat(type).concat(":"), Integer.toString(strengths.getProductionBonus()));
        }
        if(strengths.getDiceBonus().get(ORANGE_COLOR) != 0){
            System.out.format(format, ORANGE_COLOR.concat(" dice:"), "+" + strengths.getDiceBonus().get(ORANGE_COLOR));
        }
        if(strengths.getDiceBonus().get(BLACK_COLOR) != 0){
            System.out.format(format, BLACK_COLOR.concat(" dice:"), "+" + strengths.getDiceBonus().get(BLACK_COLOR));
        }
        if(strengths.getDiceBonus().get(WHITE_COLOR) != 0){
            System.out.format(format, WHITE_COLOR.concat(" dice:"), "+" + strengths.getDiceBonus().get(WHITE_COLOR));
        }
    }

    /**
     * print an Assets (resources or costs)
     * @param asset
     */
    private void printAsset(Assets asset){
        String format = "|| %-17s%-2s||\n";
        if(asset.getCoins() != 0){
            System.out.format(format, "Coins:", Integer.toString(asset.getCoins()));
        }
        if(asset.getWood() != 0){
            System.out.format(format, "Woods:", Integer.toString(asset.getWood()));
        }
        if(asset.getStone() != 0){
            System.out.format(format, "Stone:", Integer.toString(asset.getStone()));
        }
        if(asset.getServants() != 0){
            System.out.format(format, "Servants:", Integer.toString(asset.getServants()));
        }
        if(asset.getFaithPoints() != 0){
            System.out.format(format, "Faith Points:", Integer.toString(asset.getFaithPoints()));
        }
        if(asset.getBattlePoints() != 0){
            System.out.format(format, "Battle Points:", Integer.toString(asset.getBattlePoints()));
        }
        if(asset.getVictoryPoints() != 0){
            System.out.format(format, "Victory Points:", Integer.toString(asset.getVictoryPoints()));
        }
    }

    /**
     * print the faith track (with bonus and family member)
     */
    private void printFaithPointsTrack(){
        Assets[] assets = this.uiCallback.getLocalBoard().getFaithTrack();
        String format = "||%-20s||\n";
        String s = "________________________";
        for(int i = 0; i < FAITH_TRACK_LENGTH ; i++){
            printMessageln(s);
            System.out.format(format, "", 20);
            System.out.format(format, StringUtils.center(("Faith Track: " + i), 20));
            System.out.format(format, "_  _  _  _  _  _  _ ");
            System.out.format(format, "");
            if(assets[i] != null) {
                printAsset(assets[i]);
            }
            else{
                System.out.format(format, StringUtils.center(("No Assets"), 20));
            }
            System.out.format(format, "");
            printMessageln(s);
            printMessageln("");
            printMessageln("");
        }
    }

    /**
     * print the victory points track (with family member)
     */
    private void printVictoryPointsTrack(){
        String format = "||%1$-6s|%2$-6s|%3$-6s|%4$-6s|%5$-6s|%6$-6s|%7$-6s|%8$-6s|%9$-6s|%10$-6s|%11$-6s|%12$-6s|%13$-6s|%14$-6s|%15$-6s|%16$-6s|%17$-6s|%18$-6s|%19$-6s|%20$-6s||\n";
        String s = "_______________________________________________________________________________________________________________________________________________";
        printMessageln(s);
        String sRid = "_  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _  _ ";
        for (int i = 0; i < 100; i += 20){
            System.out.format(
                    format,
                    StringUtils.center(("VP:" + (i + 1)), 6),
                    StringUtils.center(("VP:" + (i + 2)), 6),
                    StringUtils.center(("VP:" + (i + 3)), 6),
                    StringUtils.center(("VP:" + (i + 4)), 6),
                    StringUtils.center(("VP:" + (i + 5)), 6),
                    StringUtils.center(("VP:" + (i + 6)), 6),
                    StringUtils.center(("VP:" + (i + 7)), 6),
                    StringUtils.center(("VP:" + (i + 8)), 6),
                    StringUtils.center(("VP:" + (i + 9)), 6),
                    StringUtils.center(("VP:" + (i + 10)), 6),
                    StringUtils.center(("VP:" + (i + 11)), 6),
                    StringUtils.center(("VP:" + (i + 12)), 6),
                    StringUtils.center(("VP:" + (i + 13)), 6),
                    StringUtils.center(("VP:" + (i + 14)), 6),
                    StringUtils.center(("VP:" + (i + 15)), 6),
                    StringUtils.center(("VP:" + (i + 16)), 6),
                    StringUtils.center(("VP:" + (i + 17)), 6),
                    StringUtils.center(("VP:" + (i + 18)), 6),
                    StringUtils.center(("VP:" + (i + 19)), 6),
                    StringUtils.center(("VP:" + (i + 20)), 6)
            );
            printMessageln(sRid);
            System.out.format(
                    format,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
            );
            printMessageln(s);
            printMessageln("");
        }
        printMessageln("");
        printMessageln("");
    }
}