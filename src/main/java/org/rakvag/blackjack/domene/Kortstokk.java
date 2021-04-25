package org.rakvag.blackjack.domene;

import java.util.Collections;
import java.util.LinkedList;

public class Kortstokk {

    private final LinkedList<Kort> kort;

    public Kortstokk() {
        kort = new LinkedList<>();
        for(Kort.Farge farge : Kort.Farge.values()) {
            for (Kort.Verdi verdi : Kort.Verdi.values()) {
                kort.add(new Kort(farge, verdi));
            }
        }
    }

    public void blandKortene() {
        Collections.shuffle(kort);
    }

    public Kort trekk() {
        return kort.poll();
    }

}

