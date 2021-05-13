package coms362.cards;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Rules;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.PlayController;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.inbound.DealEvent;
import coms362.cards.events.inbound.EndPlay;
import coms362.cards.fiftytwo.*;
import coms362.cards.model.TableBase;
import coms362.cards.streams.InBoundQueue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

import static org.junit.Assert.assertEquals;

public class TestDealInput {

    static final long expectedSig = 3201860157L;

    @Test
    public void test() {
        //set up game and match resources to provision play loop
        InBoundQueue inQ = new InBoundQueue();
        //pre-load the input stream with the input for this test
        inQ.add(new DealEvent());
        inQ.add(new EndPlay()); //artifice to stop the test

        ViewFacade views = new ViewFacade(null);
        //we keep a reference to the concrete type for later
        LoggingView p1View = new LoggingView();
        views.add(p1View);

        Map<Integer, Player> players = new HashMap<Integer, Player>();
        Player player = new P52Player(1);
        players.put((Integer) 1, player);
        players.put((Integer) 2, new P52Player(2));

        // initialize the local model for Pu52 match
        Table table = new TableBase(new P52MPGameFactory());
        Move move = new P52InitCmd(players, "", table);
        move.apply(table);
        Rules rules = new P52Rules();

        PlayController mainloop = new PlayController(inQ, rules);
        mainloop.play(table, player, views);


        CRC32 sig = new CRC32();
        String log = p1View.getLog();
        System.out.println(log);
        sig.update(log.getBytes());
        long sValue = sig.getValue();
        System.out.println(sValue);

        assertEquals(expectedSig, sValue);

    }

}
