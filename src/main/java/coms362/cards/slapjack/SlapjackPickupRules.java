package coms362.cards.slapjack;


import coms362.cards.abstractcomp.*;
import coms362.cards.events.inbound.*;
import coms362.cards.fiftytwo.*;
import coms362.cards.model.Card;
import coms362.cards.model.Location;
import coms362.cards.model.Pile;

public class SlapjackPickupRules extends P52Rules implements Rules, RulesDispatch {

    private int turn;
    public static final String PLAYER1_PILE = "player1_pile";
    public static final String PLAYER2_PILE = "player2_pile";
    public static final String CENTER_PILE = "center_pile";
    public static final String NEW_PILE = "new_pile";
    public static final String SHUFFLE_PILE = "shuffle_pile";


    public SlapjackPickupRules() {
        super();
        turn = 1;//player 1's turn by default.
    }

    public Move eval(Event nextE, Table table, Player player) {
        return nextE.dispatch(this, table, player);
    }

    private int get_op_player_num(Player p) {
        int op_player_num;
        if (p.getPlayerNum() == 1) {
            op_player_num = 2;
        } else {
            op_player_num = 1;
        }
        return op_player_num;
    }

    private Move clickOnPlayerPiles(CardEvent e, Table t, Player p, Pile toPile) {
        int op_player_num = get_op_player_num(p);
        Pile fromPile;

        if (p.getPlayerNum() == 1) {
            fromPile = t.getPile(PLAYER1_PILE);
        } else {
            fromPile = t.getPile(PLAYER2_PILE);
        }
        /*
         * drop the event if no card.
         */
        Card c = fromPile.getCard(e.getId());
        if (c == null) {
            return new DropEventCmd();
        }


        /*
         * Michael: prob have to modify the playermove class's constrctor.
         */
        /*
         * Michael: not sure if this is right, but trying.
         */
        //opponent player's turn.
        turn = op_player_num;
        t.setTurn(op_player_num);
        //otherwise, if the other player has no score, then it is my turn.
//        if (t.getPlayer(op_player_num).getScore() == 0) {
//            t.setTurn(p.getPlayerNum());
//        }
        /*
         * Michael: we call the new constructor that takes the pile
         * strings so we can use removeFromPile() & addToPile()
         * methods in move class.
         */
        //from the player pile to center pile
        if (p.getPlayerNum() == 1) {
            return new SlapjackPlayerMove(c, p, fromPile, toPile, PLAYER1_PILE, CENTER_PILE);
        } else {
            return new SlapjackPlayerMove(c, p, fromPile, toPile, PLAYER2_PILE, CENTER_PILE);
        }
    }


    private Move clickOnCenterPile(CardEvent e, Table t, Player p, Pile fromPile) {
        int op_player_num = get_op_player_num(p);
        Pile toPile;
        if (p.getPlayerNum() == 1) {
            toPile = t.getPile(PLAYER1_PILE);
        } else {
            toPile = t.getPile(PLAYER2_PILE);
        }

        /*
         * check for jack here and slapping.
         */
        Card c = fromPile.getCard(e.getId());
        if (c.getRank() == 11) {
            if (t.getPlayer(op_player_num).getScore() == 0) {
                //end the game @EVAN
//				return new SlapjackEndPlayMove();
            } else {
                t.setTurn(p.getPlayerNum());
                if (p.getPlayerNum() == 1) {
                    return new SlapjackPlayerMove(c, p, fromPile, toPile, CENTER_PILE, PLAYER1_PILE);
                } else {
                    return new SlapjackPlayerMove(c, p, fromPile, toPile, CENTER_PILE, PLAYER2_PILE);
                }


            }

        } else {
            /*
             * This is where we implement when the player wants to slap a non-jack card
             * by accident, then the other player gets the center pile.
             */
            //t.setTurn(p.getPlayerNum());
            if (p.getPlayerNum() == 1) {
                toPile = t.getPile(PLAYER2_PILE);
                return new SlapjackPlayerMove(c, p, fromPile, toPile, CENTER_PILE, PLAYER2_PILE);
            } else {
                toPile = t.getPile(PLAYER1_PILE);
                return new SlapjackPlayerMove(c, p, fromPile, toPile, CENTER_PILE, PLAYER1_PILE);
            }
        }
        return null;

    }

    public Move apply(CardEvent e, Table table, Player player) {
        Pile centerPile = table.getPile(CENTER_PILE);
        Pile fromPile;
        Pile toPile;
        Card c;

        if (centerPile.getCard(e.getId()) != null) {
            c = centerPile.getCard(e.getId());
            fromPile = centerPile;

            if (c.getRank() == 11) {
                if (player.getPlayerNum() == 1) {
                    toPile = table.getPile(PLAYER1_PILE);
                } else {
                    toPile = table.getPile(PLAYER2_PILE);
                }

            } else {
                if (player.getPlayerNum() == 1) {
                    toPile = table.getPile(PLAYER2_PILE);
                } else {
                    toPile = table.getPile(PLAYER1_PILE);
                }
            }
        } else {
            if (player.getPlayerNum() == 1) {
                fromPile = table.getPile(PLAYER1_PILE);
            } else {
                fromPile = table.getPile(PLAYER2_PILE);
            }
            toPile = table.getPile(CENTER_PILE);
            c = fromPile.getCard(e.getId());
            if (c == null) {
                return new DropEventCmd();
            }
            if(table.getTurn() == get_op_player_num(player) && fromPile != table.getPile(CENTER_PILE)){
                return new DropEventCmd();
            }

            // extra feature
            // move the pile to a random location between turns to prevent hovering
            // updatePileRemote is called in SlapjackPlayerMove apply method
            Location randomLocation = new Location((int) (Math.random() * 200) + 200, (int) (Math.random() * 200) + 200);
            toPile.moveTo(randomLocation);

        }
        table.setTurn(get_op_player_num(player));
        return new SlapjackPlayerMove(c, player, fromPile, toPile);
    }

    public Move apply(DealEvent e, Table table, Player player) {
        return new SlapjackDealCommand(table, player);
    }

    public Move apply(InitGameEvent e, Table table, Player player) {
        return new SlapjackInitCmd(table.getPlayerMap(), "Slapjack", table);
    }

    public Move apply(NewPartyEvent e, Table table, Player player) {
        if (e.getRole() == PartyRole.player) {
            return new CreatePlayerCmd(e.getPosition(), e.getSocketId());
        }
        return new DropEventCmd();
    }

    public Move apply(SetQuorumEvent e, Table table, Player player) {
        return new SetQuorumCmd(e.getQuorum());
    }

    public Move apply(ConnectEvent e, Table table, Player player) {
        Move rval = new DropEventCmd();
        System.out.println("Rules apply ConnectEvent " + e);
        if (!table.getQuorum().exceeds(table.getPlayers().size() + 1)) {
            if (e.getRole() == PartyRole.player) {
                rval = new CreatePlayerCmd(e.getPosition(), e.getSocketId());
            }
        }
        System.out.println("PickupRules connectHandler rval = " + rval);
        return rval;
    }

    public Move apply(GameRestartEvent e, Table table, Player player) {
        return new SlapjackEndPlayMove(table);
    }

    /**
     * We rely on Rules to register the appropriate input events with the
     * unmarshaller. This avoids excessive complexity in the abstract factory and
     * there is a natural dependency between the rules and the game input events.
     */
    private void registerEvents() {
        EventUnmarshallers handlers = EventUnmarshallers.getInstance();
        handlers.registerHandler(InitGameEvent.kId, (Class) InitGameEvent.class);
        handlers.registerHandler(DealEvent.kId, (Class) DealEvent.class);
        handlers.registerHandler(CardEvent.kId, (Class) CardEvent.class);
        handlers.registerHandler(GameRestartEvent.kId, (Class) GameRestartEvent.class);
        handlers.registerHandler(NewPartyEvent.kId, (Class) NewPartyEvent.class);
    }
}
