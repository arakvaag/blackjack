package org.rakvag.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Spiller {

    private final List<Kort> kortene = new ArrayList<>();
    private boolean staar = false;

    public void leggTilKort(Kort kort) {
        kortene.add(kort);
    }

    public String tegnKortene() {
        return Kort.tegnKorteneMedSum(this.kortene);
    }

    public boolean harBlackJack() {
        return Kort.finnSum(kortene) == 21;
    }

    public void velgerAaStaa() {
        staar = true;
    }

    public boolean staar() {
        return staar;
    }

    public int finnSum() {
        return Kort.finnSum(kortene);
    }

    public boolean erBust() {
        return finnSum() > 21;
    }

    @Override
    public String toString() {
        return "org.rakvag.blackjack.Spiller{" +
                "kortene=" + kortene +
                ", staar=" + staar +
                '}';
    }
}
