package org.rakvag.blackjack

import org.rakvag.blackjack.domene.Spill
import org.springframework.stereotype.Repository

@Repository
class Repository {

    private val spillene = mutableMapOf<Int, Spill>()

    fun lagre(spill: Spill) {
        spillene[spill.id] = spill
    }

    fun hentPåId(id: Int): Spill {
        require(spillene.containsKey(id)) {
            "Finnes ikke noe spill med id $id"
        }
        return spillene[id]!!
    }

}