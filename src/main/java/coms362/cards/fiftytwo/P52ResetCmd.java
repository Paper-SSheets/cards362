package coms362.cards.fiftytwo;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.*;
import coms362.cards.model.Card;
import coms362.cards.model.Location;
import coms362.cards.model.Pile;

import java.util.Map;
import java.util.Random;

public class P52ResetCmd implements Move {

    private Table table;
    private Map<Integer, Player> players;
    private String title;

    public P52ResetCmd(Map<Integer, Player> players, String title, Table table) {
        this.players = players;
        this.title = title;
        this.table = table;
    }

    public void apply(Table table) {
        //table.getPile(P52Rules.RANDOM_PILE).getCards().addAll(table.getPile(P52Rules.DISCARD_PILE).getCards());
        //idk why this wont work
        for (Card c : table.getPile(P52Rules.DISCARD_PILE).getCards())  {
            //table.removeFromPile(P52Rules.DISCARD_PILE, c);
            table.addToPile(P52Rules.RANDOM_PILE, c);
        }
        //table.getPile(P52Rules.DISCARD_PILE).getCards().clear();
    }

    public void apply(ViewFacade view) {
        for (Card c : table.getPile(P52Rules.RANDOM_PILE).getCards()) {
            String outVal = "";
            view.send(new HideCardRemote(c));
            view.send(new RemoveFromPileRemote(table.getPile(P52Rules.DISCARD_PILE), c));
            //view.send(new AddToPileRemote(table.getPile(P52Rules.RANDOM_PILE), c));
            view.send(new ShowCardRemote(c));
            //table.removeFromPile(P52Rules.DISCARD_PILE, c);
            //view.send(new RemoveFromPileRemote(table.getPile(P52Rules.DISCARD_PILE), c)); does this do anything?
            //view.send(new HideCardRemote(c));
            //view.send(new CreateCardRemote(c));
            //view.send(new UpdateCardRemote(c));
            System.out.println(outVal);
        }
//        view.send(new SetupTable());
//        view.send(new SetGameTitleRemote(title));

//        for (Player p : players.values()) {
//            String role = (p.getPlayerNum() == 1) ? "Dealer" : "Player " + p.getPlayerNum();
//            view.send(new SetBottomPlayerTextRemote(role, p));
//        }
        //table.getPile(P52Rules.DISCARD_PILE).getCards().clear();
        view.send(new UpdatePileRemote(table.getPile(P52Rules.RANDOM_PILE)));
        view.send(new UpdatePileRemote(table.getPile(P52Rules.DISCARD_PILE)));
//        view.send(new CreatePileRemote(table.getPile(P52Rules.RANDOM_PILE)));
//        view.send(new CreatePileRemote(table.getPile(P52Rules.DISCARD_PILE)));
        DealButton dealButton = new DealButton("DEAL", new Location(0, 0));
        view.register(dealButton); //so we can find it later.
        view.send(new CreateButtonRemote(dealButton));
        //view.send(new CreateButtonRemote(Integer.toString(getNextId()), "reset", "RestartGame", "Reset", new Location(500,0)));
        //view.send(new CreateButtonRemote(Integer.toString(getNextId()), "clear", "ClearTable", "Clear Table", new Location(500,0)));
    }

}