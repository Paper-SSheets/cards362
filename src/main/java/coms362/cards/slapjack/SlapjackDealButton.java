package coms362.cards.slapjack;

import coms362.cards.events.inbound.DealEvent;
import coms362.cards.model.Button;
import coms362.cards.model.Location;

public class SlapjackDealButton extends Button {
    public static final String kSelector = "dealButton";

    public SlapjackDealButton(String label, Location location) {
        super(kSelector, DealEvent.kId, label, location);
    }
}
