package org.rakvag.blackjack.domene

import org.rakvag.blackjack.IdProvider

class Spiller {

    constructor(navn: String, førsteToKort: List<Kort>, idProvider: IdProvider) {
        this.id = idProvider.hentNySpillerId()
        this.navn = navn
        this._kort = førsteToKort.toMutableList()
        this.poengsummer = regnUtPoengsummer(_kort)
    }

    constructor(state: State) {
        this.id = state.id
        this.navn = state.navn
        this._kort = state.kort.toMutableList()
        this.poengsummer = regnUtPoengsummer(_kort)
    }

    private val id: Long
    val navn: String
    private val _kort: MutableList<Kort>

    var poengsummer: List<Int>
        private set

    val kort: List<Kort>
        get() = _kort.toList()

    var står: Boolean = false
        private set

    fun taImotKort(kort: Kort) {
        _kort.add(kort)
        poengsummer = regnUtPoengsummer(_kort)
    }

    fun stå() {
        this.står = true
    }

    //region persistering
    fun eksporterState(): State {
        return State(id, navn, kort, står)
    }
    data class State(
        val id: Long,
        val navn: String,
        val kort: List<Kort>,
        val står: Boolean
    )
    //endregion

}