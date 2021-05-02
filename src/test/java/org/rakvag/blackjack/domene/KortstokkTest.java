package org.rakvag.blackjack.domene;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class KortstokkTest {

    @Test
    public void testAtConstructorGir52UnikeKort() {
        Kortstokk kortstokk = new Kortstokk();

        //Siden Set ikke tillatter duplikater, og Kort har implementert equals og hashCode, vil eventuelle duplikater
        // føre til mindre enn 52 kort i settet
        Set<Kort> kortene = new HashSet<>();
        for (int i = 0; i < 52; i++) {
            kortene.add(kortstokk.trekk());
        }
        assertEquals(52, kortene.size());
    }

    @Test
    public void testBlandKortene() {
        Kort førsteKortFørBlandingFørsteForsøk = new Kortstokk().trekk().snuFaceUp();
        Kort førsteKortFørBlandingAndreForsøk = new Kortstokk().trekk().snuFaceUp();
        //Sjekker at første kort alltid er det samme på ny-opprettet kortstokk
        assertEquals(førsteKortFørBlandingFørsteForsøk, førsteKortFørBlandingAndreForsøk);

        //Sjekker at første kort i kortstokken endres når en blander kortene
        Kort førsteKortUtenBlanding = new Kortstokk().trekk().snuFaceUp();
        Kort førsteKortMedBlanding = new Kortstokk().blandKortene().trekk().snuFaceUp();
        if (førsteKortUtenBlanding.equals(førsteKortMedBlanding)) {
            //En av 52 ganger (i snitt) vil første kort etter blanding ikke bli endret.
            //Blander da en gang til, hvis det fortsatt er likt, så konkluderes det med at blandKortene ikke virker
            førsteKortMedBlanding = new Kortstokk().blandKortene().trekk().snuFaceUp();
            if (førsteKortUtenBlanding.equals(førsteKortMedBlanding)) {
                fail("Blanding gjort to ganger, fortsatt er første kort i kortstokken uendret. blandKortene() fungerer ikke.");
            }
        }
    }
}