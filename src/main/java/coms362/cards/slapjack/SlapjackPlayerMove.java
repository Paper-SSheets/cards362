package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.*;
import coms362.cards.model.Card;
import coms362.cards.model.Location;
import coms362.cards.model.Pile;

public class SlapjackPlayerMove implements Move {
    private Card c;
    private Player p;
    private Pile fromPile;
    private Pile toPile;
    private Table table;
    private String from;
    private String to;
    private Pile tempPile;

    public SlapjackPlayerMove(Card c, Player p, Pile fromPile, Pile toPile) {
//		super(c, p, fromPile, toPile);
        this.c = c;
        this.p = p;
        this.fromPile = fromPile;
        this.toPile = toPile;
    }
    public SlapjackPlayerMove(Card c, Player p, Pile fromPile, Pile toPile, String from, String to)	{
        this.c = c;
        this.p = p;
        this.fromPile = fromPile;
        this.toPile = toPile;
        this.from = from;
        this.to = to;
    }

    @Override
    public void apply(Table table) {
        if(fromPile == table.getPile(SlapjackPickupRules.CENTER_PILE))
        {
            tempPile = new Pile("tempPile", new Location(0,0));
            for(Card card: fromPile.getCards())
            {
                table.addToPile(toPile.selector, card);
                tempPile.addCard((card));
            }
            for(Card card: tempPile.getCards())
            {
                table.removeFromPile(fromPile.selector, card);
            }

            if(c.getRank() == 11) {
                table.addToScore(p, tempPile.getCards().size());
            } else {
                if(p.equals(table.getPlayer(1))) {
                    table.addToScore(table.getPlayer(2), tempPile.getCards().size());
                }
                else{
                    table.addToScore(table.getPlayer(1), tempPile.getCards().size());
                }
            }
        } else {
            table.removeFromPile(fromPile.selector, c);

            System.out.println(table.toString());
            table.addToPile(toPile.selector, c);
            table.addToScore(p, -1);

        }
        this.table = table;
    }

    @Override
    public void apply(ViewFacade view) {
        if(fromPile.selector == SlapjackPickupRules.CENTER_PILE)
        {
            for(Card card: tempPile.getCards())
            {
                view.send(new AddToPileBottomRemote(toPile,card));
                view.send(new RemoveFromPileRemote(fromPile, card));
            }
            view.send(new ShowPlayerScore(table.getPlayer(1), null));
            view.send(new ShowPlayerScore(table.getPlayer(2), null));
        } else {
            view.send(new UpdatePileRemote(toPile));
            view.send(new HideCardRemote(c));
            view.send(new RemoveFromPileRemote(fromPile, c));
            view.send(new AddToPileRemote(toPile, c));
            view.send(new ShowCardRemote(c));
            view.send(new ShowPlayerScore(p, null));
        }
    }

    public boolean isMatchEnd()  {
        if (p.getPlayerNum() == 1)  {
            return table.getPile(SlapjackPickupRules.PLAYER2_PILE).getCards().isEmpty();
        }
        return table.getPile(SlapjackPickupRules.PLAYER1_PILE).getCards().isEmpty();
    }
}

