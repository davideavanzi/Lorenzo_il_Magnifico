package it.polimi.ingsw.lim.ui;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.vdurmont.emoji.EmojiParser;
import it.polimi.ingsw.lim.model.leaders.LeaderCard;
import it.polimi.ingsw.lim.model.leaders.Leaders;
import it.polimi.ingsw.lim.utils.Lock;
import it.polimi.ingsw.lim.exceptions.InvalidInputException;
import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.model.cards.*;
import it.polimi.ingsw.lim.model.excommunications.*;
import it.polimi.ingsw.lim.model.immediateEffects.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.ui.UIController.*;
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.*;

/**
 * This is the client command line interface.
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

    /**
     * UI controller callback.
     */
    private UIController uiCallback;

    /**
     * Lock.
     */
    private Lock lock;

    /**
     * It's used for color the output.
     */
    private ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();

    /**
     * Package local constructor.
     * @param uiCallback ui controller callback
     */
    protected CLI(UIController uiCallback) {
        lock = new Lock();
        lock.lock();
        this.uiCallback = uiCallback;
        initializeCommandLine();
    }

    @Override
    public Lock getLock() {
        return lock;
    }

    /**
     * Notify the player that the game is finish.
     */
    public void endGameMessage(ArrayList<Player> scoreboard) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~***" +
                " THE GAME HAS ENDED ***~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        int count = 1;
        printGameMessageln("Scoreboard: ");
        for (Player player : scoreboard) {
            printMessageln(count + ") " + player.getNickname());
            count++;
        }
    }

    /**
     * Ask input to player until it is a integer.
     */
    private void waitForIntInput() {
        while (!userInput.hasNextInt()) {
            input = userInput.next();
            printError(input.concat(" is not a valid number."));
        }
        inputNum = userInput.nextInt();
    }

    /**
     * The method that ask the player the number of servants.
     * @return
     */
    private int askForServant() {
        do {
            waitForIntInput();
        } while (inputNum < 0);
        return inputNum;
    }

    /**
     * Ask the player how many servants he want to deploy to support a bonus harvest move.
     */
    private void askForFastHarvest() {
        printGameMessageln("How many servants do you like to deploy for this harvest? Minimum "
                .concat((String.valueOf(uiCallback.getTmpVar().getMinServantsHarv()))));
        uiCallback.sendFastHarvest(askForServant());
    }

    /**
     * Ask the player how many servants he want to deploy to support a bonus production move.
     */
    private void askForFastProduction() {
        printGameMessageln("How many servants do you like to deploy for this production? Minimum "
                .concat((String.valueOf(uiCallback.getTmpVar().getMinServantsProd()))));
        uiCallback.sendFastProduction(askForServant());
    }

    /**
     * Ask the player the parameters to create a bonus tower move packet to sent to server.
     */
    private void askForFastTowerMove() {
        ArrayList<String> tower = new ArrayList<>();
        printGameMessageln("You can activate a bonus tower move!");
        printMessageln("How many Servants would you like to deploy to support your move?");
        do {
            waitForIntInput();
        } while (inputNum < 0);

        int servantsDeployed = inputNum;
        printMessageln("In which tower would you like to pick a Card?");
        tower.add("Green Tower");
        tower.add("Yellow Tower");
        tower.add("Blue Tower");
        tower.add("Purple Tower");
        if(uiCallback.getLocalPlayers().size() == 5){
            tower.add("Black Tower");
        }
        int count = 1;
        for(String color : tower){
            printMessageln(count + ") " + color);
        }

        //Two/three/four players
        if(uiCallback.getLocalPlayers().size() < 5) {
            do {
                waitForIntInput();
            } while (inputNum < 0 || inputNum > 5);
        }
        //Five players
        if(uiCallback.getLocalPlayers().size() == 5) {
            do {
                waitForIntInput();
            } while (inputNum < 0 || inputNum > 6);
        }
        int chosenTower = inputNum;

        String towerColor = tower.get(chosenTower-1);

        printMessageln("In which floor of ".concat(towerColor.toLowerCase()).concat(" you would you like to pick a card?"));
        do {
            waitForIntInput();
        } while (inputNum < 0 || inputNum > TOWER_HEIGHT);
        int floor = inputNum;
        uiCallback.sendFastTowerMove(servantsDeployed, tower.get(chosenTower-1), floor);
    }

    /**
     * Ask the player what production he want to activate.
     */
    private void askForProductionOptions() {
        String format = "||%-20s||\n";
        String sRid = "||_  _  _  _  _  _  _ ||";
        String s = "________________________";
        ArrayList<Integer> choose = new ArrayList<>();
        printGameMessageln("You can activate a production!");
        printMessageln("");
        printMessageln(s);
        System.out.format(format, "");
        for (ArrayList<Object[]> card : uiCallback.getTmpVar().getOptionsProd()) {
            Integer num = 0;
            System.out.format(format, StringUtils.center((num.toString().concat("->Not Activate")), 20));
            System.out.format(format, "");
            for (Object[] prod : card) {
                num++;
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center((num.toString().concat("->Activate This")), 20));
                System.out.format(format, "");
                if (((Assets)prod[0]).isNotNull()) {
                    printMessageln(sRid);
                    System.out.format(format, "");
                    System.out.format(format, StringUtils.center(("Production Cost:"), 20));
                    printAsset((Assets)prod[0]);
                }
                if(prod[1] instanceof Assets){
                    if (((Assets)prod[0]).isNotNull()) {
                        printMessageln(sRid);
                        System.out.format(format, "");
                        System.out.format(format, StringUtils.center(("Production Result:"), 20));
                        printAsset((Assets) prod[1]);
                    }
                } else if(prod[1] instanceof Integer){
                    if((Integer)prod[1] != 0) {
                        printMessageln(sRid);
                        System.out.format(format, "");
                        System.out.format(format, StringUtils.center((prod[1]).toString(), 20));
                    }
                }
                do {
                    while (!userInput.hasNextInt()) {
                        input = userInput.next();
                        printError(input.concat(" is not a valid number."));
                    }
                    inputNum = userInput.nextInt();
                } while (inputNum < 0 || inputNum > card.size());
                choose.add(inputNum);
            }
        }
        uiCallback.sendProductionOption(choose);
        lock.unlock();
    }

    /**
     * Ask the player if he want to pay with battle point or resources.
     */
    private void askForOptionalBpPick() {
        printGameMessageln("You have picked a card that can be payed in two ways, using resources or battle points.\n".concat(
                "How do you prefer to pay?\n1) Battle Points\n2) Resources"));
        do {
            waitForIntInput();
        } while (!(inputNum.equals(1) || inputNum.equals(2)));
        availableCmdList.remove(OPTIONAL_BP_PICK_CMD);
        uiCallback.sendOptionalBpPick(inputNum.equals(1));
        lock.unlock();
    }

    /**
     * Ask the player which favor he wants.
     */
    private void askForFavor() {
        ArrayList<Integer> favorChoice = new ArrayList<>();
        int count = 0;
        printGameMessage("You can choose " + uiCallback.getTmpVar().getFavorAmount() + "council's favors: ");
        printMessageln("");
        printCouncilFavors();
        do {
            waitForIntInput();
            favorChoice.add(inputNum);
        } while (count < uiCallback.getTmpVar().getFavorAmount() && inputNum >= 0);
        availableCmdList.remove(CHOOSE_FAVOR_CMD);
        uiCallback.sendFavorChoice(favorChoice);
        lock.unlock();
    }

    /**
     * Ask the player if he want suffer the excommunication
     */
    private void askForExcommunication() {
        printGameMessageln("Do you want to suffer the excommunication?");
        printMessageln("1) yes\n2) no");
        do {
            waitForIntInput();
        } while (!(inputNum.equals(1) || inputNum.equals(2)));
        availableCmdList.remove(EXCOMMUNICATION_CMD);
        uiCallback.sendExcommunicationChoice(inputNum.equals(1));
        lock.unlock();
    }

    /**
     * Ask the player what family members' value will be set to 6, independently to the dice.
     */
    private void askPlayerFmToBoost() {
        printMessageln("You have activate Lorenzo Da Monteferltro, please choose a family member to boost to value 6:");
        int count = 1;
        for(FamilyMember fm : uiCallback.getPlayer(uiCallback.getUsername()).getFamilyMembers()) {
            printMessageln(count + ") " + fm);
            count++;
        }
        do {
            waitForIntInput();
        } while (inputNum-1 <= 0 || inputNum-1 > uiCallback.getPlayer(uiCallback.getUsername()).getFamilyMembers().size()-1);
        availableCmdList.remove(LORENZO_MONTEFELTRO_CMD);
        uiCallback.sendFamilyMemberColor(uiCallback.getPlayer(uiCallback.getUsername()).getFamilyMembers().get(inputNum-1).getDiceColor());
    }

    /**
     * Ask the player the leader's name to copy.
     */
    private void askPlayerLeaderToCopy() {
        int count = 1;
        printMessageln("You have activate Lorenzo Il Magnifico, you can copy one of the following leader ability: ");
        for (String leaderName : uiCallback.getTmpVar().getCopyableLeaders()) {
            printMessageln(count + ") " + leaderName);
            count++;
        }
        do {
            waitForIntInput();
        } while (inputNum-1 <= 0 || inputNum-1 > uiCallback.getTmpVar().getCopyableLeaders().size()-1);
        availableCmdList.remove(LORENZO_MEDICI_CMD);
        uiCallback.sendCopyLeader(inputNum-1);
    }

    private void leaderCardDraft() {
        int count = 1;
        printMessageln("Choose one of this four card, the other three will be discarded: ");
        for (int id : uiCallback.getTmpVar().getLeaderOptions()) {
            printMessageln(count + ") " + Leaders.getLeaderById(id));
            count++;
        }
        do {
            waitForIntInput();
        } while (inputNum-1 <= 0 || inputNum-1 > 3);
        availableCmdList.remove(CHOOSE_LEADER_DRAFT_CMD);
        availableCmdList.remove(CHOOSE_LEADER_DRAFT_CMD);
        uiCallback.sendDraftToServer(inputNum-1);
    }

    private void leaderCardManager() {
        String[] list = new String[] {"Activate Leader Card", "Discarded Leader Card", "Deploy Leader Card"};
        int count = 1;
        printMessageln("What action would you like to do?");
        for (String move : list) {
            printMessageln(count + ") " + move);
            count++;
        }
        do {
            waitForIntInput();
        } while (inputNum-1 <= 0 || inputNum-1 > 2);
        userInput.nextLine();
        int actionChoice = inputNum-1;
        count = 1;

        for (LeaderCard card : uiCallback.getPlayer(uiCallback.getUsername()).getLeaderCards()) {
            printMessageln("ID: " + card.getLeaderCardId() + "  Leader's Name: " + card.getCardName());
            count++;
        }
        do {
            waitForIntInput();
        } while (inputNum-1 <= 0);
        uiCallback.sendLeaderAction(actionChoice, inputNum-1);
    }

    /**
     * Ask the player the servants' number to deploy to support the move.
     * @return the servants' number
     */
    private String fmServant() {
        printGameMessage("How many servants would you like to put here? ");
        do {
            waitForIntInput();
        } while (inputNum < 0);
        System.out.println(inputNum);
        return inputNum.toString();
    }

    /**
     * Ask the player the destination.
     * @param board the game board
     * @param numBoard the number of player
     * @param marketSlot the market slot's index
     * @return the chosen destination
     */
    private ArrayList<String> chooseDestination(String[] board, int numBoard, int marketSlot) {
        int count = 1;
        ArrayList<String> destination = new ArrayList<>();
        for (String pos : board) {
            printMessageln(count + ") " + pos);
            count++;
        }
        do {
            waitForIntInput();
        } while (inputNum-1 <= 0 && inputNum-1 > numBoard);

        if (board[inputNum-1].contains("Tower"))
            destination.add(board[inputNum-1].replace(" Tower", ""));
        else
            destination.add(board[inputNum-1]);

        if (inputNum-1 >= 0 && inputNum-1 <4) { //If tower, select the floor
            printGameMessage("Please select the floor: (1/2/3/4) ");
            //String twrColor = destination.get(0).replace(" Tower", "");
            do {
                waitForIntInput();
            } while (inputNum-1 <= 0 && inputNum-1 > 4);
            destination.add(inputNum.toString());

        } else if (inputNum == 8) { //If market, select the slot
            printGameMessage("Please select the market slot: (1/2) ");
            do {
                waitForIntInput();
            } while (inputNum-1 <= 0 && inputNum-1 > marketSlot);
            destination.add(inputNum.toString());
        }
        return destination;
    }

    /**
     * Choose the familiar destination.
     * @return the chosen destination
     */
    private ArrayList<String> fmDestination() {
        String[] board4Player =
                {"GREEN Tower", "YELLOW Tower", "BLUE Tower", "PURPLE Tower", "Council", "Production", "Harvest", "Market"};
        String[] board5Player =
                {"Green Tower", "Yellow Tower", "Blue Tower", "Purple Tower", "Black Tower", "Council", "Production",
                        "Harvest", "Market"};
        printGameMessageln("Where would you like to put it?");

        //Two/Three players
        if (uiCallback.getLocalPlayers().size() <= 3) {
            return chooseDestination(board4Player, 8, 2);
        }

        //Four players
        if (uiCallback.getLocalPlayers().size() < 5) {
            return chooseDestination(board4Player, 8, 4);
        }

        //Five players
        if (uiCallback.getLocalPlayers().size() < 6) {
            return chooseDestination(board5Player, 9, 5);
        }
        return null;
    }

    /**
     * Choose the familiar color.
     * @return the chosen color
     */
    private String fmColor() {
        printGameMessageln("What family member would you like to place?");
        int count = 1;
        for (FamilyMember fm : uiCallback.getPlayer(uiCallback.getUsername()).getFamilyMembers()) {
            printMessageln(count + ") " + fm.getDiceColor());
            count++;
        }
        do {
            waitForIntInput();
        } while (inputNum < 0 && inputNum > count);
        return uiCallback.getPlayer(uiCallback.getUsername()).getFamilyMembers().get(inputNum - 1).getDiceColor();
    }

    /**
     * Create place family member packet and send it to the server.
     */
    private void placeFamilyMember() {
        availableCmdList.remove(FAMILY_MEMBER_CMD);
        uiCallback.sendPlaceFM(fmColor(), fmDestination(), fmServant());
        lock.unlock();
    }

    /**
     * Show information about a specific card.
     * The player must enter where is the card, only the card on board or in personal board are watchable.
     */
    private void showCard() {
        printGameMessageln("Select the position of the card of which you want to see information: ");
        printMessageln("1) Player's Board (nameCard, namePlayer)\n2) Tower(color, floor)");
        do {
            waitForIntInput();
            userInput.nextLine();
        } while (!(inputNum.equals(1) || inputNum.equals(2)));
        if (inputNum.equals(1)) {
            printGameMessage("Enter the name of the card: ");
            String cardName = userInput.nextLine().trim();
            printGameMessage("Enter the name of the player: ");
            String playerName = userInput.nextLine().trim();
            printPlayerSingleCard(cardName, uiCallback.getPlayer(playerName));
        } else {
            ArrayList<String> twrColor = new ArrayList<>();
            twrColor.add("Green");
            twrColor.add("Yellow");
            twrColor.add("Blue");
            twrColor.add("Purple");
            twrColor.add("Black");
            printGameMessageln("Select the tower's color: ");
            int count = 1;
            for (String color : twrColor) {
                printMessageln(count + ") " + color);
                count++;
            }
            printMessageln("");
            do {
                waitForIntInput();
            } while ((inputNum-1 < 0 || inputNum-1 > 3) || (inputNum.equals(5) && (uiCallback.getLocalPlayers().size() == 5)));
            int color = inputNum-1;
            printGameMessage("Please select the floor: (1/2/3/4) ");
            do {
                waitForIntInput();
                userInput.nextLine();
            } while (inputNum-1 < 0 && inputNum-1 > 4);
            int floor = inputNum-1;
            printCardInTower(twrColor.get(color).toUpperCase(), floor);
        }
    }

    /**
     * Print personal information about a specific player.
     * The player's name is ask to player.
     */
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
        printMessageln("");
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

    /**
     * Remove the command to the available command list.
     * @param command to remove
     */
    private void commandRemover(String command) {
        if (availableCmdList.get(command) != null)
            availableCmdList.remove(command);
    }

    /**
     * Add the command to the available command list.
     * @param command the input command.
     */
    @Override
    public void commandAdder(String command) {
        if (availableCmdList.get(command) == null) {
            availableCmdList.put(command, cmdList.get(command));
            printCmd();
        }
    }

    /**
     * If a command failed it is re-inserted in the available command list.
     * @param command
     * @param message
     * @param outcome
     */
    @Override
    public void commandManager(String command, String message, boolean outcome) {
        printMessageln(("[").concat(command).concat("]: ").concat(message));
        if (!outcome)
            availableCmdList.put(command, cmdList.get(command));
    }

    /**
     * cmdExecutor execute the command inserted.
     * @param command command's name
     * @throws InvalidInputException if the command does not correct
     */
    private void cmdExecutor(String command) throws InvalidInputException {
        if (cmdList.get(command) == null)
            throw new InvalidInputException(("[COMMAND_LINE]: Command not found: ").concat(command));
        if (availableCmdList.get(command) == null)
            throw new InvalidInputException(("[COMMAND_LINE]: Command not available: ").concat(command));
        availableCmdList.get(command).run();
    }

    /**
     * Print the available command.
     */
    private void printCmd() {
        availableCmdList.keySet().forEach(command -> System.out.printf("%-30s%s%n", ("-> ~ ")
                .concat(command), ("- ").concat(cmdDescr.get(command))));
        printMessage("");
    }

    /**
     * In the beginning of each round this method is called.
     * @param isMyTurn if true is the player's turn
     */
    @Override
    public void notifyStartRound(boolean isMyTurn) {
        if (isMyTurn) {
            availableCmdList.put(FAMILY_MEMBER_CMD, cmdList.get(FAMILY_MEMBER_CMD));
            availableCmdList.put(LEADER_CARD_CMD, cmdList.get(LEADER_CARD_CMD));

        } else {
            commandRemover(FAMILY_MEMBER_CMD);
            commandRemover(LEADER_CARD_CMD);
            printCmd();
        }
    }

    /**
     * Print the whole game board.
     */
    @Override
    public void printGameBoard() {
        printBoard();
        printTurnSplitter();
        printCmd();
    }

    /**
     * This method is called when the game is created.
     */
    @Override
    public void notifyStartGame () {
        availableCmdList.put(TURN_CMD, cmdList.get(TURN_CMD));
        availableCmdList.put(CARD_CMD, cmdList.get(CARD_CMD));
        availableCmdList.put(BOARD_CMD, cmdList.get(BOARD_CMD));
        availableCmdList.put(ALL_PLAYER_INFO_CMD, cmdList.get(ALL_PLAYER_INFO_CMD));
    }

    /**
     * Listen for user command.
     */
    @Override
    public void waitForRequest() {
        lock.lock();
        printCmd();
        while (true) {
            printGameMessageln("Enter a command: ");
            input = userInput.next().trim();
            try {
                cmdExecutor(input);
            } catch (InvalidInputException e) {
                printError(e.getMessage());
            }
        }
    }

    /**
     * Populate the Command HashMap.
     */
    private void initializeCmdList() {
        cmdList.put(CHAT_CMD, () -> chat());
        cmdList.put(TURN_CMD, () -> turnOrder());
        cmdList.put(CARD_CMD, () -> showCard());
        cmdList.put(BOARD_CMD, () -> printBoard());
        cmdList.put(ALL_PLAYER_INFO_CMD, () -> printPlayerBoard());
        cmdList.put(FAMILY_MEMBER_CMD, () -> placeFamilyMember());
        cmdList.put(LEADER_CARD_CMD, () -> leaderCardManager());
        cmdList.put(CHOOSE_LEADER_DRAFT_CMD, () -> leaderCardDraft());
        cmdList.put(LORENZO_MEDICI_CMD, () -> askPlayerLeaderToCopy());
        cmdList.put(LORENZO_MONTEFELTRO_CMD, () -> askPlayerFmToBoost());
        cmdList.put(EXCOMMUNICATION_CMD, () -> askForExcommunication());
        cmdList.put(CHOOSE_FAVOR_CMD, () -> askForFavor());
        cmdList.put(OPTIONAL_BP_PICK_CMD, () -> askForOptionalBpPick());
        cmdList.put(CHOOSE_PRODUCTION_CMD, () -> askForProductionOptions());
        cmdList.put(SERVANTS_PRODUCTION_CMD, () -> askForFastProduction());
        cmdList.put(SERVANTS_HARVEST_CMD, () -> askForFastHarvest());
        cmdList.put(PICK_FROM_TOWER_CMD, () -> askForFastTowerMove());
    }

    /**
     * Populate the Description HashMap.
     */
    private void initializeCmdDescr() {
        cmdDescr.put(CHAT_CMD, CHAT_DESCR);
        cmdDescr.put(TURN_CMD, TURN_DESCR);
        cmdDescr.put(CARD_CMD, CARD_DESCR);
        cmdDescr.put(BOARD_CMD, BOARD_DESCR);
        cmdDescr.put(ALL_PLAYER_INFO_CMD, ALL_PLAYER_INFO_DESCR);
        cmdDescr.put(FAMILY_MEMBER_CMD, FAMILY_MEMBER_DESCR);
        cmdDescr.put(LEADER_CARD_CMD, LEADER_CARD_DESCR);
        cmdDescr.put(CHOOSE_LEADER_DRAFT_CMD, CHOOSE_LEADER_DRAFT_DESCR);
        cmdDescr.put(PRINT_LEADER_CARD_CMD, PRINT_LEADER_CARD_DESCR);
        cmdDescr.put(LORENZO_MEDICI_CMD, LORENZO_MEDICI_DESCR);
        cmdDescr.put(LORENZO_MONTEFELTRO_CMD, LORENZO_MONTEFELTRO_DESCR);
        cmdDescr.put(EXCOMMUNICATION_CMD, EXCOMMUNICATION_DESCR);
        cmdDescr.put(CHOOSE_FAVOR_CMD, CHOOSE_FAVOR_DESCR);
        cmdDescr.put(OPTIONAL_BP_PICK_CMD, OPTIONAL_BP_PICK_DESCR);
        cmdDescr.put(CHOOSE_PRODUCTION_CMD, CHOOSE_PRODUCTION_DESCR);
        cmdDescr.put(SERVANTS_PRODUCTION_CMD, SERVANTS_PRODUCTION_DESCR);
        cmdDescr.put(SERVANTS_HARVEST_CMD, SERVANTS_HARVEST_DESCR);
        cmdDescr.put(PICK_FROM_TOWER_CMD, PICK_FROM_TOWER_DESCR);
    }

    private void initializeCommandLine() {
        cmdDescr = new HashMap<>();
        availableCmdList = new HashMap<>();
        cmdList = new HashMap<>();

        initializeCmdDescr();
        initializeCmdList();
        availableCmdList.put(CHAT_CMD, cmdList.get(CHAT_CMD));
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
        loginInfo[1] = userInput.nextLine().trim();
        return loginInfo;
    }

    /**
     * Choose the connection protocol and connect to the server.
     */
    @Override
    public String setNetworkSettings() {
        while (true) {
            printMessage("Please select the network protocol: (socket/rmi): ");
            input = userInput.nextLine().toLowerCase();
            switch (input) {
                case "socket":
                case "s":
                    return "socket";
                case "rmi":
                case "r":
                    return "rmi";
                default:
                    printError("[COMMAND_LINE]: Not a valid choice!");
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
        cp.println(EmojiParser.parseToUnicode(":email:  ")+"[CHAT] message from "+sender+" : " +message,
                Ansi.Attribute.BOLD, Ansi.FColor.BLUE, Ansi.BColor.WHITE);
        cp.clear();
    }

    private void printTurnSplitter() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~***" +
                " IT'S YOUR TURN ***~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
    }

    @Override
    public void printGameMessageln(String message) {
        System.out.println("[GAME]: "+message);
    }

    @Override
    public void printGameMessage(String message) {
        System.out.print("[GAME]: "+message);
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
        System.out.println(EmojiParser.parseToUnicode(message));
    }

    /**
     * Print on stdout a message
     * @param message
     */
    @Override
    public void printMessage(String message) {
        System.out.print(EmojiParser.parseToUnicode(message));
    }


    /**
     * Print all the board info in CLI (Towers, Market, VictoryPointsTrack)
     */
    public void printBoard(){
        this.printTowers();
        this.printMarket();
        this.printExcommunications();
        this.printDice(this.uiCallback.getLocalBoard().getDice());
    }

    private void printExcommunications(){
        ArrayList<Excommunication> excommunications = this.uiCallback.getLocalBoard().getExcommunications();
        for(Excommunication excommunication: excommunications){
            printSingleExcommunication(excommunication);
        }
    }

    /**
     * Print a specific excommunication passed like argument.
     * @param excommunication abstract object
     */
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
            System.out.format(format, StringUtils.center("rounds",20));
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

    /**
     * Print a specific AssetsExcommunication passed like argument.
     * @param excommunication object
     */
    private void printSingleExcommunication(AssetsExcommunication excommunication){
        String format = "||%-20s||\n";
        String sRid = "_  _  _  _  _  _  _ ";
        System.out.format(format, sRid);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center("Malus:", 20));
        System.out.format(format, "");
        printAsset(excommunication.getMalus());
    }

    /**
     * Print a specific EndGameAssetsExcommunication passed like argument.
     * @param excommunication object
     */
    private void printSingleExcommunication(EndGameAssetsExcommunication excommunication){
        String format = "||%-20s||\n";
        String sRid = "_  _  _  _  _  _  _ ";
        System.out.format(format, sRid);
        System.out.format(format, "");
        try {
            if (excommunication.getOnAssetsMalus(0).isNotNull()) {
                System.out.format(format, StringUtils.center("For Each:", 20));
                System.out.format(format, "");
                printAsset(excommunication.getOnAssetsMalus(0));
            }
        }catch (NullPointerException e){
            System.out.format(format, StringUtils.center("No Assets Malus:", 20));
            System.out.format(format, "");
        }
        if (excommunication.getProductionCardCostMalus().isNotNull()) {
            System.out.format(format, StringUtils.center("For Each:", 20));
            System.out.format(format, StringUtils.center("of Yellow Card:", 20));
            System.out.format(format, "");
            printAsset(excommunication.getProductionCardCostMalus());
        }
        try {
            if (excommunication.getOnAssetsMalus() != null) {
                if (excommunication.getOnAssetsMalus(1).isNotNull()) {
                    System.out.format(format, sRid);
                    System.out.format(format, "");
                    System.out.format(format, StringUtils.center("Malus:", 20));
                    System.out.format(format, "");
                    printAsset(excommunication.getOnAssetsMalus(1));
                }
            }
        }catch (NullPointerException e){
            System.out.format(format, StringUtils.center("No Assets Malus:", 20));
            System.out.format(format, "");
        }
    }

    /**
     * Print a specific EndGameCardsExcommunication passed like argument.
     * @param excommunication object
     */
    private void printSingleExcommunication(EndGameCardsExcommunication excommunication){
        String format = "||%-20s||\n";
        String sRid = "_  _  _  _  _  _  _ ";
        System.out.format(format, sRid);
        System.out.format(format, "");
        System.out.format(format, StringUtils.center("Blocked Color:", 20));
        System.out.format(format, "");
        System.out.format(format, StringUtils.center(excommunication.getBlockedCardColor(),20));
    }

    /**
     * Print a specific StrengthsExcommunication passed like argument.
     * @param excommunication object
     */
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
                    printMessageln("\n");
                }
            }
            printMessageln("\n");
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
        if (uiCallback.getLocalPlayers().size() == 5) {
            for (Card card : player.getCardsOfColor(BLACK_COLOR)) {
                if (card.getName().equalsIgnoreCase(name)) {
                    printCard(card);
                    return;
                }
            }
        }
        printError("[COMMAND_LINE]: No card found, please re-enter the command");
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
        if(greenCard.getActionStrength().isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Action Cost"), 20));
            printStrengths(greenCard.getActionStrength(), "Cost");
        }
        if(greenCard.getHarvestResult().isNotNull()){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Harvest Result"), 20));
            printAsset(greenCard.getHarvestResult());
        }
        if(greenCard.getCouncilFavourAmount() != 0){
            printMessageln(sRid);
            System.out.format(format, "");
            System.out.format(format, StringUtils.center(("Council Favour: ").concat(((Integer)greenCard.getCouncilFavourAmount()).toString()), 20));
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
            for(java.lang.Object ob: yellowCard.getProductionResults()){
                printMessageln(sRid);
                System.out.format(format, "");
                System.out.format(format, StringUtils.center(("Production Result:"), 20));
                if(ob instanceof Assets){
                    printAsset((Assets)ob);
                }
                else if(ob instanceof Integer){
                    System.out.format(format, StringUtils.center((ob).toString(), 20));
                }
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
            fourthString = "Already Picked";
            if (this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).isOccupied()){
                tab4 = this.uiCallback.getLocalBoard().getTowers().get(color).getFloor(4).getFamilyMemberSlot().getOwnerColor().substring(0 , 1);/*Taking the first char*/
                format = "||%1$-30s|".concat(StringUtils.center(tab4, 4)).concat("|%2$-30s|").concat(tab3).concat("|%3$-30s|").concat(tab2).concat("|%4$-30s|").concat(tab1).concat("||\n");
                tab4 = "\t";
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
                tab3 = "\t";
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
                tab2 = "\t";
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
                tab1 = "\t";
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
        Object[] market = this.uiCallback.getLocalBoard().getMarket().getBonuses();
        FamilyMember[] familyMembers = this.uiCallback.getLocalBoard().getMarket().getSlots();
        String format = "||%-20s||\n";
        String s = "________________________";
        int marketSize = market.length;
        for(int i = 0; i < marketSize; i++){
            printMessageln(s);
            System.out.format(format, "", 20);
            System.out.format(format, StringUtils.center(("MARKET " + (i + 1)), 20));
            System.out.format(format, "_  _  _  _  _  _  _ ");
            System.out.format(format, "");
            if(market[i] instanceof Assets){
                printAsset((Assets)market[i]);
            }
            else if (market[i] instanceof Integer){
                System.out.format(format, StringUtils.center(("Different Favour: " + ((int)market[i])), 20));
            }
            System.out.format(format, "");
            printMessageln(s);
            if(this.uiCallback.getLocalBoard().getMarket().isPositionOccupied(i+1)){
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
        String format = "|| %-17s%-2s||\n";
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

    private void printDice(HashMap<String, Integer> dice){
        String format = "||%1$-10s||%2$-10s||%3$-10s||\n";
        String s = "______________________________________";
        printMessageln(s);
        System.out.format(format, StringUtils.center("Orange", 10), StringUtils.center("Black", 10), StringUtils.center("Withe", 10));
        System.out.format(format, StringUtils.center(dice.get(ORANGE_COLOR).toString(), 10), StringUtils.center(dice.get(BLACK_COLOR).toString(), 10), StringUtils.center(dice.get(WHITE_COLOR).toString(), 10));
        printMessageln(s);
    }
}