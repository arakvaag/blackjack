package org.rakvag.blackjack.domene

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.rakvag.blackjack.domene.Kort.Farge.*
import org.rakvag.blackjack.domene.Kort.Verdi


class RegnUtPoengsummerTest {

    @Test
    fun `to kort, ingen ess eller andre bildekort`() {
        val kortene = listOf(
            Kort(HJERTER, Verdi.TRE),
            Kort(HJERTER, Verdi.FEM)
        )
        assertEquals(listOf(8), regnUtPoengsummer(kortene))
    }

    @Test
    fun `tre kort, ingen ess eller andre bildekort`() {
        val kortene = listOf(
            Kort(HJERTER, Verdi.SJU),
            Kort(RUTER, Verdi.FIRE),
            Kort(SPAR, Verdi.ÅTTE)
        )
        assertEquals(listOf(19), regnUtPoengsummer(kortene))
    }

    @Test
    fun `tre kort, ingen ess, men et annet bildekort`() {
        val kortene = listOf(
            Kort(HJERTER, Verdi.TO),
            Kort(RUTER, Verdi.FIRE),
            Kort(SPAR, Verdi.DAME)
        )
        assertEquals(listOf(16), regnUtPoengsummer(kortene))
    }

    @Test
    fun `tre kort, ingen ess, men alle andre bildekort`() {
        val kortene = listOf(
            Kort(RUTER, Verdi.KNEKT),
            Kort(SPAR, Verdi.DAME),
            Kort(HJERTER, Verdi.KONGE)
        )
        assertEquals(listOf(30), regnUtPoengsummer(kortene))
    }

    @Test
    fun `ett ess`() {
        val kortene = listOf(
            Kort(RUTER, Verdi.TRE),
            Kort(SPAR, Verdi.SEKS),
            Kort(HJERTER, Verdi.ESS)
        )
        assertEquals(listOf(10, 20), regnUtPoengsummer(kortene))
    }

    @Test
    fun `to ess`() {
        val kortene = listOf(
            Kort(RUTER, Verdi.TRE),
            Kort(SPAR, Verdi.ESS),
            Kort(HJERTER, Verdi.ESS)
        )
        assertEquals(listOf(5, 15, 25), regnUtPoengsummer(kortene))
    }

    @Test
    fun `tre ess`() {
        val kortene = listOf(
            Kort(RUTER, Verdi.TRE),
            Kort(SPAR, Verdi.ESS),
            Kort(HJERTER, Verdi.ESS),
            Kort(KLØVER, Verdi.ESS)
        )
        assertEquals(listOf(6, 16, 26, 36), regnUtPoengsummer(kortene))
    }
}