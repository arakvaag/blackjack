package org.rakvag.blackjack;

import org.rakvag.blackjack.domene.Kortstokk;
import org.springframework.stereotype.Component;

//Poenget med denne klassen/Spring-bønna er å kunne mocke den i tester, slik at en kan returnere mock-kortstokker,
// som da gir støtte for å selv bestemme hvilke kort som returneres i testene
@Component
public class KortstokkProvider {

    public Kortstokk hentNyKortstokk() {
        return new Kortstokk();
    }

}
