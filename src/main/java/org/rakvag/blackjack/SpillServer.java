package org.rakvag.blackjack;

import org.rakvag.blackjack.domene.Spill;
import org.springframework.stereotype.Component;

@Component
public class SpillServer {

    public SpillServer(KortstokkProvider kortstokkProvider) {
        this.kortstokkProvider = kortstokkProvider;
    }

    private final KortstokkProvider kortstokkProvider;
    private Spill spill;

    public Spill startNyttSpill(String spillersNavn) {
        spill = new Spill(spillersNavn, kortstokkProvider.hentNyKortstokk());
        return spill;
    }

    public Spill hentAktivtSpill() {
        if (spill == null) {
            throw new SpillIkkeStartetException();
        }
        return spill;
    }

    public void nullstill() {
        spill = null;
    }

    public static class SpillIkkeStartetException extends RuntimeException {
        public SpillIkkeStartetException() {
            super();
        }
    }
}
