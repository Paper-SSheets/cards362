package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.*;
import coms362.cards.model.Card;
import coms362.cards.model.Location;
import coms362.cards.model.Pile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SlapjackInitCmd implements Move {

    private Table table;
    private Map<Integer, Player> players;
    private String title;

    public SlapjackInitCmd(Map<Integer, Player> players, String title, Table table) {
        this.table = table;
        this.players = players;
        this.title = title;
    }

    public void apply(Table table) {
        Pile randomPile1 = new Pile(SlapjackPickupRules.PLAYER1_PILE, new Location(300, 85), false);
        Pile randomPile2 = new Pile(SlapjackPickupRules.PLAYER2_PILE, new Location(300, 500), false);
        /*
         * Michael: newPile will not show on table, so dont view.send this pile.
         */
        Pile newPile = new Pile(SlapjackPickupRules.NEW_PILE, new Location(100, 500), false);
        /*
         * Michael: need to limit the cards to 26 per pile later.
         */
        try {
            for (String suit : Card.suits) {
                for (int i = 1; i <= 13; i++) {
                    Card card = new Card();
                    card.setSuit(suit);
                    card.setRank(i);
                    card.setX(100);
                    card.setY(500);
                    card.setRotate(0);
                    card.setFaceUp(false);
                    newPile.addCard(card);
                }
            }
            /*
             * Michael: add to a new temp pile then split 26 each.
             */
            int i = 1;
            for (Card c : shuffle(newPile).getCards()) {
                Card card = new Card();
                card.setSuit(c.getSuit());
                card.setRank(c.getRank());
                card.setX(300);
                if (i <= 26) {
                    card.setY(85);
                    card.setRotate(c.getRotate());
                    card.setFaceUp(c.isFaceUp());

                    randomPile1.addCard(card);
                } else {
                    card.setY(500);
                    card.setRotate(c.getRotate());
                    card.setFaceUp(c.isFaceUp());

                    randomPile2.addCard(card);
                }
                i++;
            }

            table.addPile(randomPile1);
            table.addPile(randomPile2);
            table.addPile(new Pile(SlapjackPickupRules.CENTER_PILE, new Location(300, 300), true));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Player p : players.values()) {
            p.addToScore(26);
        }
    }

    public void apply(ViewFacade view) {
        view.send(new SetupTable());
        view.send(new SetGameTitleRemote(title));

        for (Player p : players.values()) {
            String role = (p.getPlayerNum() == 1) ? "Dealer" : "Player " + p.getPlayerNum();
            view.send(new SetBottomPlayerTextRemote(role, p));
        }

        view.send(new CreatePileRemote(table.getPile(SlapjackPickupRules.PLAYER1_PILE)));
        view.send(new CreatePileRemote(table.getPile(SlapjackPickupRules.PLAYER2_PILE)));
        view.send(new CreatePileRemote(table.getPile(SlapjackPickupRules.CENTER_PILE)));
        SlapjackDealButton dealButton = new SlapjackDealButton("JackDEAL", new Location(0, 0));
        view.register(dealButton); //so we can find it later.
        view.send(new CreateButtonRemote(dealButton));
    }


    private Pile shuffle(Pile pile) {
        Pile newShufflePile = new Pile(SlapjackPickupRules.SHUFFLE_PILE, pile.getLocation());
        List<Card> cards = new ArrayList<Card>(pile.getCards());
        Collections.shuffle(cards);
        newShufflePile.getCards().clear();

        for (Card c : cards) {
            Card card = new Card();

            card.setSuit(c.getSuit());
            card.setRank(c.getRank());
            card.setX(c.getX());
            card.setY(c.getY());
            card.setRotate(c.getRotate());
            card.setFaceUp(c.isFaceUp());

            newShufflePile.addCard(card);
        }

        return newShufflePile;
    }


}
