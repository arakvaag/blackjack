package org.rakvag.blackjack.domene;

import java.util.Objects;

public class Kort {

    public enum Farge {
        SPAR('S'), HJERTER('H'), RUTER('R'), KLOEVER('K');

        public final char kode;
        Farge(char kode) {
            this.kode = kode;
        }
    }
    public enum Verdi {
        TO("2"), TRE("3"), FIRE("4"), FEM("5"), SEKS("6"), SJU("7"),
        ÅTTE("8"), NI("9"), TI("10"), KNEKT("J", 10), DAME("Q", 10),
        KONGE("K", 10), ESS("A", 11);

        public final String kode;
        public final int tallVerdi;

        Verdi(String kode, int tallVerdi) {
            this.kode = kode;
            this.tallVerdi = tallVerdi;
        }
        Verdi(String kode) {
            this.kode = kode;
            this.tallVerdi = Integer.parseInt(kode);
        }
    }

    public final Farge farge;
    public final Verdi verdi;
    private boolean faceUp;

    public Kort(Farge farge, Verdi verdi) {
        this.farge = farge;
        this.verdi = verdi;
        this.faceUp = false;
    }

    public Kort snuFaceUp() {
        faceUp = true;
        return this;
    }

    public int getTallverdi() {
        return verdi.tallVerdi;
    }

    @Override
    public String toString() {
        if (!faceUp) {
            return "**";
        } else {
            return farge.kode + verdi.kode;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kort kort = (Kort) o;
        return faceUp == kort.faceUp && farge == kort.farge && verdi == kort.verdi;
    }

    @Override
    public int hashCode() {
        return Objects.hash(farge, verdi, faceUp);
    }
}
