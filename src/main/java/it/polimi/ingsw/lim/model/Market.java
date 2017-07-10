package it.polimi.ingsw.lim.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static it.polimi.ingsw.lim.Settings.MAX_USERS_PER_ROOM;
import static it.polimi.ingsw.lim.utils.Log.getLog;

/**
 * This class represents the market. Slots and bonuses are mapped one-to-one
 */
public class Market implements Serializable {

    /**
     *
     */
    private FamilyMember[] slots;
    /**
     *
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = Objects[].class)
    private Object[] bonuses;

    /**
     * Constructor, it creates a market of the proper size based on the number of players in the game
     */
    public Market(int playersNumber, Object[] marketBonuses) {
        //TODO: exception for wrong player number or wrong bonuses number?
        getLog().info("Creating market instance");
        int slotsNumber = 2;
        if(playersNumber <= MAX_USERS_PER_ROOM && playersNumber > 3) //do we have to check if there are more than 6 players?
            slotsNumber = playersNumber;
        this.slots = new FamilyMember[slotsNumber];
        this.bonuses = new Object[slotsNumber];
        for (int i = 0; i < slotsNumber; i++)
            this.bonuses[i] = marketBonuses[i];
    }

    public Market(){
    }

    public void addFamilyMember(FamilyMember fm, int position){
        this.slots[position - 1] = fm;
    }

    @JsonIgnore
    public Object getBonuses(int position){
        int marketPos = position - 1;
        return this.bonuses[marketPos];
    }

    public Object[] getBonuses(){
        return bonuses;
    }

    public void setBonuses(Object[] bonuses){
        this.bonuses = bonuses;
    }

    public FamilyMember[] getSlots() {
        return slots;
    }

    public void setSlots(FamilyMember[] slots){
        this.slots = slots;
    }

    @JsonIgnore
    public boolean isPositionAvailable(int position) {
        return position-1 >= 0 && position-1 < slots.length;
    }

    @JsonIgnore
    public boolean isPositionOccupied(int position){
        return (isPositionAvailable(position) && this.slots[position-1] != null);
    }

    public void clear(){
        getLog().info("Clearing market spaces");
        Arrays.stream(slots).forEach(slot -> slot = null);
    }

    @JsonIgnore
    public int getSize(){
        return this.bonuses.length;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getSlots());
        result = 31 * result + Arrays.hashCode(getBonuses());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Market)) return false;

        Market market = (Market) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getSlots(), market.getSlots())) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getBonuses(), market.getBonuses());
    }
}