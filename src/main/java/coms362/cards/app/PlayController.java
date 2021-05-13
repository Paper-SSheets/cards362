package coms362.cards.app;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Rules;
import coms362.cards.abstractcomp.Table;
import coms362.cards.events.inbound.Event;
import coms362.cards.events.inbound.GameRestartEvent;
import coms362.cards.events.remote.SetGameTitleRemote;
import coms362.cards.streams.InBoundQueue;

public class PlayController {

    private InBoundQueue inQ;
    private Rules rules;            // This line needs to be refactored
//    private List<Rules> rulesList;  // to be something like this.

    public PlayController(InBoundQueue inQ, Rules rules) {
        this.inQ = inQ;
        this.rules = rules;
    }

    public Event play(Table table, Player player, ViewFacade views) {
        Event nextE = null;
        try {
            while (!table.isMatchOver() && (nextE = inQ.take()) != null) {
                Move move = rules.eval(nextE, table, player);
                move.apply(table);
                move.apply(views);
                if (move.isMatchEnd()) {
                    //System.err.println("Terminating on MatchEnd " + move);
                    //added here to
                    inQ.add(new GameRestartEvent());
                    //views.send(new SetGameTitleRemote("Slapjack"));
                    //break;
                }
            }
        } catch (Exception e) {
            System.err.println("Play terminated on exception: " + e.getMessage());
            e.printStackTrace();
        }
        return nextE;
    }

}
