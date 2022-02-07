package org.rakvag.blackjack

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.rakvag.blackjack.domene.Kort
import org.rakvag.blackjack.domene.Kort.Farge.*
import org.rakvag.blackjack.domene.Kort.Verdi
import org.rakvag.blackjack.domene.Kortstokk
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

	@MockBean
	private lateinit var provider: Provider

	@LocalServerPort
	private var serverPort: Int = -1

	@Autowired
	private lateinit var objectmapper: ObjectMapper

	private val kortstokk = mockk<Kortstokk>()

	private var sistReturnerteId = 0

	@BeforeEach
	fun setup() {
		Mockito.`when`(provider.hentNyKortstokk()).thenReturn(kortstokk)
		Mockito.`when`(provider.hentNySpillId()).thenReturn(++sistReturnerteId)
		every { kortstokk.blandKortene() } returns Unit
	}

	@Test
	fun `opprette nytt spill`() {
		every { kortstokk.trekkKort() } returnsMany listOf(
			Kort(HJERTER, Verdi.FEM),
			Kort(RUTER, Verdi.ÅTTE),
			Kort(KLØVER, Verdi.TRE),
			Kort(HJERTER, Verdi.FIRE),
		)

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
					"status":"I_GANG",
					"spillId": 1
				}
			""".trimIndent(),
			response.body(),
			JSONCompareMode.LENIENT
		)

	}

	@Test
	fun `hente informasjon om spill`() {
		every { kortstokk.trekkKort() } returnsMany listOf(
			Kort(HJERTER, Verdi.FEM),
			Kort(RUTER, Verdi.ÅTTE),
			Kort(KLØVER, Verdi.TRE),
			Kort(HJERTER, Verdi.FIRE),
		)
		val httpClient = HttpClient.newHttpClient()

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
		every { kortstokk.trekkKort() } returnsMany listOf(
			Kort(HJERTER, Verdi.KONGE),
			Kort(RUTER, Verdi.ÅTTE),
			Kort(KLØVER, Verdi.NI),
			Kort(HJERTER, Verdi.FIRE),
			Kort(SPAR, Verdi.NI)
		)
		val httpClient = HttpClient.newHttpClient()

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
					"spillId": 1
				}
			""".trimIndent(),
			response.body(),
			JSONCompareMode.LENIENT
		)

	}

	@Test
	fun `spiller trekker kort`() {
		every { kortstokk.trekkKort() } returnsMany listOf(
			Kort(HJERTER, Verdi.TRE),
			Kort(RUTER, Verdi.ÅTTE),
			Kort(KLØVER, Verdi.NI),
			Kort(HJERTER, Verdi.FIRE),
			Kort(SPAR, Verdi.FEM)
		)
		val httpClient = HttpClient.newHttpClient()

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
					"spillId": 1
				}
			""".trimIndent(),
			response.body(),
			JSONCompareMode.LENIENT
		)

	}
}
