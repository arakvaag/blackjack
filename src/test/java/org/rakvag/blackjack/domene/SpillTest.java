package org.rakvag.blackjack.domene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rakvag.blackjack.domene.Kort.Farge;
import org.rakvag.blackjack.domene.Kort.Verdi;
import org.rakvag.blackjack.domene.Spill.SpillDTO;
import org.rakvag.blackjack.domene.Spill.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class SpillTest {

    @Mock
    private Kortstokk kortstokk;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void opprettingAvSpill_ToKortTilSpillerOgDealer_SpillersNavnVises_SpillstatusErI_GANG() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TO);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FIRE);
        Kort dealerKort1 = new Kort(Farge.HJERTER, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill-objektet er og skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.I_GANG, spillDTO.status);
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(2, spillDTO.spillerDTO.kort.size());
        assertEquals(2, spillDTO.dealerDTO.kort.size());
    }

    @Test
    public void mensSpilletErIGangSkalDealersKortNrToOgPoengsumVæreSkjult() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TO);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FIRE);
        Kort spillerKort3 = new Kort(Farge.HJERTER, Verdi.SEKS);
        Kort dealerKort1 = new Kort(Farge.HJERTER, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);
        spill.trekkKort();

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill-objektet er og skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.I_GANG, spillDTO.status);
        assertEquals("**", spillDTO.dealerDTO.kort.get(1));
        assertNull(spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void spillerFårBlackjack() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.KNEKT);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.ESS);
        Kort dealerKort1 = new Kort(Farge.HJERTER, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill-objektet er og skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.SPILLER_VANT_MED_BLACKJACK, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(21, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals(dealerKort2.toString(), spillDTO.dealerDTO.kort.get(1));
        assertEquals(8, spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void dealerFårBlackjack() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TRE);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        Kort dealerKort1 = new Kort(Farge.HJERTER, Verdi.KNEKT);
        Kort dealerKort2 = new Kort(Farge.HJERTER, Verdi.ESS);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill-objektet er og skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.DEALER_VANT_MED_BLACKJACK, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(8, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals(dealerKort2.toString(), spillDTO.dealerDTO.kort.get(1));
        assertEquals(21, spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void bådeSpillerOgDealerFårBlackjack() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.KNEKT);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.ESS);
        Kort dealerKort1 = new Kort(Farge.SPAR, Verdi.DAME);
        Kort dealerKort2 = new Kort(Farge.SPAR, Verdi.ESS);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill-objektet er og skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.UAVGJORT, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(21, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals(dealerKort2.toString(), spillDTO.dealerDTO.kort.get(1));
        assertEquals(21, spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void spillerTrekkerKort_BlirIkkeBust() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TI);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        Kort spillerKort3 = new Kort(Farge.HJERTER, Verdi.SEKS);
        Kort dealerKort1 = new Kort(Farge.HJERTER, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);
        spill.trekkKort();

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.I_GANG, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(spillerKort3.toString(), spillDTO.spillerDTO.kort.get(2));
        assertEquals(21, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals("**", spillDTO.dealerDTO.kort.get(1)); //Dealers kort nr 2 skal være skjult så lenge spillet er i gang
        assertNull(spillDTO.dealerDTO.poengsum); //Poengsummen til dealer skal være skjult så lenge spillet er i gang
    }

    @Test
    public void spillerTrekkerKort_BlirBust() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TO);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.TI);
        Kort spillerKort3 = new Kort(Farge.HJERTER, Verdi.DAME);
        Kort dealerKort1 = new Kort(Farge.SPAR, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.SPAR, Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);
        spill.trekkKort();

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.SPILLER_BUST, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(spillerKort3.toString(), spillDTO.spillerDTO.kort.get(2));
        assertEquals(22, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals(dealerKort2.toString(), spillDTO.dealerDTO.kort.get(1));
        assertEquals(8, spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void spillerStårMed21_DealerFår17OgStår() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TI);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        Kort spillerKort3 = new Kort(Farge.HJERTER, Verdi.SEKS);
        Kort dealerKort1 = new Kort(Farge.SPAR, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.SPAR, Verdi.FEM);
        Kort dealerKort3 = new Kort(Farge.SPAR, Verdi.SEKS);
        Kort dealerKort4 = new Kort(Farge.RUTER, Verdi.TRE);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3)
                .thenReturn(dealerKort3)
                .thenReturn(dealerKort4);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);
        spill.trekkKort();
        spill.stå();

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.SPILLER_VANT_PÅ_POENG
                , spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(spillerKort3.toString(), spillDTO.spillerDTO.kort.get(2));
        assertEquals(21, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals("S3", spillDTO.dealerDTO.kort.get(0));
        assertEquals("S5", spillDTO.dealerDTO.kort.get(1));
        assertEquals("S6", spillDTO.dealerDTO.kort.get(2));
        assertEquals("R3", spillDTO.dealerDTO.kort.get(3));
        assertEquals(17, spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void spillerFår21OgStår_DealerFår16TrekkerEtTilOgBlirBust() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TI);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        Kort spillerKort3 = new Kort(Farge.HJERTER, Verdi.SEKS);
        Kort dealerKort1 = new Kort(Farge.SPAR, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.SPAR, Verdi.FIRE);
        Kort dealerKort3 = new Kort(Farge.SPAR, Verdi.NI);
        Kort dealerKort4 = new Kort(Farge.SPAR, Verdi.SEKS);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3)
                .thenReturn(dealerKort3)
                .thenReturn(dealerKort4);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);
        spill.trekkKort();
        spill.stå();

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.DEALER_BUST, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(spillerKort3.toString(), spillDTO.spillerDTO.kort.get(2));
        assertEquals(21, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals(dealerKort2.toString(), spillDTO.dealerDTO.kort.get(1));
        assertEquals(dealerKort3.toString(), spillDTO.dealerDTO.kort.get(2));
        assertEquals(dealerKort4.toString(), spillDTO.dealerDTO.kort.get(3));
        assertEquals(22, spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void spillerStårPå15_DealerFår16TrekkerLikevelEttKortTil() { //Dealer har ikke lov til å ta hensyn til spillers poengsum, kun om spiller er bust eller ikke
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TI);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        Kort dealerKort1 = new Kort(Farge.SPAR, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.SPAR, Verdi.FIRE);
        Kort dealerKort3 = new Kort(Farge.SPAR, Verdi.NI);
        Kort dealerKort4 = new Kort(Farge.SPAR, Verdi.SEKS);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(dealerKort3)
                .thenReturn(dealerKort4);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);
        spill.stå();

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.DEALER_BUST, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(15, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals(dealerKort2.toString(), spillDTO.dealerDTO.kort.get(1));
        assertEquals(dealerKort3.toString(), spillDTO.dealerDTO.kort.get(2));
        assertEquals(dealerKort4.toString(), spillDTO.dealerDTO.kort.get(3));
        assertEquals(22, spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void bådeSpillerOgDealerStårPå18() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TI);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        Kort spillerKort3 = new Kort(Farge.HJERTER, Verdi.TRE);
        Kort dealerKort1 = new Kort(Farge.SPAR, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.SPAR, Verdi.FIRE);
        Kort dealerKort3 = new Kort(Farge.SPAR, Verdi.NI);
        Kort dealerKort4 = new Kort(Farge.SPAR, Verdi.TO);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3)
                .thenReturn(dealerKort3)
                .thenReturn(dealerKort4);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);
        spill.trekkKort();
        spill.stå();

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.UAVGJORT, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(18, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals(dealerKort2.toString(), spillDTO.dealerDTO.kort.get(1));
        assertEquals(dealerKort3.toString(), spillDTO.dealerDTO.kort.get(2));
        assertEquals(dealerKort4.toString(), spillDTO.dealerDTO.kort.get(3));
        assertEquals(18, spillDTO.dealerDTO.poengsum);
    }

    @Test
    public void spillerStårPå18_DealerStårPå21() {
        String spillersNavn = "Ole";

        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);

        Kort spillerKort1 = new Kort(Farge.HJERTER, Verdi.TI);
        Kort spillerKort2 = new Kort(Farge.HJERTER, Verdi.FEM);
        Kort spillerKort3 = new Kort(Farge.HJERTER, Verdi.TRE);
        Kort dealerKort1 = new Kort(Farge.SPAR, Verdi.TRE);
        Kort dealerKort2 = new Kort(Farge.SPAR, Verdi.FIRE);
        Kort dealerKort3 = new Kort(Farge.SPAR, Verdi.NI);
        Kort dealerKort4 = new Kort(Farge.SPAR, Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3)
                .thenReturn(dealerKort3)
                .thenReturn(dealerKort4);

        //ACT
        Spill spill = new Spill(spillersNavn, kortstokk);
        spill.trekkKort();
        spill.stå();

        //ASSERT
        //Verifikasjon av tilstand skjer gjennom DTO-en - state i selve Spill skal være private
        SpillDTO spillDTO = spill.lagDTO();
        assertEquals(Status.DEALER_VANT_PÅ_POENG, spillDTO.status);

        //Spillers tilstand
        assertEquals(spillersNavn, spillDTO.spillerDTO.navn);
        assertEquals(spillerKort1.toString(), spillDTO.spillerDTO.kort.get(0));
        assertEquals(spillerKort2.toString(), spillDTO.spillerDTO.kort.get(1));
        assertEquals(18, spillDTO.spillerDTO.poengsum);

        //Dealers tilstand
        assertEquals(dealerKort1.toString(), spillDTO.dealerDTO.kort.get(0));
        assertEquals(dealerKort2.toString(), spillDTO.dealerDTO.kort.get(1));
        assertEquals(dealerKort3.toString(), spillDTO.dealerDTO.kort.get(2));
        assertEquals(dealerKort4.toString(), spillDTO.dealerDTO.kort.get(3));
        assertEquals(21, spillDTO.dealerDTO.poengsum);
    }
}
