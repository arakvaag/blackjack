package org.rakvag.blackjack.domene;

import org.junit.jupiter.api.Test;
import org.rakvag.blackjack.domene.Kort.Farge;
import org.rakvag.blackjack.domene.Kort.Verdi;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KortTest {

    @Test
    public void testTallverdier() {
        assertEquals(2, new Kort(Farge.HJERTER, Verdi.TO).getTallverdi());
        assertEquals(3, new Kort(Farge.HJERTER, Verdi.TRE).getTallverdi());
        assertEquals(4, new Kort(Farge.HJERTER, Verdi.FIRE).getTallverdi());
        assertEquals(5, new Kort(Farge.HJERTER, Verdi.FEM).getTallverdi());
        assertEquals(6, new Kort(Farge.HJERTER, Verdi.SEKS).getTallverdi());
        assertEquals(7, new Kort(Farge.HJERTER, Verdi.SJU).getTallverdi());
        assertEquals(8, new Kort(Farge.HJERTER, Verdi.ÅTTE).getTallverdi());
        assertEquals(9, new Kort(Farge.HJERTER, Verdi.NI).getTallverdi());
        assertEquals(10, new Kort(Farge.HJERTER, Verdi.TI).getTallverdi());
        assertEquals(10, new Kort(Farge.HJERTER, Verdi.KNEKT).getTallverdi());
        assertEquals(10, new Kort(Farge.HJERTER, Verdi.DAME).getTallverdi());
        assertEquals(10, new Kort(Farge.HJERTER, Verdi.KONGE).getTallverdi());
        assertEquals(11, new Kort(Farge.HJERTER, Verdi.ESS).getTallverdi());
    }

    @Test
    public void testSnuFaceUp() {
        //Er face down når kortet lages, og face down gir tekstverdi **
        assertEquals("**", new Kort(Farge.HJERTER, Verdi.TO).toString());

        //Face up gir tekstverdi som forteller farge og verdi
        assertEquals("H2", new Kort(Farge.HJERTER, Verdi.TO).snuFaceUp().toString());

        //Om du kaller snuFaceUp flere ganger, går det bra og kortet er fortsatt face up
        assertEquals("H2", new Kort(Farge.HJERTER, Verdi.TO).snuFaceUp().snuFaceUp().toString());
    }



}