package org.rakvag.blackjack.domene

import org.rakvag.blackjack.domene.Spill.Status.*

class Spill(
    spillersNavn: String,
    private val kortstokk: Kortstokk
) {

    enum class Status {
        I_GANG, SPILLER_VANT_MED_BLACKJACK, DEALER_VANT_MED_BLACKJACK, SPILLER_VANT_PÅ_POENG, PUSH, SPILLER_BUST,
        DEALER_BUST, DEALER_VANT_PÅ_POENG
    }

    private val spiller: Spiller
    val spillersNavn: String
        get() = spiller.navn
    val spillerskort: List<Kort>
        get() = spiller.kort
    val spillersPoengsummer: List<Int>
        get() = spiller.poengsummer

    private val dealer: Dealer
    val dealersÅpneKort: List<Kort>
        get() = dealer.åpneKort
    val antallKortHosDealer: Int
        get() = dealer.antallKort
    fun hentDealersPoengsummer(): List<Int> { //Bruker en funksjon i stedet for property her, siden det kan kastes exception
        check(dealer.viserAlleKort) {
            "Ikke tillatt å hente dealersPoengsummer før spillet er ferdig, og dealer har snudd kortene sine"
        }
        return dealer.poengsummer
    }

    var status: Status
        private set

    init {
        val kort1 = kortstokk.trekkKort()
        val kort2 = kortstokk.trekkKort()
        val kort3 = kortstokk.trekkKort()
        val kort4 = kortstokk.trekkKort()
        this.spiller = Spiller(spillersNavn, listOf(kort1, kort3))
        this.dealer = Dealer(listOf(kort2, kort4))
        this.status = beregnStatus()
        if (status != I_GANG) {
            dealer.snuKortene()
        }
    }

    fun spillerTrekkerKort() {
        check(status == I_GANG) { "Spillet er avsluttet" }
        spiller.taImotKort(kortstokk.trekkKort())
        status = beregnStatus()
        if (status != I_GANG) {
            dealer.snuKortene()
        }
    }

    fun spillerStår() {
        check(status == I_GANG) { "Spillet er avsluttet" }
        status = beregnStatus()
        while (status == I_GANG) {
            dealer.taImotKort(kortstokk.trekkKort())
            status = beregnStatus()
        }
        dealer.snuKortene()
    }

    private fun beregnStatus(): Status {
        //For sjekk på blackjack (kun relevant når kortene akkurat er delt ut)
        if (spiller.kort.size == 2 && dealer.antallKort == 2) {
            if (spiller.poengsummer.contains(21) && dealer.poengsummer.contains(21)) {
                return PUSH
            }
            if (spiller.poengsummer.contains(21) && !dealer.poengsummer.contains(21)) {
                return SPILLER_VANT_MED_BLACKJACK
            }
            if (!spiller.poengsummer.contains(21) && dealer.poengsummer.contains(21)) {
                return DEALER_VANT_MED_BLACKJACK
            }
        }

        //Hvis alle spillers poengsumer er større enn 21, er spiller bust
        if (spiller.poengsummer.none { it < 22 }) {
            return SPILLER_BUST
        }

        if (dealer.poengsummer.none { it < 22 }) {
            return DEALER_BUST
        }

        //Hvis dealer har 17 eller høyere (og ikke bust), må dealer stå
        if (dealer.poengsummer.any { it == 17 || it == 18 || it == 19 || it == 20 || it == 21 }) {
            val høyestPoengsumSpiller = spiller.poengsummer.filter { it < 22 }.maxOrNull()!!
            val høyestePoengsumDealer = dealer.poengsummer.filter { it < 22 }.maxOrNull()!!
            return if (høyestPoengsumSpiller == høyestePoengsumDealer) {
                PUSH
            } else if (høyestPoengsumSpiller > høyestePoengsumDealer) {
                SPILLER_VANT_PÅ_POENG
            } else {
                DEALER_VANT_PÅ_POENG
            }
        }

        return I_GANG
    }

}