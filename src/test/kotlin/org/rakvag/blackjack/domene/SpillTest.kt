package org.rakvag.blackjack.domene

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.rakvag.blackjack.Provider
import org.rakvag.blackjack.domene.Kort.Farge.HJERTER
import org.rakvag.blackjack.domene.Kort.Farge.SPAR
import org.rakvag.blackjack.domene.Kort.Verdi.*

class SpillTest {

    private val provider = mockk<Provider>()
    private var sistTildelteId = 0
    private val kortstokk = mockk<Kortstokk>()

    @BeforeEach
    fun setup() {
        every { provider.hentNyKortstokk() } returns kortstokk
        every { provider.hentNySpillId() } returns ++sistTildelteId
        every { kortstokk.blandKortene() } returns Unit
    }

    @Test
    fun `oppretter nytt spill - ingen blackjack`() {
        //ARRANGE
        val spillersNavn = "Testesen"
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(HJERTER, TO),
            Kort(HJERTER, TRE),
            Kort(HJERTER, FIRE),
            Kort(HJERTER, FEM)
        )

        //ACT
        val spill = Spill(spillersNavn, provider)

        //ASSERT
        assertEquals(Spill.Status.I_GANG, spill.status)

        assertEquals(spillersNavn, spill.spillersNavn)
        assertEquals(listOf(Kort(HJERTER, TO), Kort(HJERTER, FIRE)), spill.spillersKort)
        assertEquals(listOf(6), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, TRE)), spill.dealersÅpneKort)
        assertEquals(2, spill.antallKortHosDealer)
        assertThrows<IllegalStateException> {
            spill.hentDealersPoengsummer()
        }
    }

    @Test
    fun `oppretter nytt spill - spiller får blackjack`() {
        //ARRANGE
        val spillersNavn = "Testesen"
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(HJERTER, ESS),
            Kort(HJERTER, TRE),
            Kort(HJERTER, KONGE),
            Kort(HJERTER, FEM)
        )

        //ACT
        val spill = Spill(spillersNavn, provider)

        //ASSERT
        assertEquals(Spill.Status.SPILLER_VANT_MED_BLACKJACK, spill.status)

        assertEquals(spillersNavn, spill.spillersNavn)
        assertEquals(listOf(Kort(HJERTER, ESS), Kort(HJERTER, KONGE)), spill.spillersKort)
        assertEquals(listOf(11, 21), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, TRE), Kort(HJERTER, FEM)), spill.dealersÅpneKort)
        assertEquals(2, spill.antallKortHosDealer)
        assertEquals(listOf(8), spill.hentDealersPoengsummer())
    }

    @Test
    fun `oppretter nytt spill - dealer får blackjack`() {
        //ARRANGE
        val spillersNavn = "Testesen"
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(HJERTER, TRE),
            Kort(HJERTER, ESS),
            Kort(HJERTER, FIRE),
            Kort(HJERTER, KONGE)
        )

        //ACT
        val spill = Spill(spillersNavn, provider)

        //ASSERT
        assertEquals(Spill.Status.DEALER_VANT_MED_BLACKJACK, spill.status)

        assertEquals(spillersNavn, spill.spillersNavn)
        assertEquals(listOf(Kort(HJERTER, TRE), Kort(HJERTER, FIRE)), spill.spillersKort)
        assertEquals(listOf(7), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, ESS), Kort(HJERTER, KONGE)), spill.dealersÅpneKort)
        assertEquals(2, spill.antallKortHosDealer)
        assertEquals(listOf(11, 21), spill.hentDealersPoengsummer())
    }

    @Test
    fun `oppretter nytt spill - begge får blackjack`() {
        //ARRANGE
        val spillersNavn = "Testesen"
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(SPAR, ESS),
            Kort(HJERTER, ESS),
            Kort(SPAR, KONGE),
            Kort(HJERTER, KONGE)
        )

        //ACT
        val spill = Spill(spillersNavn, provider)

        //ASSERT
        assertEquals(Spill.Status.PUSH, spill.status)

        assertEquals(spillersNavn, spill.spillersNavn)
        assertEquals(listOf(Kort(SPAR, ESS), Kort(SPAR, KONGE)), spill.spillersKort)
        assertEquals(listOf(11, 21), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, ESS), Kort(HJERTER, KONGE)), spill.dealersÅpneKort)
        assertEquals(2, spill.antallKortHosDealer)
        assertEquals(listOf(11, 21), spill.hentDealersPoengsummer())
    }

    @Test
    fun `spiller trekker kort - blir ikke bust`() {
        //ARRANGE
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(HJERTER, TRE),
            Kort(HJERTER, FIRE),
            Kort(HJERTER, FEM),
            Kort(HJERTER, SEKS),
            Kort(HJERTER, SJU)
        )
        val spill = Spill("Testesen", provider)

        //ACT
        spill.spillerTrekkerKort()

        //ASSERT
        assertEquals(Spill.Status.I_GANG, spill.status)

        assertEquals(listOf(Kort(HJERTER, TRE), Kort(HJERTER, FEM), Kort(HJERTER, SJU)), spill.spillersKort)
        assertEquals(listOf(15), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, FIRE)), spill.dealersÅpneKort)
        assertEquals(2, spill.antallKortHosDealer)
        assertThrows<IllegalStateException> { spill.hentDealersPoengsummer() }
    }

    @Test
    fun `spiller trekker kort - blir bust med 22`() {
        //ARRANGE
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(SPAR, FEM),
            Kort(HJERTER, FIRE),
            Kort(SPAR, TI),
            Kort(HJERTER, SEKS),
            Kort(SPAR, SJU)
        )
        val spill = Spill("Testesen", provider)

        //ACT
        spill.spillerTrekkerKort()

        //ASSERT
        assertEquals(Spill.Status.SPILLER_BUST, spill.status)

        assertEquals(listOf(Kort(SPAR, FEM), Kort(SPAR, TI), Kort(SPAR, SJU)), spill.spillersKort)
        assertEquals(listOf(22), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, FIRE), Kort(HJERTER, SEKS)), spill.dealersÅpneKort)
        assertEquals(2, spill.antallKortHosDealer)
        assertEquals(listOf(10), spill.hentDealersPoengsummer())
    }

    @Test
    fun `dealer må stå på 17 eller mer`() {
        //ARRANGE
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(SPAR, KONGE),
            Kort(HJERTER, FIRE),
            Kort(SPAR, ÅTTE),
            Kort(HJERTER, SEKS),
            Kort(HJERTER, SJU)
        )
        val spill = Spill("Testesen", provider)

        //ACT
        spill.spillerStår()

        //ASSERT
        assertEquals(Spill.Status.SPILLER_VANT_PÅ_POENG, spill.status)

        assertEquals(listOf(Kort(SPAR, KONGE), Kort(SPAR, ÅTTE)), spill.spillersKort)
        assertEquals(listOf(18), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, FIRE), Kort(HJERTER, SEKS), Kort(HJERTER, SJU)), spill.dealersÅpneKort)
        assertEquals(3, spill.antallKortHosDealer)
        assertEquals(listOf(17), spill.hentDealersPoengsummer())
    }

    @Test
    fun `spiller står, dealer får 22 og blir bust`() {
        //ARRANGE
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(SPAR, KONGE),
            Kort(HJERTER, FEM),
            Kort(SPAR, ÅTTE),
            Kort(HJERTER, SJU),
            Kort(HJERTER, KONGE)
        )
        val spill = Spill("Testesen", provider)

        //ACT
        spill.spillerStår()

        //ASSERT
        assertEquals(Spill.Status.DEALER_BUST, spill.status)

        assertEquals(listOf(Kort(SPAR, KONGE), Kort(SPAR, ÅTTE)), spill.spillersKort)
        assertEquals(listOf(18), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, FEM), Kort(HJERTER, SJU), Kort(HJERTER, KONGE)), spill.dealersÅpneKort)
        assertEquals(3, spill.antallKortHosDealer)
        assertEquals(listOf(22), spill.hentDealersPoengsummer())
    }

    @Test
    fun `spiller står, dealer står, men med høyere poengsum`() {
        //ARRANGE
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(SPAR, TRE),
            Kort(HJERTER, FEM),
            Kort(SPAR, SJU),
            Kort(HJERTER, SEKS),
            Kort(SPAR, NI),
            Kort(HJERTER, NI)
        )
        val spill = Spill("Testesen", provider)

        //ACT
        spill.spillerTrekkerKort()
        spill.spillerStår()

        //ASSERT
        assertEquals(Spill.Status.DEALER_VANT_PÅ_POENG, spill.status)

        assertEquals(listOf(Kort(SPAR, TRE), Kort(SPAR, SJU), Kort(SPAR, NI)), spill.spillersKort)
        assertEquals(listOf(19), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, FEM), Kort(HJERTER, SEKS), Kort(HJERTER, NI)), spill.dealersÅpneKort)
        assertEquals(3, spill.antallKortHosDealer)
        assertEquals(listOf(20), spill.hentDealersPoengsummer())
    }

    @Test
    fun `spiller står, dealer står, spiller har høyere poengsum`() {
        //ARRANGE
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(SPAR, TRE),
            Kort(HJERTER, FEM),
            Kort(SPAR, SJU),
            Kort(HJERTER, SEKS),
            Kort(SPAR, KNEKT),
            Kort(HJERTER, ÅTTE)
        )
        val spill = Spill("Testesen", provider)

        //ACT
        spill.spillerTrekkerKort()
        spill.spillerStår()

        //ASSERT
        assertEquals(Spill.Status.SPILLER_VANT_PÅ_POENG, spill.status)

        assertEquals(listOf(Kort(SPAR, TRE), Kort(SPAR, SJU), Kort(SPAR, KNEKT)), spill.spillersKort)
        assertEquals(listOf(20), spill.spillersPoengsummer)

        assertEquals(listOf(Kort(HJERTER, FEM), Kort(HJERTER, SEKS), Kort(HJERTER, ÅTTE)), spill.dealersÅpneKort)
        assertEquals(3, spill.antallKortHosDealer)
        assertEquals(listOf(19), spill.hentDealersPoengsummer())
    }

    @Test
    fun `prøver å trekke kort etter spillet er ferdig`() {
        //ARRANGE
        //Spiller får blackjack
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(SPAR, ESS),
            Kort(HJERTER, FEM),
            Kort(SPAR, KONGE),
            Kort(HJERTER, SEKS),
        )
        val spill = Spill("Testesen", provider)

        //ACT
        assertThrows<IllegalStateException> {
            spill.spillerTrekkerKort()
        }
    }

    @Test
    fun `prøver å stå etter spillet er ferdig`() {
        //ARRANGE
        //Spiller får blackjack
        every { kortstokk.trekkKort() }.returnsMany(
            Kort(SPAR, ESS),
            Kort(HJERTER, FEM),
            Kort(SPAR, KONGE),
            Kort(HJERTER, SEKS),
        )
        val spill = Spill("Testesen", provider)

        //ACT
        assertThrows<IllegalStateException> {
            spill.spillerStår()
        }
    }
}