package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.*;
import coms362.cards.fiftytwo.DealButton;
import coms362.cards.fiftytwo.P52Rules;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class SlapjackDealCommand implements Move {
    private Table table;

    public SlapjackDealCommand(Table table, Player player) {
//	    		super(table, player);
        this.table = table;
    }

    @Override
    public void apply(Table table) {

    }

    @Override
    public void apply(ViewFacade views) {

        try {
            views.send(new SetGameTitleRemote("Slapjack"));
            String remoteId = views.getRemoteId(DealButton.kSelector);
            views.send(new HideButtonRemote(remoteId));
            Pile player1 = table.getPile(SlapjackPickupRules.PLAYER1_PILE);
            Pile player2 = table.getPile(SlapjackPickupRules.PLAYER2_PILE);
            if (player1 == null || player2 == null) {
                return;
            }
            for (Card c : player1.getCards()) {
                String outVal = "";
                if (table.getPile(SlapjackPickupRules.CENTER_PILE).getCards().size() == 0) {
                    views.send(new CreateCardRemote(c));
                    views.send(new UpdateCardRemote(c));
                }
                views.send(new AddToPileRemote(table.getPile(SlapjackPickupRules.PLAYER1_PILE), c));
                System.out.println(outVal);
            }
            for (Card c : player2.getCards()) {
                String outVal = "";
                if (table.getPile(SlapjackPickupRules.CENTER_PILE).getCards().size() == 0) {
                    views.send(new CreateCardRemote(c));
                    views.send(new UpdateCardRemote(c));
                }
                views.send(new AddToPileRemote(table.getPile(SlapjackPickupRules.PLAYER2_PILE), c));
                System.out.println(outVal);
            }
            if (table.getPile(SlapjackPickupRules.CENTER_PILE).getCards().size() != 0) {  //WILL NEED TO CHANGE IF WINNER!
                for (Player p : table.getPlayers()) {
                    p.addToScore(-1 * p.getScore());
                    p.addToScore(26);
                }
                table.getPile(SlapjackPickupRules.CENTER_PILE).getCards().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}	
