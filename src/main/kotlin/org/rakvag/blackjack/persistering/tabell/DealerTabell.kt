package org.rakvag.blackjack.persistering.tabell

import org.rakvag.blackjack.domene.Dealer
import org.rakvag.blackjack.domene.Kort
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class DealerTabell(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun hentPåSpillId(spillId: Long): Dealer.State {
        return jdbcTemplate.query(
            "SELECT * FROM DEALER WHERE SPILL_ID = :SPILL_ID",
            MapSqlParameterSource(mutableMapOf("SPILL_ID" to spillId)), rowMapper)[0]
    }

    fun insert(state: Dealer.State, spillId: Long) {
        jdbcTemplate.update(
            """
                INSERT INTO DEALER (
                    ID, SPILL_ID, KORT, VISER_ALLE_KORT
                ) VALUES (
                    :ID, :SPILL_ID, :KORT, :VISER_ALLE_KORT
                )
            """.trimIndent(),
            mutableMapOf(
                "ID" to state.id,
                "SPILL_ID" to spillId,
                "KORT" to state.kort.joinToString(separator = ","),
                "VISER_ALLE_KORT" to state.viserAlleKort
            )
        )
    }

    fun update(state: Dealer.State) {
        jdbcTemplate.update(
            """
                UPDATE DEALER
                SET
                    KORT = :KORT,
                    VISER_ALLE_KORT = :VISER_ALLE_KORT
                WHERE ID = :ID 
            """.trimIndent(),
            mapOf(
                "KORT" to state.kort.joinToString(separator = ","),
                "VISER_ALLE_KORT" to state.viserAlleKort,
                "ID" to state.id
            )
        )
    }

    private val rowMapper: RowMapper<Dealer.State> = RowMapper { resultSet: ResultSet, _: Int ->
        Dealer.State(
            id = resultSet.getBigDecimal("ID").toLong(),
            kort = resultSet.getString("KORT")!!
                .split(",")
                .map { Kort(it) },
            viserAlleKort = resultSet.getBoolean("VISER_ALLE_KORT")
        )
    }

}