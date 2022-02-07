package org.rakvag.blackjack.domene

import org.rakvag.blackjack.domene.Kort.Farge.*
import org.rakvag.blackjack.domene.Kort.Verdi.*

class Kortstokk {

    private val deque = ArrayDeque<Kort>()

    init {
        listOf(HJERTER, KLØVER, RUTER, SPAR).forEach {
            deque.addLast(Kort(it, ESS))
            deque.addLast(Kort(it, TO))
            deque.addLast(Kort(it, TRE))
            deque.addLast(Kort(it, FIRE))
            deque.addLast(Kort(it, FEM))
            deque.addLast(Kort(it, SEKS))
            deque.addLast(Kort(it, SJU))
            deque.addLast(Kort(it, ÅTTE))
            deque.addLast(Kort(it, NI))
            deque.addLast(Kort(it, TI))
            deque.addLast(Kort(it, KNEKT))
            deque.addLast(Kort(it, DAME))
            deque.addLast(Kort(it, KONGE))
        }
    }

    fun blandKortene() {
        deque.shuffle()
    }

    fun trekkKort(): Kort {
        check(deque.isNotEmpty()) { "Kortstokken er tom"}
        return deque.removeFirst()
    }
}