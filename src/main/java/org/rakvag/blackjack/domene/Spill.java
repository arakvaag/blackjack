package org.rakvag.blackjack.domene;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Spill {

    public enum Status { I_GANG, DEALER_BUST, SPILLER_BUST, SPILLER_VANT_PAA_POENG, DEALER_VANT_PAA_POENG, SPILLER_VANT_MED_BLACKJACK, PUSH}

    private final Kortstokk kortstokk;
    private final String spillersNavn;
    private final List<Kort> spillersKort;
    private final List<Kort> dealersKort;
    private Status status;

    public Spill(String spillersNavn) {
        this.spillersNavn = spillersNavn;

        this.kortstokk = new Kortstokk();
        kortstokk.blandKortene();

        this.spillersKort = new ArrayList<>();
        this.spillersKort.add(kortstokk.trekk().snuFaceUp());
        this.spillersKort.add(kortstokk.trekk().snuFaceUp());

        this.dealersKort = new ArrayList<>();
        this.dealersKort.add(kortstokk.trekk().snuFaceUp());
        this.dealersKort.add(kortstokk.trekk());

        //HVIS spillers poengsum allerede her er 21
        // OG dealeren ikke også har fått 21 allerede,
        // SÅ har spilleren fått BLACKJACK
        if (regnutPoengsum(spillersKort) == 21 && regnutPoengsum(dealersKort) != 21) {
            this.status = Status.SPILLER_VANT_MED_BLACKJACK;
        } else {
            this.status = Status.I_GANG;
        }
    }

    public Spill trekkKort() {
        if (status != Status.I_GANG) {
            throw new SpillerErFullførtException();
        }

        spillersKort.add(kortstokk.trekk().snuFaceUp());
        if (regnutPoengsum(spillersKort) > 21) {
            status = Status.SPILLER_BUST;
            dealersKort.forEach(Kort::snuFaceUp);
        }

        return this;
    }

    public Spill stå() {
        if (status != Status.I_GANG) {
            throw new SpillerErFullførtException();
        }

        fullførDealersSpill();

        int dealersPoengsum = regnutPoengsum(dealersKort);
        int spillersPoengsum = regnutPoengsum(spillersKort);

        if (dealersPoengsum > 21) {
            status = Status.DEALER_BUST;
        } else if (spillersPoengsum == dealersPoengsum) {
            status = Status.PUSH;
        } else if (spillersPoengsum > dealersPoengsum) {
            status = Status.SPILLER_VANT_PAA_POENG;
        } else {
            status = Status.DEALER_VANT_PAA_POENG;
        }

        dealersKort.forEach(Kort::snuFaceUp);

        return this;
    }

    private void fullførDealersSpill() {
        dealersKort.forEach(Kort::snuFaceUp);
        while(regnutPoengsum(dealersKort) < 17) {
            dealersKort.add(kortstokk.trekk().snuFaceUp());
        }
    }

    private int regnutPoengsum(List<Kort> kortene) {
        int poengsum = kortene.stream().mapToInt(Kort::getTallverdi).reduce(0, Integer::sum);
        if (poengsum > 21) {
            int antallEss = (int) kortene.stream().filter(kort -> kort.verdi == Kort.Verdi.ESS).count();
            for (int i = antallEss; i > 0; i--) {
                poengsum -= 10; //Endrer verdien på ett ess fra 11 til 1 (trekker fra 10 poeng)
                if (poengsum <= 21) {
                    break;
                }
            }
        }
        return poengsum;
    }

    public static class SpillDTO {
        public final SpillerDTO spillerDTO;
        public final DealerDTO dealerDTO;
        public final String status;
        public SpillDTO(Spill spill) {
            this.spillerDTO = new SpillerDTO(spill);
            this.dealerDTO = new DealerDTO(spill);
            this.status = spill.status.name();
        }
    }
    public static class SpillerDTO {
        public final String navn;
        public final List<String> kort;
        public final String poengsum;

        public SpillerDTO(Spill spill) {
            this.navn = spill.spillersNavn;
            this.kort = spill.spillersKort.stream().map(Kort::toString).collect(Collectors.toList());
            this.poengsum = String.valueOf(spill.regnutPoengsum(spill.spillersKort));
        }
    }
    public static class DealerDTO {
        public final List<String> kort;
        public final String poengsum;

        public DealerDTO(Spill spill) {
            this.kort = spill.dealersKort.stream().map(Kort::toString).collect(Collectors.toList());
            if (spill.status == Status.I_GANG) {
                this.poengsum = null;
            } else {
                this.poengsum = String.valueOf(spill.regnutPoengsum(spill.dealersKort));
            }
        }
    }

    public static class SpillerErFullførtException extends RuntimeException {
        public SpillerErFullførtException() {
            super();
        }
    }
}
