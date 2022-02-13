package org.rakvag.blackjack.domene

import org.rakvag.blackjack.IdProvider

class Dealer {

    constructor(førsteToKort: List<Kort>, idProvider: IdProvider) {
        this.id = idProvider.hentNyDealerId()
        this._kort = førsteToKort.toMutableList()
        this.poengsummer = regnUtPoengsummer(_kort)
        this.viserAlleKort = false
    }

    constructor(state: State) {
        this.id = state.id
        this._kort = state.kort.toMutableList()
        this.poengsummer = regnUtPoengsummer(_kort)
        this.viserAlleKort = state.viserAlleKort
    }

    private val id: Long
    private val _kort: MutableList<Kort>

    val åpneKort: List<Kort>
        get() {
            return if (!viserAlleKort)
                listOf(_kort[0])
            else
                _kort.toList()
        }

    val antallKort: Int
        get() = _kort.size

    var poengsummer: List<Int>
        private set

    var viserAlleKort: Boolean
        private set

    fun snuKortene() {
        viserAlleKort = true
    }

    fun taImotKort(kort: Kort) {
        _kort.add(kort)
        poengsummer = regnUtPoengsummer(_kort)
    }

    //region persistering
    fun eksporterState(): State {
        return State(id, _kort.toList(), viserAlleKort)
    }
    data class State(
        val id: Long,
        val kort: List<Kort>,
        val viserAlleKort: Boolean
    )
    //endregion

}
