package org.rakvag.blackjack

import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class IdProvider(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun hentNySpillId(): Long {
        return jdbcTemplate.queryForObject(
            "SELECT NEXTVAL('SPILL_ID_SEQ')",
            EmptySqlParameterSource(),
            Long::class.java
        )!!
    }

    fun hentNySpillerId(): Long {
        return jdbcTemplate.queryForObject(
            "SELECT NEXTVAL('SPILLER_ID_SEQ')",
            EmptySqlParameterSource(),
            Long::class.java
        )!!
    }

    fun hentNyDealerId(): Long {
        return jdbcTemplate.queryForObject(
            "SELECT NEXTVAL('DEALER_ID_SEQ')",
            EmptySqlParameterSource(),
            Long::class.java
        )!!
    }
}