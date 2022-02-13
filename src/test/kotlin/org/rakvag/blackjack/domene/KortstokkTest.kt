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
    fun `kort-klassen har toString som gir to-karakters versjon av kortet`() {
        assertEquals("HA", Kort(Farge.HJERTER, Verdi.ESS).toString())
        assertEquals("H2", Kort(Farge.HJERTER, Verdi.TO).toString())
        assertEquals("H3", Kort(Farge.HJERTER, Verdi.TRE).toString())
        assertEquals("S4", Kort(Farge.SPAR, Verdi.FIRE).toString())
        assertEquals("S5", Kort(Farge.SPAR, Verdi.FEM).toString())
        assertEquals("S6", Kort(Farge.SPAR, Verdi.SEKS).toString())
        assertEquals("R7", Kort(Farge.RUTER, Verdi.SJU).toString())
        assertEquals("R8", Kort(Farge.RUTER, Verdi.ÅTTE).toString())
        assertEquals("R9", Kort(Farge.RUTER, Verdi.NI).toString())
        assertEquals("K10", Kort(Farge.KLØVER, Verdi.TI).toString())
        assertEquals("KJ", Kort(Farge.KLØVER, Verdi.KNEKT).toString())
        assertEquals("KQ", Kort(Farge.KLØVER, Verdi.DAME).toString())
        assertEquals("KK", Kort(Farge.KLØVER, Verdi.KONGE).toString())
    }

    @Test
    fun `blander kortstokken`() {
        val ublandetKortstokkNr1 = KortstokkImpl()
        val korteneFraKortstokkNr1 = mutableListOf<Kort>()
        repeat(52) {
            korteneFraKortstokkNr1.add(ublandetKortstokkNr1.trekkKort())
        }

        val ublandetKortstokkNr2 = KortstokkImpl()
        val korteneFraKortstokkNr2 = mutableListOf<Kort>()
        repeat(52) {
            korteneFraKortstokkNr2.add(ublandetKortstokkNr2.trekkKort())
        }

        //Sjekker at ny-opprettede kortstokker alltid har samme rekkefølge på kortene
        assertEquals(korteneFraKortstokkNr1, korteneFraKortstokkNr2)

        val blandetKortstokk = KortstokkImpl()
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
        val kortstokk = KortstokkImpl()

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