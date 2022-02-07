package org.rakvag.blackjack.domene

class Dealer(førsteToKort: List<Kort>) {

    private val _kort: MutableList<Kort> = førsteToKort.toMutableList()

    val åpneKort: List<Kort>
        get() {
            return if (!viserAlleKort)
                listOf(_kort[0])
            else
                _kort.toList()
        }

    val antallKort: Int
        get() = _kort.size

    var poengsummer: List<Int> = regnUtPoengsummer(_kort)
        private set

    var viserAlleKort: Boolean = false
        private set

    fun snuKortene() {
        viserAlleKort = true
    }

    fun taImotKort(kort: Kort) {
        _kort.add(kort)
        poengsummer = regnUtPoengsummer(_kort)
    }

}
