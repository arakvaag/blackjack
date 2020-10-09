package org.rakvag.blackjack;

import java.util.List;

public class Kort {

    public static String tegnKorteneMedSum(List<Kort> kortene) {
        StringBuilder korteneUtenSum = new StringBuilder();
        kortene.forEach(kort -> korteneUtenSum.append(kort.farge.kode).append("-").append(kort.verdi.kode).append(" "));
        String korteneUtenSumTrimmet = korteneUtenSum.substring(0, korteneUtenSum.length() - 1);

        return "Sum: " + Kort.finnSum(kortene) + " (" + korteneUtenSumTrimmet + ")";
    }

    public static int finnSum(List<Kort> kortene) {
        int sum = kortene.stream().map(it -> it.verdi.tallVerdi).reduce(0, Integer::sum);
        if (sum > 21) {
            int antallEss = (int) kortene.stream().filter(kort -> kort.verdi == Verdi.ESS).count();
            for (int i = 0; i < antallEss; i++) {
                sum -= 10; //Endrer verdien på en stk ESS fra 11 til 1
                if (sum <= 21)
                    break;
            }
        }
        return sum;
    }

    public enum Farge {
        SPAR('S'), HJERTER('H'), RUTER('R'), KLOEVER('K');

        public final char kode;
        Farge(char kode) {
            this.kode = kode;
        }
    }

    public enum Verdi {
        EN("1"), TO("2"), TRE("3"), FIRE("4"), FEM("5"), SEKS("6"), SJU("7"),
        ATTE("8"), NI("9"), TI("10"), KNEKT("J", 10), DAME("Q", 10),
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

    public Kort(Farge farge, Verdi verdi) {
        this.farge = farge;
        this.verdi = verdi;
    }

    public final Farge farge;
    public final Verdi verdi;

    @Override
    public String toString() {
        return farge.name() + "-" + verdi.name();
    }
}
