package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Rules;
import coms362.cards.abstractcomp.Table;
import coms362.cards.fiftytwo.P52MPGameFactory;
import coms362.cards.model.TableBase;

public class SlapjackGameFactory extends P52MPGameFactory {

    @Override
    public Rules createRules() {
        return new SlapjackPickupRules();
    }

    @Override
    public Table createTable() {
        return new TableBase(this);
    }
}
