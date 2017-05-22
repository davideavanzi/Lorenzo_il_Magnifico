package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This effect class provides a simple bonus in term of resources
 */
public class AssetsEffect extends ImmediateEffect {

    /**
     * Constructor
     */
    public AssetsEffect(Assets bonus) {
        this.bonus = bonus;
    }

    /**
     * The bouns
     */
    private Assets bonus;

    public Assets getBonus(){
        return this.bonus;
    }

}