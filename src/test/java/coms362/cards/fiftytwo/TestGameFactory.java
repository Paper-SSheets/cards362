package coms362.cards.fiftytwo;

import coms362.cards.abstractcomp.*;
import coms362.cards.model.PlayerFactory;
import coms362.cards.streams.RemoteTableGateway;

import java.util.ArrayList;
import java.util.List;

public class TestGameFactory implements GameFactory {

    public Table activeTable = null;
    public List<View> activeViews = new ArrayList<View>();
    private GameFactory proxied = null;

    public TestGameFactory(GameFactory proxied) {
        this.proxied = proxied;
        activeViews.add(new LoggingView());
    }

    @Override
    public Rules createRules() {
        return proxied.createRules();
    }

    @Override
    public Table createTable() {
        activeTable = proxied.createTable();
        return activeTable;
    }

    @Override
    public Player createPlayer(Integer position, String socketId) {
        return proxied.createPlayer(position, socketId);
    }

    @Override
    public View createView(PartyRole role, Integer num, String socketId, RemoteTableGateway gw) {
        View rval = proxied.createView(role, num, socketId, gw);
        activeViews.add(rval);
        return rval;
    }

    @Override
    public PlayerFactory createPlayerFactory() {
        return (PlayerFactory) this;
    }

}
