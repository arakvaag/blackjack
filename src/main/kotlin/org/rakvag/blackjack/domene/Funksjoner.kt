package org.rakvag.blackjack.domene

fun regnUtPoengsummer(kort: List<Kort>): List<Int> {
    val antallEss = kort.map { it.verdi }.count { it == Kort.Verdi.ESS }
    val poengsummer = mutableListOf<Int>()
    for (i in antallEss downTo 0) {
        poengsummer.add(kort.sumOf { it.verdi.tallVerdi } - i*10)
    }
    return poengsummer
}
