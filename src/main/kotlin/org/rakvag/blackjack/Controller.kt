package org.rakvag.blackjack

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.rakvag.blackjack.domene.Spill
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/spill")
class Controller(
    private val objectMapper: ObjectMapper,
    private val provider: Provider,
    private val repository: Repository
) {

    @PostMapping
    fun opprettNyttSpill(@RequestBody requestBody: String): ResponseEntity<String> {
        val request = objectMapper.readValue<Request>(requestBody)

        val spill = Spill(request.spillersNavn, provider)
        val responseBody = mappSpillTilJsonResponse(spill)

        repository.lagre(spill)

        return ResponseEntity.ok(responseBody)
    }

    @GetMapping(path = ["/{id}"])
    fun hentSpill(@PathVariable("id") id: Int): ResponseEntity<String> {
        val spill = repository.hentPåId(id)
        val responseBody = mappSpillTilJsonResponse(spill)
        return ResponseEntity.ok(responseBody)
    }

    @PostMapping(path = ["/{id}/staa"])
    fun spillerStår(@PathVariable("id") id: Int): ResponseEntity<String> {
        val spill = repository.hentPåId(id)
        spill.spillerStår()
        repository.lagre(spill)
        val responseBody = mappSpillTilJsonResponse(spill)
        return ResponseEntity.ok(responseBody)
    }

    @PostMapping(path = ["/{id}/trekk"])
    fun spillerTrekkerKort(@PathVariable("id") id: Int): ResponseEntity<String> {
        val spill = repository.hentPåId(id)
        spill.spillerTrekkerKort()
        repository.lagre(spill)
        val responseBody = mappSpillTilJsonResponse(spill)
        return ResponseEntity.ok(responseBody)
    }

    private fun mappSpillTilJsonResponse(spill: Spill): String {
        return objectMapper.writeValueAsString(
            Response(
                spillersNavn = spill.spillersNavn,
                spillersKort = spill.spillersKort.map { it.toString() },
                spillersPoengsummer = spill.spillersPoengsummer,
                antallKortHosDealer = spill.antallKortHosDealer,
                dealersÅpneKort = spill.dealersÅpneKort.map { it.toString() },
                status = spill.status,
                spillId = spill.id
            )
        )
    }

    private data class Request(
        val spillersNavn: String
    )

    private data class Response(
        val spillersNavn: String,
        val spillersKort: List<String>,
        val spillersPoengsummer: List<Int>,
        val antallKortHosDealer: Int,
        val dealersÅpneKort: List<String>,
        val status: Spill.Status,
        val spillId: Int
    )

}