package org.rakvag.blackjack.persistering

import org.rakvag.blackjack.domene.Spill
import org.rakvag.blackjack.persistering.tabell.DealerTabell
import org.rakvag.blackjack.persistering.tabell.SpillTabell
import org.rakvag.blackjack.persistering.tabell.SpillerTabell
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class Repository(
    private val spillTabell: SpillTabell,
    private val dealerTabell: DealerTabell,
    private val spillerTabell: SpillerTabell
) {

    @Transactional
    fun lagre(spill: Spill) {
        if (!spill.harVærtPersistert()) {
            val spillState = spill.eksporterState()
            spillTabell.insert(spillState)
            dealerTabell.insert(spillState.dealer!!, spillState.id)
            spillerTabell.insert(spillState.spiller!!, spillState.id)
            spill.registrerPersistert()
        } else if (spill.erEndretEtterSistePersistering()) {
            val spillState = spill.eksporterState()
            spillTabell.update(spillState)
            dealerTabell.update(spillState.dealer!!)
            spillerTabell.update(spillState.spiller!!)
            spill.registrerPersistert()
        }
    }

    fun hentPåId(id: Long): Spill {
        val dealerState = dealerTabell.hentPåSpillId(id)
        val spillerState = spillerTabell.hentPåSpillId(id)
        val spillState = spillTabell.hentPåId(id)
        val spill = Spill(spillState.copy(spiller = spillerState, dealer = dealerState))
        spill.registrerPersistert()
        return spill
    }
}