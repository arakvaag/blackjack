package org.rakvag.blackjack

import org.rakvag.blackjack.domene.Kortstokk
import org.rakvag.blackjack.domene.KortstokkImpl
import org.springframework.stereotype.Component

@Component
class KortstokkProvider {

    fun hentNyKortstokk(): Kortstokk {
        return KortstokkImpl()
    }

}