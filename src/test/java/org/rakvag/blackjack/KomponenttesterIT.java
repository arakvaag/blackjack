package org.rakvag.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rakvag.blackjack.domene.Kort;
import org.rakvag.blackjack.domene.Kortstokk;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class KomponenttesterIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SpillServer spillServer;

    @MockBean
    private KortstokkProvider kortstokkProvider;

    @Mock
    private Kortstokk kortstokk;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(kortstokkProvider.hentNyKortstokk()).thenReturn(kortstokk);
        spillServer.nullstill();
    }

    @Test
    public void startSpill() throws Exception {
        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);
        Kort spillerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TI);
        Kort spillerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        Kort dealerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TRE);
        Kort dealerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2);

        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill")
                        .content("""
                                { "spillersNavn": "Ole" }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(200, result.getResponse().getStatus());
        JSONAssert.assertEquals("""
                    {
                      "spillerDTO": {
                        "navn": "Ole",
                        "kort": [
                          "H10",
                          "H5"
                        ],
                        "poengsum": 15
                      },
                      "dealerDTO": {
                        "kort": [
                          "H3",
                          "**"
                        ],
                        "poengsum": null
                      },
                      "status": "I_GANG"
                    }
                """, result.getResponse().getContentAsString(StandardCharsets.UTF_8), JSONCompareMode.LENIENT);
    }

    @Test
    public void trekkKort() throws Exception {
        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);
        Kort spillerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TI);
        Kort spillerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        Kort spillerKort3 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TO);
        Kort dealerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TRE);
        Kort dealerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3);

        //Starter spillet
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill")
                        .content("""
                                { "spillersNavn": "Ole" }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill/trekkKort")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(200, result.getResponse().getStatus());
        JSONAssert.assertEquals("""
                    {
                      "spillerDTO": {
                        "navn": "Ole",
                        "kort": [
                          "H10",
                          "H5",
                          "H2"
                        ],
                        "poengsum": 17
                      },
                      "dealerDTO": {
                        "kort": [
                          "H3",
                          "**"
                        ],
                        "poengsum": null
                      },
                      "status": "I_GANG"
                    }
                """, result.getResponse().getContentAsString(StandardCharsets.UTF_8), JSONCompareMode.LENIENT);
    }

    @Test
    public void trekkKort_SpillIkkeStartet() throws Exception {
        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill/trekkKort")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void trekkKort_SpillFullført() throws Exception {
        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);
        Kort spillerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TI);
        Kort spillerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.ESS);
        Kort dealerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TRE);
        Kort dealerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2);

        //ACT
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill")
                        .content("""
                                { "spillersNavn": "Ole" }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill/trekkKort")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void stå() throws Exception {
        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);
        Kort spillerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TI);
        Kort spillerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        Kort spillerKort3 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.SEKS);
        Kort dealerKort1 = new Kort(Kort.Farge.SPAR, Kort.Verdi.KNEKT);
        Kort dealerKort2 = new Kort(Kort.Farge.SPAR, Kort.Verdi.NI);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2)
                .thenReturn(spillerKort3);

        //Starter spillet
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill")
                        .content("""
                                { "spillersNavn": "Ole" }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        //Trekker ett kort
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill/trekkKort")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill/staa")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(200, result.getResponse().getStatus());
        JSONAssert.assertEquals("""
                    {
                      "spillerDTO": {
                        "navn": "Ole",
                        "kort": [
                          "H10",
                          "H5",
                          "H6"
                        ],
                        "poengsum": 21
                      },
                      "dealerDTO": {
                        "kort": [
                          "SJ",
                          "S9"
                        ],
                        "poengsum": 19
                      },
                      "status": "SPILLER_VANT_PÅ_POENG"
                    }
                """, result.getResponse().getContentAsString(StandardCharsets.UTF_8), JSONCompareMode.LENIENT);
    }

    @Test
    public void stå_SpillIkkeStartet() throws Exception {
        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill/staa")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void stå_SpillFullført() throws Exception {
        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);
        Kort spillerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TI);
        Kort spillerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.ESS);
        Kort dealerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TRE);
        Kort dealerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill")
                        .content("""
                                { "spillersNavn": "Ole" }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill/staa")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void hentSpill() throws Exception {
        //ARRANGE
        when(kortstokk.blandKortene()).thenReturn(kortstokk);
        Kort spillerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TI);
        Kort spillerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        Kort dealerKort1 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.TRE);
        Kort dealerKort2 = new Kort(Kort.Farge.HJERTER, Kort.Verdi.FEM);
        when(kortstokk.trekk())
                .thenReturn(spillerKort1)
                .thenReturn(dealerKort1)
                .thenReturn(spillerKort2)
                .thenReturn(dealerKort2);

        //Starter spillet
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/spill")
                        .content("""
                                { "spillersNavn": "Ole" }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/spill")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(200, result.getResponse().getStatus());
        JSONAssert.assertEquals("""
                    {
                      "spillerDTO": {
                        "navn": "Ole",
                        "kort": [
                          "H10",
                          "H5"
                        ],
                        "poengsum": 15
                      },
                      "dealerDTO": {
                        "kort": [
                          "H3",
                          "**"
                        ],
                        "poengsum": null
                      },
                      "status": "I_GANG"
                    }
                """, result.getResponse().getContentAsString(StandardCharsets.UTF_8), JSONCompareMode.LENIENT);
    }

    @Test
    public void hentSpill_SpillIkkeStartet() throws Exception {
        //ACT
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/spill")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        //ASSERT
        assertEquals(400, result.getResponse().getStatus());
    }

}
