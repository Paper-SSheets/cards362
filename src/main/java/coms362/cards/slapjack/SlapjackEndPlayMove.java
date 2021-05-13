package coms362.cards.slapjack;

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

public class SlapjackEndPlayMove implements Move {

    private Table table;
    private int winner;

    public SlapjackEndPlayMove(Table table) {
        this.table = table;
    }

    public void apply(Table table) {
        //If player 1 wins
        if (table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards().size() != 0) {
            winner = 1;
            for (Card c : table.getPile(SlapjackPickupRules.CENTER_PILE).getCards()) {
                table.addToPile(SlapjackPickupRules.PLAYER2_PILE, c);
            }
            for (Card c : table.getPile(SlapjackPickupRules.PLAYER2_PILE).getCards())  {
                table.removeFromPile(SlapjackPickupRules.CENTER_PILE, c);
            }
        }
        //If player 2 wins
        else  {
            winner = 2;
            for (Card c : table.getPile(SlapjackPickupRules.CENTER_PILE).getCards()) {
                table.addToPile(SlapjackPickupRules.PLAYER1_PILE, c);
            }
            for (Card c : table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards())  {
                table.removeFromPile(SlapjackPickupRules.CENTER_PILE, c);
            }
        }
    }

    public void apply(ViewFacade view) {
        //if player 1 wins
        if (winner == 1) {
            view.send(new SetGameTitleRemote("Player 1 wins!"));
            for (Card c : table.getPile(SlapjackPickupRules.PLAYER2_PILE).getCards())  {
                view.send(new RemoveFromPileRemote(table.getPile(SlapjackPickupRules.CENTER_PILE), c));
            }
        }
        //if player 2 wins
        else  {
            view.send(new SetGameTitleRemote("Player 2 wins!"));
            for (Card c : table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards())  {
                view.send(new RemoveFromPileRemote(table.getPile(SlapjackPickupRules.CENTER_PILE), c));
            }
        }
        //make sure the cards are evenly distributed
        if (table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards().size() >
                table.getPile(SlapjackPickupRules.PLAYER2_PILE).getCards().size())  {
            for (Card c : table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards())  {
                table.addToPile(SlapjackPickupRules.PLAYER2_PILE, c);
                table.addToPile(SlapjackPickupRules.CENTER_PILE, c);
                if (table.getPile(SlapjackPickupRules.PLAYER2_PILE).getCards().size() == 26)  {
                    break;
                }
            }
            for (Card c : table.getPile(SlapjackPickupRules.CENTER_PILE).getCards())  {
                table.removeFromPile(SlapjackPickupRules.PLAYER1_PILE, c);
                view.send(new RemoveFromPileRemote(table.getPile(SlapjackPickupRules.PLAYER1_PILE), c));
            }
        }
        else if (table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards().size() <
                table.getPile(SlapjackPickupRules.PLAYER2_PILE).getCards().size())  {
            for (Card c : table.getPile(SlapjackPickupRules.PLAYER2_PILE).getCards())  {
                table.addToPile(SlapjackPickupRules.PLAYER1_PILE, c);
                table.addToPile(SlapjackPickupRules.CENTER_PILE, c);
                if (table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards().size() == 26)  {
                    break;
                }
            }
            for (Card c : table.getPile(SlapjackPickupRules.CENTER_PILE).getCards())  {
                table.removeFromPile(SlapjackPickupRules.PLAYER2_PILE, c);
                view.send(new RemoveFromPileRemote(table.getPile(SlapjackPickupRules.PLAYER2_PILE), c));
            }
        }
        //Add one card here for checks later in event P1 size == P2 size
        for (Card c : table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards())  {
            table.addToPile(SlapjackPickupRules.PLAYER1_PILE, c);
            break;
        }
        view.send(new UpdatePileRemote(table.getPile(SlapjackPickupRules.PLAYER1_PILE)));
        view.send(new UpdatePileRemote(table.getPile(SlapjackPickupRules.PLAYER2_PILE)));
        view.send(new UpdatePileRemote(table.getPile(SlapjackPickupRules.CENTER_PILE)));

        SlapjackDealButton dealButton = new SlapjackDealButton("JackDEAL", new Location(0, 0));

        view.register(dealButton);
        view.send(new CreateButtonRemote(dealButton));
    }

}