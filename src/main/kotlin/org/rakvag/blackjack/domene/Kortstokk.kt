package org.rakvag.blackjack.domene

interface Kortstokk {

    fun blandKortene()
    fun trekkKort(): Kort
    fun eksporterState(): List<Kort>

}