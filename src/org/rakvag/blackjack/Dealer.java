package org.rakvag.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Dealer {

    private final List<Kort> kortene = new ArrayList<>();

    public void leggTilKort(Kort kort) {
        kortene.add(kort);
    }

    public String tegnKorteneMaskert() {
        Kort foersteKort = kortene.get(0);
        StringBuilder output = new StringBuilder(" Sum: ?? (" + foersteKort.farge.kode + "-" + foersteKort.verdi.kode + " ");
        for (int i = 1; i < kortene.size(); i++) {
            output.append("?-? ");
        }
        return output.substring(0, output.length() - 1) + ")";
    }

    public String tegnAlleKorteneAapentMedSum() {
        return Kort.tegnKorteneMedSum(this.kortene);
    }

    public boolean maaStaa() {
        return Kort.finnSum(this.kortene) >= 17;
    }

    public int finnSum() {
        return Kort.finnSum(kortene);
    }

    public boolean erBust() {
        return finnSum() > 21;
    }

    @Override
    public String toString() {
        return "org.rakvag.blackjack.Dealer{" +
                "kortene=" + kortene +
                '}';
    }
}
