package org.rakvag.blackjack

import org.rakvag.blackjack.domene.Kort
import org.rakvag.blackjack.domene.Kortstokk

class TestKortstokk: Kortstokk {

    private val deque = ArrayDeque<Kort>()

    fun gjørKlarKortstokken(deFørsteKortene: List<Kort>) {
        //Legger innholdet i deFørsteKortene øverst i kortstokken
        deFørsteKortene.forEach { deque.addLast(it) }
        //Legger de resterende kortene i kortstokken slik at det blir totalt 52 kort
        listOf(Kort.Farge.HJERTER, Kort.Farge.KLØVER, Kort.Farge.RUTER, Kort.Farge.SPAR).forEach { farge ->
            Kort.Verdi.values().toList().forEach loopVerdi@{ verdi ->
                val kort = Kort(farge, verdi)
                if (deFørsteKortene.contains(kort)) {
                    return@loopVerdi
                }
                deque.addLast(kort)
            }
        }

    }

    override fun blandKortene() {
        //Gjør ingenting
    }

    override fun trekkKort(): Kort {
        check(deque.isNotEmpty()) {"Kortstokken er ikke klar enda - kjør gjørKlarKortstokk()"}
        return deque.removeFirst()
    }

    override fun eksporterState(): List<Kort> { return deque.toList() }
}