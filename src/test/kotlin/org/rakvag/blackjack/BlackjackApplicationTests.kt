package org.rakvag.blackjack

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.rakvag.blackjack.domene.Kort
import org.rakvag.blackjack.domene.Kort.Farge.*
import org.rakvag.blackjack.domene.Kort.Verdi
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlackjackApplicationTests {

	@LocalServerPort
	private var serverPort: Int = -1

	@MockBean
	private lateinit var kortstokkProvider: KortstokkProvider

	@Autowired
	private lateinit var objectmapper: ObjectMapper

	private val testKortstokk = TestKortstokk()

	@BeforeEach
	fun setup() {
		Mockito.`when`(kortstokkProvider.hentNyKortstokk()).thenReturn(testKortstokk)
	}

	@Test
	fun `opprette nytt spill`() {
		//ARRANGE
		testKortstokk.gjørKlarKortstokken(listOf(
			Kort(HJERTER, Verdi.FEM),
			Kort(RUTER, Verdi.ÅTTE),
			Kort(KLØVER, Verdi.TRE),
			Kort(HJERTER, Verdi.FIRE)
		))

		//ACT
		val httpClient = HttpClient.newHttpClient()
		val httpRequest = HttpRequest.newBuilder()
			.uri(URI("http://localhost:$serverPort/spill"))
			.POST(BodyPublishers.ofString(""" { "spillersNavn": "Testesen" }"""))
			.build()
		val response = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))

		//ASSERT
		assertEquals(200, response.statusCode())
		JSONAssert.assertEquals(
			"""
				{
					"spillersNavn":"Testesen",
					"spillersKort":[
						"H5","K3"
					],
					"spillersPoengsummer":[8],
					"antallKortHosDealer":2,
					"dealersÅpneKort":[
						"R8"
					],
					"status":"I_GANG"
				}
			""".trimIndent(),
			response.body(),
			JSONCompareMode.LENIENT
		)
		assertTrue(objectmapper.readValue(response.body(), Map::class.java).containsKey("spillId"))

	}

	@Test
	fun `hente informasjon om spill`() {
		val httpClient = HttpClient.newHttpClient()

		testKortstokk.gjørKlarKortstokken(listOf(
			Kort(HJERTER, Verdi.FEM),
			Kort(RUTER, Verdi.ÅTTE),
			Kort(KLØVER, Verdi.TRE),
			Kort(HJERTER, Verdi.FIRE)
		))

		var httpRequest = HttpRequest.newBuilder()
			.uri(URI("http://localhost:$serverPort/spill"))
			.POST(BodyPublishers.ofString(""" { "spillersNavn": "Testesen" }"""))
			.build()
		val opprettResponse = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))
		val spillId = objectmapper.readValue<Map<String, Any>>(opprettResponse.body())["spillId"] as Int

		//ACT
		httpRequest = HttpRequest.newBuilder()
			.uri(URI("http://localhost:$serverPort/spill/$spillId"))
			.GET()
			.build()
		val response = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))

		//ASSERT
		assertEquals(200, response.statusCode())
		JSONAssert.assertEquals(
			"""
				{
					"spillersNavn":"Testesen",
					"spillersKort":[
						"H5","K3"
					],
					"spillersPoengsummer":[8],
					"antallKortHosDealer":2,
					"dealersÅpneKort":[
						"R8"
					],
					"status":"I_GANG",
					"spillId": 1
				}
			""".trimIndent(),
			response.body(),
			JSONCompareMode.LENIENT
		)

	}

	@Test
	fun `spiller står`() {
		val httpClient = HttpClient.newHttpClient()

		testKortstokk.gjørKlarKortstokken(listOf(
			Kort(HJERTER, Verdi.KONGE),
			Kort(RUTER, Verdi.ÅTTE),
			Kort(KLØVER, Verdi.NI),
			Kort(HJERTER, Verdi.FIRE),
			Kort(SPAR, Verdi.NI)
		))

		var httpRequest = HttpRequest.newBuilder()
			.uri(URI("http://localhost:$serverPort/spill"))
			.POST(BodyPublishers.ofString(""" { "spillersNavn": "Testesen" }"""))
			.build()
		val opprettResponse = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))
		val spillId = objectmapper.readValue<Map<String, Any>>(opprettResponse.body())["spillId"] as Int

		//ACT
		httpRequest = HttpRequest.newBuilder()
			.uri(URI("http://localhost:$serverPort/spill/$spillId/staa"))
			.POST(BodyPublishers.noBody())
			.build()
		val response = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))

		//ASSERT
		assertEquals(200, response.statusCode())
		JSONAssert.assertEquals(
			"""
				{
					"spillersNavn":"Testesen",
					"spillersKort":[
						"HK","K9"
					],
					"spillersPoengsummer":[19],
					"antallKortHosDealer":3,
					"dealersÅpneKort":[
						"R8","H4","S9"
					],
					"status":"DEALER_VANT_PÅ_POENG",
					"spillId": $spillId
				}
			""".trimIndent(),
			response.body(),
			JSONCompareMode.LENIENT
		)

	}

	@Test
	fun `spiller trekker kort`() {
		val httpClient = HttpClient.newHttpClient()

		testKortstokk.gjørKlarKortstokken(listOf(
			Kort(HJERTER, Verdi.TRE),
			Kort(RUTER, Verdi.ÅTTE),
			Kort(KLØVER, Verdi.NI),
			Kort(HJERTER, Verdi.FIRE),
			Kort(SPAR, Verdi.FEM)
		))

		var httpRequest = HttpRequest.newBuilder()
			.uri(URI("http://localhost:$serverPort/spill"))
			.POST(BodyPublishers.ofString(""" { "spillersNavn": "Testesen" }"""))
			.build()
		val opprettResponse = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))
		val spillId = objectmapper.readValue<Map<String, Any>>(opprettResponse.body())["spillId"] as Int

		//ACT
		httpRequest = HttpRequest.newBuilder()
			.uri(URI("http://localhost:$serverPort/spill/$spillId/trekk"))
			.POST(BodyPublishers.noBody())
			.build()
		val response = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))

		//ASSERT
		assertEquals(200, response.statusCode())
		JSONAssert.assertEquals(
			"""
				{
					"spillersNavn":"Testesen",
					"spillersKort":[
						"H3","K9","S5"
					],
					"spillersPoengsummer":[17],
					"antallKortHosDealer":2,
					"dealersÅpneKort":[
						"R8"
					],
					"status":"I_GANG",
					"spillId": $spillId
				}
			""".trimIndent(),
			response.body(),
			JSONCompareMode.LENIENT
		)

	}

	@Test
	fun `spiller trekker kort flere ganger, og så står`() {
		val httpClient = HttpClient.newHttpClient()

		testKortstokk.gjørKlarKortstokken(listOf(
			Kort(SPAR, Verdi.TRE),     // Spiller kort 1
			Kort(HJERTER, Verdi.ÅTTE), // Dealer kort 1
			Kort(SPAR, Verdi.FIRE),    // Spiller kort 2
			Kort(HJERTER, Verdi.FIRE), // Dealer kort 2
			Kort(SPAR, Verdi.FEM),     // Spiller kort 3
			Kort(SPAR, Verdi.ÅTTE),      // Spiller kort 4
			Kort(HJERTER, Verdi.FEM)   // Dealer kort 3
		))

		var httpRequest = HttpRequest.newBuilder().uri(URI("http://localhost:$serverPort/spill"))
			.POST(BodyPublishers.ofString(""" { "spillersNavn": "Testesen" }""")).build()
		val opprettResponse = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))
		val spillId = objectmapper.readValue<Map<String, Any>>(opprettResponse.body())["spillId"] as Int

		httpRequest = HttpRequest.newBuilder().uri(URI("http://localhost:$serverPort/spill/$spillId/trekk"))
			.POST(BodyPublishers.noBody()).build()
		httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))

		httpRequest = HttpRequest.newBuilder().uri(URI("http://localhost:$serverPort/spill/$spillId/trekk"))
			.POST(BodyPublishers.noBody()).build()
		httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))

		//ACT
		httpRequest = HttpRequest.newBuilder().uri(URI("http://localhost:$serverPort/spill/$spillId/staa"))
			.POST(BodyPublishers.noBody()).build()
		val response = httpClient.send(httpRequest, BodyHandlers.ofString(Charsets.UTF_8))

		//ASSERT
		assertEquals(200, response.statusCode())
		JSONAssert.assertEquals(
			"""
				{
					"spillersNavn":"Testesen",
					"spillersKort":[
						"S3","S4","S5","S8"
					],
					"spillersPoengsummer":[20],
					"antallKortHosDealer":3,
					"dealersÅpneKort":[
						"H8","H4","H5"
					],
					"dealersPoengsummer":[17],
					"status":"SPILLER_VANT_PÅ_POENG",
					"spillId": $spillId
				}
			""".trimIndent(),
			response.body(),
			JSONCompareMode.LENIENT
		)

	}


}
