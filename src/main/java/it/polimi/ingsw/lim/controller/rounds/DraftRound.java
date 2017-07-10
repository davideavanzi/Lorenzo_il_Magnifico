package it.polimi.ingsw.lim.controller.rounds;

import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.exceptions.BadRequestException;
import it.polimi.ingsw.lim.model.leaders.LeaderCard;
import it.polimi.ingsw.lim.model.leaders.Leaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Settings.LEADERS_PER_PLAYER;
import static it.polimi.ingsw.lim.utils.Log.getLog;

/**
 * Created by ava on 09/07/17.
 */
public class DraftRound implements Round, Runnable {

    private HashMap<String, ArrayList<Integer>> chosenLeaders;
    private Room roomCallback;
    private HashMap<String, ArrayList<Integer>> leaderQueue;
    private Random randomGenerator;
    private int timerCounter;
    private int timerDuration;

    public DraftRound(Room roomCallback, int singleDraftDuration) {
        timerCounter = 1;
        timerDuration = singleDraftDuration;
        this.roomCallback = roomCallback;
        roomCallback.setDraftRound(this);
        new RoundTimer(timerDuration, this);
        this.randomGenerator = new Random();
        this.chosenLeaders = new HashMap<>();
        this.leaderQueue = new HashMap<>();
    }

    @Override
    public void run() {
        ArrayList<LeaderCard> leaderCards = new ArrayList<>(Leaders.getAllLeaders());
        for (User user : roomCallback.getUsersList()){
            ArrayList<Integer> firstLeaders = new ArrayList<>();
            for (int i = 0; i < LEADERS_PER_PLAYER; i++)
                firstLeaders.add(leaderCards.remove(randomGenerator.nextInt(leaderCards.size())).getLeaderCardId());
            this.chosenLeaders.put(user.getUsername(), new ArrayList<>());
            this.leaderQueue.put(user.getUsername(), firstLeaders);
        }
        notifyDraft();
    }

    /**
     * This method sends a draft request to each user in the room.
     */
    private void notifyDraft() {
        getLog().log(Level.INFO, "Draft phase number "+timerCounter+" has begun. " +
                "Users have "+timerDuration+" seconds to choose the card to keep.");
        roomCallback.getUsersList().forEach(user -> user.askLeaderDraft(leaderQueue.get(user.getUsername())));
    }

    /**
     * this method executes a new draft phase.
     */
    private void newDraft() {
        ArrayList<String> names = new ArrayList<>(leaderQueue.keySet());
        ArrayList<Integer> tmpQueue = leaderQueue.get(names.get(0));
        for (int i = 0; i < names.size()-1; i++)
            leaderQueue.replace(names.get(i), leaderQueue.get(names.get(i+1)));
        leaderQueue.replace(names.get(names.size()-1), tmpQueue);
        this.timerCounter++;
        notifyDraft();
    }

    /**
     * this method closes the current draft, giving random leaders to players who haven't decided yet
     */
    private void closeDraft(){
        roomCallback.getUsersList().forEach(user -> {
            if (chosenLeaders.get(user.getUsername()).size() < timerCounter)
                chosenLeaders.get(user.getUsername()).add(leaderQueue.get(user.getUsername())
                        .remove(randomGenerator.nextInt(leaderQueue.get(user.getUsername()).size())));
        });
    }

    public void applyChoice(String username, int choice) throws BadRequestException {
        if (chosenLeaders.get(username).size() < timerCounter)
            chosenLeaders.get(username).add(leaderQueue.get(username).remove(choice));
        else throw new BadRequestException("Wrong leader draft choice!");
    }

    @Override
    public void timerEnded() {
        if(timerCounter == LEADERS_PER_PLAYER) {
            closeDraft();
            roomCallback.getUsersList().forEach(user -> roomCallback.getGameController()
                    .giveLeadersToUser(chosenLeaders.get(user.getUsername()), user));
            roomCallback.getDraftLock().unlock();
        } else {
            new RoundTimer(this.timerDuration, this);
            closeDraft();
            newDraft();
        }
    }
}
