package org.rakvag.blackjack;

import java.util.Scanner;

public class Blackjack {

    public static void main(String[] args) {
        new Blackjack().kjoerSpill();
    }

    private final Kortstokk kortstokk;
    private final Dealer dealer;
    private final Spiller spiller;

    public Blackjack() {
        kortstokk = new Kortstokk();
        kortstokk.bland();
        dealer = new Dealer();
        dealer.leggTilKort(kortstokk.trekkKort());
        dealer.leggTilKort(kortstokk.trekkKort());
        spiller = new Spiller();
        spiller.leggTilKort(kortstokk.trekkKort());
        spiller.leggTilKort(kortstokk.trekkKort());
    }

    public void kjoerSpill() {
        Scanner scanner = new Scanner(System.in);

        while (!erSpilletFullfoert()) {
            System.out.println("Dealer: " + dealer.tegnKorteneMaskert() + "\n" + "Spiller: " + spiller.tegnKortene());

            if (!spiller.staar() && !spiller.erBust() && !dealer.erBust()) {
                System.out.print("Angi om du vil stå (S) eller få nytt kort (N): ");
                String input = scanner.next();
                if ("N".equals(input.toUpperCase())) {
                    spiller.leggTilKort(kortstokk.trekkKort());
                } else if ("S".equals(input.toUpperCase())) {
                    spiller.velgerAaStaa();
                } else {
                    System.out.println("Ukjent kommando, bruk S eller N.");
                }
            }

            if (!dealer.erBust() && !dealer.maaStaa() && !spiller.erBust()) {
                dealer.leggTilKort(kortstokk.trekkKort());
            }

        }

        printResultat();
    }

    private boolean erSpilletFullfoert() {
        return spiller.harBlackJack() || spiller.erBust() || dealer.erBust() || (spiller.staar() && dealer.maaStaa());
    }

    private void printResultat() {
        System.out.println();

        int sumSpiller = spiller.finnSum();
        int sumDealer = dealer.finnSum();
        if (spiller.erBust() && dealer.erBust()) {
            System.out.println("Begge bust, ingen vant!");
        } else if (dealer.erBust()) {
            System.out.println("Dealer bust, spiller vant!");
        } else if (spiller.erBust()) {
            System.out.println("Spiller bust, huset vant!");
        } else if (sumSpiller == sumDealer) {
            System.out.println("Det ble uavgjort!");
        } else if (sumSpiller > sumDealer) {
            System.out.println("Spilleren vant!");
        } else {
            System.out.println("Huset vant!");
        }
        System.out.println();
        System.out.println("Dealer: " + dealer.tegnAlleKorteneAapentMedSum() + "\n" + "Spiller: " + spiller.tegnKortene());
        System.out.println();
    }

}
