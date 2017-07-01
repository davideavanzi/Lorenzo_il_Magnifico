package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.exceptions.InvalidInputException;
import it.polimi.ingsw.lim.model.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Console;
import java.util.HashMap;
import java.util.Scanner;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.ui.UIController.*;
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.*;
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.INFO;
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.TURN;

/**
 * Created by nico.
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
    private int inputNum;

    private UIController uiCallback;

    private Lock lock;

    protected CLI(UIController uiCallback) {
        lock = new Lock();
        lock.lock();
        this.uiCallback = uiCallback;
        initializeCommandLine();
    }

    /**
     * Simply print on stdout a string.
     */
    private void turnForm() {
        printMessageln("Turn order: ");
    }

    @Override
    public int sendServantsToServer(int minimum) {
        printMessageln("How many servants do you want to use in this action? Minimum: " + minimum);
        while(!userInput.hasNextInt()) {
            userInput.next();
        }
        return inputNum = userInput.nextInt();
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

    private void personalInformation() {

    }

    /**
     * Print the currently turn order.
     */
    private void turnOrder() {
        turnForm();
        for (Player pl : getLocalPlayers())
            printMessageln(pl.getNickname());
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

    public void cmdManager(String command) {

    }

    private void cmdExecutor(String command) throws InvalidInputException {
        if (cmdDescr.get(command) == null)
            throw new InvalidInputException(("[COMMAND_LINE]: Command not found: ").concat(command));
        if (availableCmdList.get(command) == null)
            throw new InvalidInputException(("[COMMAND_LINE]: Command not available: ").concat(command));
        availableCmdList.get(command).run();
    }

    private void printCmd() {
        printMessageln("Available Command:");
        printMessageln("");
        availableCmdList.keySet().forEach(command -> System.out.printf("%-30s%s%n", command, cmdDescr.get(command)));
        printMessage("");
    }

    @Override
    public void waitForRequest() {
        while (true) {
            printCmd();
            printMessage("Enter a command: ");
            input = userInput.next().toLowerCase().trim();
            printBoard();
            try {
                cmdExecutor(input);
            } catch (InvalidInputException e) {
                printMessageln(e.getMessage());
            }
            lock.lock();
        }
    }

    private void initializeCommandLine() {
        cmdDescr = new HashMap<>();
        availableCmdList = new HashMap<>();

        //Description HashMap
        cmdDescr.put(CHAT, CHAT_DESCR);
        cmdDescr.put(TURN, TURN_DESCR);
        cmdDescr.put(INFO, INFO_DESCR);
        cmdDescr.put(FAMILY_MEMBER, FAMILY_MEMBER_DESCR);
        cmdDescr.put(LEADER_CARD, LEADER_CARD_DESCR);
        cmdDescr.put(EXCOMMUNICATION, EXCOMMUNICATION_DESCR);
        cmdDescr.put(CHOOSE_FAVOR, CHOOSE_FAVOR_DESCR);
        cmdDescr.put(CHOOSE_TOWER,CHOOSE_TOWER_DESCR);
        cmdDescr.put(CHOOSE_FLOOR, CHOOSE_FLOOR_DESCR);
        cmdDescr.put(CHOOSE_PRODUCTION, CHOOSE_PRODUCTION_DESCR);

        //Command HashMap
        availableCmdList.put(CHAT, () -> chat());
        availableCmdList.put(TURN, () -> turnOrder());
        availableCmdList.put(INFO, () -> personalInformation());

        if(uiCallback.getIsMyTurn()) {
            //availableCmdList.put(FAMILY_MEMBER, () -> );
            //availableCmdList.put(LEADER_CARD, () -> );
        }
    }

    /**
     * Enter the sendLoginInfo information.
     * @return the sendLoginInfo information.
     */
    @Override
    public String[] loginForm() {
        String[] loginInfo = new String[1];
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
                    printMessageln("Not a valid choice!");
            }
        }
    }

    /**
     * Print on stdout a message
     * @param message
     */
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

    public void printBoard(){
        this.printTowers();
        this.printMarket();
        this.printFaithPointsTrack();
        this.printVictoryPointsTrack();
    }

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
            fourthString = "Name: ".concat(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).getCard().getName());
        }
        else{
            fourthString = "Card Already Selected";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).isOccupied()){
                tab4 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).getFamilyMember().getOwnerColor().substring(0 , 1);/*Taking the first char*/
                format = "||%1$-30s|".concat(StringUtils.center(tab4, 4)).concat("|%2$-30s|").concat(tab3).concat("|%3$-30s|").concat(tab2).concat("|%4$-30s|").concat(tab1).concat("||\n");
            }
        }
        if(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(3).hasCard()){
            thirdString = "Name: ".concat(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(3).getCard().getName());
        }
        else{
            thirdString = "Card Already Selected";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(3).isOccupied()){
                tab3 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(3).getFamilyMember().getOwnerColor().substring(0 , 1);/*Taking the first char*/
                format = "||%1$-30s|".concat(tab4).concat("|%2$-30s|").concat(StringUtils.center(tab3, 4)).concat("|%3$-30s|").concat(tab2).concat("|%4$-30s|").concat(tab1).concat("||\n");
            }
        }
        if(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(2).hasCard()){
            secondString = "Name: ".concat(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(2).getCard().getName());
        }
        else{
            secondString = "Card Already Selected";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(24).isOccupied()){
                tab2 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(2).getFamilyMember().getOwnerColor().substring(0 , 1);/*Taking the first char*/
                format = "||%1$-30s|".concat(tab4).concat("|%2$-30s|").concat(tab3).concat("|%3$-30s|").concat(StringUtils.center(tab2, 4)).concat("|%4$-30s|").concat(tab1).concat("||\n");
            }
        }
        if(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(1).hasCard()){
            firstString = "Name: ".concat(this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(1).getCard().getName());
        }
        else{
            firstString = "Card Already Selected";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(1).isOccupied()){
                tab1 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(1).getFamilyMember().getOwnerColor().substring(0 , 1);/*Taking the first char*/
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

    private void printMarket(){
        Assets [] assets = this.uiCallback.getLocalBoard().getMarket().getBonus();
        FamilyMember[] familyMembers = this.uiCallback.getLocalBoard().getMarket().getSlots();
        String format = "||%-20s||\n";
        String s = "________________________";
        for(int i = 0; i < 2; i++){
            printMessageln(s);
            System.out.format(format, "", 20);
            System.out.format(format, StringUtils.center(("MARKET " + i), 20));
            System.out.format(format, "_  _  _  _  _  _  _ ");
            System.out.format(format, "");
            printAsset(assets[i]);
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

    public void printAsset(Assets asset){
        if(asset.getCoins() != 0){
            printMessageln(("|| Coins:\t\t\t"+asset.getCoins()).concat(" ||"));
        }
        if(asset.getWood() != 0){
            printMessageln(("|| Woods:\t\t\t\t"+asset.getWood()).concat(" ||"));
        }
        if(asset.getStone() != 0){
            printMessageln(("|| Stones:\t\t\t"+asset.getStone()).concat(" ||"));
        }
        if(asset.getServants() != 0){
            printMessageln(("|| Servants:\t\t"+asset.getServants()).concat(" ||"));
        }
        if(asset.getFaithPoints() != 0){
            printMessageln(("|| Faith Points:\t\t"+asset.getFaithPoints()).concat(" ||"));
        }
        if(asset.getBattlePoints() != 0){
            printMessageln(("|| Battle Points:\t"+asset.getBattlePoints()).concat(" ||"));
        }
        if(asset.getVictoryPoints() != 0){
            printMessageln(("| Victory Points:\t"+asset.getVictoryPoints()).concat(" ||"));
        }
    }

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