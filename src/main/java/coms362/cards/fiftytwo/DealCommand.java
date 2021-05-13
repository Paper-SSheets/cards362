package coms362.cards.fiftytwo;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.*;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class DealCommand implements Move {
    private Table table;

    public DealCommand(Table table, Player player) {
        this.table = table;
    }

    public void apply(Table table) {
        // TODO Auto-generated method stub
    }

    public void apply(ViewFacade views) {

        try {
            String remoteId = views.getRemoteId(DealButton.kSelector);
            views.send(new HideButtonRemote(remoteId));
            Pile local = table.getPile(P52Rules.RANDOM_PILE);
            if (local == null) {
                return;
            }
            if (table.getPile(P52Rules.DISCARD_PILE).getCards().size() == 52)  {
                for (Card c : local.getCards()) {
                    String outVal = "";
                    //views.send(new CreateCardRemote(c));
                    views.send(new UpdateCardRemote(c));
                    table.removeFromPile(P52Rules.DISCARD_PILE, c);
                    System.out.println(outVal);
                }
                table.getPile(P52Rules.DISCARD_PILE).getCards().clear();
                return;
            }
            for (Card c : local.getCards()) {
                String outVal = "";
                views.send(new CreateCardRemote(c));
                views.send(new UpdateCardRemote(c));
                System.out.println(outVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

