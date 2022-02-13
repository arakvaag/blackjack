package org.rakvag.blackjack.persistering.tabell

import org.rakvag.blackjack.domene.Kort
import org.rakvag.blackjack.domene.Spiller
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class SpillerTabell(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun hentPåSpillId(spillId: Long): Spiller.State {
        return jdbcTemplate.query(
            "SELECT * FROM SPILLER WHERE SPILL_ID = :SPILL_ID",
            MapSqlParameterSource(mutableMapOf("SPILL_ID" to spillId)), rowMapper)[0]
    }

    fun insert(state: Spiller.State, spillId: Long) {
        jdbcTemplate.update(
            """
                INSERT INTO SPILLER (
                    ID, SPILL_ID, NAVN, KORT, STAAR
                ) VALUES (
                    :ID, :SPILL_ID, :NAVN, :KORT, :STAAR
                )
            """.trimIndent(),
            mutableMapOf(
                "ID" to state.id,
                "SPILL_ID" to spillId,
                "NAVN" to state.navn,
                "KORT" to state.kort.joinToString(separator = ","),
                "STAAR" to state.står
            )
        )
    }

    fun update(state: Spiller.State) {
        jdbcTemplate.update(
            """
                UPDATE SPILLER
                SET
                    NAVN = :NAVN, 
                    KORT = :KORT,
                    STAAR = :STAAR
                WHERE ID = :ID 
            """.trimIndent(),
            mapOf(
                "NAVN" to state.navn,
                "KORT" to state.kort.joinToString(separator = ","),
                "STAAR" to state.står,
                "ID" to state.id
            )
        )
    }

    private val rowMapper: RowMapper<Spiller.State> = RowMapper { resultSet: ResultSet, _: Int ->
        Spiller.State(
            id = resultSet.getBigDecimal("ID").toLong(),
            kort = resultSet.getString("KORT")!!
                .split(",")
                .map { Kort(it) },
            navn = resultSet.getString("NAVN"),
            står = resultSet.getBoolean("STAAR")
        )
    }

}