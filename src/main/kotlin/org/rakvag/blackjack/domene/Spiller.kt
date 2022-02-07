package org.rakvag.blackjack.domene

class Spiller(
    val navn: String,
    førsteToKort: List<Kort>
) {

    private val _kort: MutableList<Kort> = førsteToKort.toMutableList()

    var poengsummer: List<Int> = regnUtPoengsummer(_kort)
        private set

    val kort: List<Kort>
        get() = _kort.toList()

    fun taImotKort(kort: Kort) {
        _kort.add(kort)
        poengsummer = regnUtPoengsummer(_kort)
    }

}