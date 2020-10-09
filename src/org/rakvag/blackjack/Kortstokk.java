package org.rakvag.blackjack;

import java.util.Collections;
import java.util.LinkedList;

public class Kortstokk {

    private final LinkedList<Kort> kortene;

    public Kortstokk() {
        kortene = new LinkedList<>();
        for(Kort.Farge farge : Kort.Farge.values()) {
            for (Kort.Verdi verdi : Kort.Verdi.values()) {
                kortene.add(new Kort(farge, verdi));
            }
        }
    }

    public void bland() {
        Collections.shuffle(kortene);
    }

    public Kort trekkKort() {
        return kortene.poll();
    }
}
