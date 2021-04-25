package org.rakvag.blackjack;

import org.rakvag.blackjack.domene.Spill;
import org.springframework.stereotype.Component;

@Component
public class SpillServer {

    private Spill spill;

    public Spill startNyttSpill(String spillersNavn) {
        spill = new Spill(spillersNavn);
        return spill;
    }

    public Spill hentAktivtSpill() {
        if (spill == null) {
            throw new SpillIkkeStartetException();
        }
        return spill;
    }

    public static class SpillIkkeStartetException extends RuntimeException {
        public SpillIkkeStartetException() {
            super();
        }
    }
}
