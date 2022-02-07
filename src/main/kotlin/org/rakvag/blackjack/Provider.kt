package org.rakvag.blackjack

import org.rakvag.blackjack.domene.Kortstokk
import org.springframework.stereotype.Component

@Component
class Provider {

    private var sistReturnerteId: Int = 0

    fun hentNyKortstokk(): Kortstokk {
        return Kortstokk()
    }

    fun hentNySpillId(): Int {
        return ++sistReturnerteId
    }
}