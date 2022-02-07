package org.rakvag.blackjack.domene

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.rakvag.blackjack.domene.Kort.Farge
import org.rakvag.blackjack.domene.Kort.Verdi

class KortstokkTest {

    @Test
    fun `kort-klassen implementerer korrekt equals og hashcode`() {
        assertTrue(Kort(Farge.HJERTER, Verdi.TRE) == Kort(Farge.HJERTER, Verdi.TRE))
        assertTrue(Kort(Farge.HJERTER, Verdi.TRE).hashCode() == Kort(Farge.HJERTER, Verdi.TRE).hashCode())
    }

    @Test
    fun `blander kortstokken`() {
        val ublandetKortstokkNr1 = Kortstokk()
        val korteneFraKortstokkNr1 = mutableListOf<Kort>()
        repeat(52) {
            korteneFraKortstokkNr1.add(ublandetKortstokkNr1.trekkKort())
        }

        val ublandetKortstokkNr2 = Kortstokk()
        val korteneFraKortstokkNr2 = mutableListOf<Kort>()
        repeat(52) {
            korteneFraKortstokkNr2.add(ublandetKortstokkNr2.trekkKort())
        }

        //Sjekker at ny-opprettede kortstokker alltid har samme rekkefølge på kortene
        assertEquals(korteneFraKortstokkNr1, korteneFraKortstokkNr2)

        val blandetKortstokk = Kortstokk()
        blandetKortstokk.blandKortene()
        val korteneFraBlandetKortstokk = mutableListOf<Kort>()
        repeat(52) {
            korteneFraBlandetKortstokk.add(blandetKortstokk.trekkKort())
        }

        //Sjekker at rekkefølgende på kortene i den blandede kortstokken er ulik den i en av de ublandede
        assertNotEquals(korteneFraKortstokkNr1, korteneFraBlandetKortstokk)
    }

    @Test
    fun `ny kortstokk gir 52 forskjellige kort`() {
        val kortstokk = Kortstokk()

        val trekteKort = mutableSetOf<Kort>()
        repeat(52) {
            trekteKort.add(kortstokk.trekkKort())
        }
        //Siden det er brukt Set, som ikke tillater duplikate innslag, så er det tilstrekkelig å sjekke
        // at set-et inneholder 52 kort
        assertEquals(52, trekteKort.size)

        //Kortstokken er tom
        assertThrows<IllegalStateException> {
            kortstokk.trekkKort()
        }
    }
}