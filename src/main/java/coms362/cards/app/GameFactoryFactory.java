package coms362.cards.app;

import coms362.cards.abstractcomp.GameFactory;
import coms362.cards.fiftytwo.P52MPGameFactory;
import coms362.cards.fiftytwo.sp.P52SPGameFactory;
import coms362.cards.slapjack.SlapjackGameFactory;

import java.util.Arrays;
import java.util.List;

public class GameFactoryFactory {

    // TODO: list of games is hardcoded for now
    static final String PU52MP = "PU52MP";
    static final String PU52SP = "PU52SP";
    static final String SLAPJACK = "Slapjack";

    String[] gameIds = {PU52MP, PU52SP, SLAPJACK};
    List<String> supported = Arrays.asList(gameIds);

    public GameFactory getGameFactory(String selector) {
        switch (selector) {
            case PU52MP:
                return new P52MPGameFactory();
            case PU52SP:
                return new P52SPGameFactory();

            case SLAPJACK:
                return new SlapjackGameFactory();
        }
        return null;
    }

    public boolean isValidSelection(String gameId) {
        return supported.contains(gameId);
    }

}
