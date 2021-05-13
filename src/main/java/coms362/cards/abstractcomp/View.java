package coms362.cards.abstractcomp;

import coms362.cards.streams.Marshalls;

import java.io.IOException;

public interface View {

    void send(Marshalls event) throws IOException;

    int getCameraPosition();

}
