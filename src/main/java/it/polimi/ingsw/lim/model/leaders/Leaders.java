package it.polimi.ingsw.lim.model.leaders;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Strengths;
import it.polimi.ingsw.lim.model.immediateEffects.*;

import java.util.ArrayList;

import java.util.HashMap;

import static it.polimi.ingsw.lim.Settings.*;

/**
 * This class is responsible only of building leadercards that will be needed by the game. It uses the Builder pattern
 * to easily construct leader cards.
 */
public class Leaders {

    public static ArrayList<LeaderCard> buildLeaders() {
        ArrayList<LeaderCard> leaders = new ArrayList<>();
        HashMap<String, Integer> cardsRequirement = new HashMap<>();
        cardsRequirement.put(PURPLE_COLOR, 5);
        leaders.add(new ActivableLeader.Builder().name("Francesco Sforza").id(1).immediateEffect
                (new ActionEffect(new Strengths(1,0,0,0,0,0,0), null))
                .cardsRequirement(cardsRequirement).build());

        cardsRequirement.clear();
        cardsRequirement.put(BLUE_COLOR, 5);
        leaders.add(new PermanentLeader.Builder().name("Ludovico Ariosto").id(2)
                .cardsRequirement(cardsRequirement).build());

        cardsRequirement.clear();
        cardsRequirement.put(YELLOW_COLOR, 5);
        leaders.add(new PermanentLeader.Builder().name("Filippo Brunelleschi").id(3)
                .cardsRequirement(cardsRequirement).build());


        leaders.add(new PermanentLeader.Builder().name("Sigismondo Malatesta").id(4)
                .assetsRequirement(new Assets(0,0,0,0,3,7,0))
                .build());

        leaders.add(new ActivableLeader.Builder().name("Girolamo Savonarola").id(5).immediateEffect
                (new AssetsEffect(new Assets(0,0,0,0,1,0,0)))
                .assetsRequirement(new Assets(18,0,0,0,0,0,0))
                .build());

        leaders.add(new ActivableLeader.Builder().name("Michelangelo Buonarroti").id(6).immediateEffect
                (new AssetsEffect(new Assets(3,0,0,0,0,0,0)))
                .assetsRequirement(new Assets(0,0,10,0,0,0,0))
                .build());

        leaders.add(new ActivableLeader.Builder().name("Giovanni delle Bande Nere").id(7).immediateEffect
                (new AssetsEffect(new Assets(1,1,1,0,0,0,0)))
                .assetsRequirement(new Assets(0,0,0,0,0,12,0))
                .build());

        cardsRequirement.clear();
        cardsRequirement.put(BLUE_COLOR, 4);
        cardsRequirement.put(YELLOW_COLOR, 2);
        leaders.add(new ActivableLeader.Builder().name("Leonardo da Vinci").id(8)
                .immediateEffect(new ActionEffect(new Strengths(0,0,0,0,0,0,0), null))
                .cardsRequirement(cardsRequirement).build());

        leaders.add(new ActivableLeader.Builder().name("Sandro Botticelli").id(9).immediateEffect
                (new AssetsEffect(new Assets(0,0,0,0,0,2,1)))
                .assetsRequirement(new Assets(0,10,0,0,0,0,0))
                .build());

        cardsRequirement.clear();
        cardsRequirement.put(YELLOW_COLOR, 2);
        cardsRequirement.put(GREEN_COLOR, 2);
        cardsRequirement.put(BLUE_COLOR, 2);
        cardsRequirement.put(PURPLE_COLOR, 2);
        leaders.add(new PermanentLeader.Builder().name("Ludovico il moro").id(10)
                .cardsRequirement(cardsRequirement).build());

        cardsRequirement.clear();
        cardsRequirement.put(YELLOW_COLOR, 6);
        cardsRequirement.put(GREEN_COLOR, 6);
        cardsRequirement.put(BLUE_COLOR, 6);
        cardsRequirement.put(PURPLE_COLOR, 6);
        leaders.add(new PermanentLeader.Builder().name("Lucrezia Borgia").id(11)
                .cardsRequirement(cardsRequirement).build());

        cardsRequirement.clear();
        cardsRequirement.put(GREEN_COLOR, 5);
        leaders.add(new ActivableLeader.Builder().name("Federico da Montefeltro").id(12)
                .cardsRequirement(cardsRequirement).build());

        leaders.add(new PermanentLeader.Builder().name("Lorenzo de' Medici").id(13)
                .assetsRequirement(new Assets(0,0,0,0,0,0,35))
                .build());

        leaders.add(new PermanentLeader.Builder().name("Sisto IV").id(14)
                .assetsRequirement(new Assets(6,6,6,6,0,0,0))
                .build());

        cardsRequirement.clear();
        cardsRequirement.put(YELLOW_COLOR, 3);
        leaders.add(new PermanentLeader.Builder().name("Cesare Borgia").id(15).cardsRequirement(cardsRequirement)
                .assetsRequirement(new Assets(12,0,0,0,2,0,0))
                .build());

        leaders.add(new PermanentLeader.Builder().name("Santa Rita").id(16)
                .assetsRequirement(new Assets(0,0,0,0,8,0,0))
                .build());

        cardsRequirement.clear();
        cardsRequirement.put(BLUE_COLOR, 2);
        cardsRequirement.put(YELLOW_COLOR, 4);
        leaders.add(new ActivableLeader.Builder().name("Cosimo de' Medici").id(17)
                .immediateEffect(new AssetsEffect(new Assets(0,0,0,3,0,0,1)))
                .cardsRequirement(cardsRequirement).build());

        cardsRequirement.clear();
        cardsRequirement.put(PURPLE_COLOR, 2);
        cardsRequirement.put(GREEN_COLOR, 4);
        leaders.add(new ActivableLeader.Builder().name("Bartolomeo Colleoni").id(18)
                .immediateEffect(new AssetsEffect(new Assets(0,0,0,0,0,0,4)))
                .cardsRequirement(cardsRequirement).build());

        leaders.add(new ActivableLeader.Builder().name("Ludovico III Gonzaga").id(19)
                .immediateEffect(new CouncilFavorsEffect(1))
                .assetsRequirement(new Assets(0,0,0,15,0,0,0))
                .build());

        cardsRequirement.clear();
        cardsRequirement.put(PURPLE_COLOR, 4);
        cardsRequirement.put(YELLOW_COLOR, 2);
        leaders.add(new PermanentLeader.Builder().name("Pico della Mirandola").id(20)
                .cardsRequirement(cardsRequirement).build());

        return leaders;
    }

}
