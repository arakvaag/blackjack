package org.rakvag.blackjack.persistering.tabell

import org.rakvag.blackjack.domene.Kort
import org.rakvag.blackjack.domene.Spill
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class SpillTabell(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun hentPåId(id: Long): Spill.State {
        return jdbcTemplate.query(
            "SELECT * FROM SPILL WHERE ID = :ID",
            MapSqlParameterSource(mutableMapOf("ID" to id)), rowMapper)[0]
    }

    fun insert(state: Spill.State) {
        jdbcTemplate.update(
            """
                INSERT INTO SPILL (
                    ID, STATUS, KORTSTOKK
                ) VALUES (
                    :ID, :STATUS, :KORTSTOKK
                )
            """.trimIndent(),
            mutableMapOf(
                "ID" to state.id,
                "STATUS" to state.status.name,
                "KORTSTOKK" to state.kortstokk.joinToString(separator = ",")
            )
        )
    }

    fun update(state: Spill.State) {
        jdbcTemplate.update(
            """
                UPDATE SPILL
                SET
                    STATUS = :STATUS, 
                    KORTSTOKK = :KORTSTOKK
                WHERE ID = :ID 
            """.trimIndent(),
            mapOf(
                "STATUS" to state.status.name,
                "KORTSTOKK" to state.kortstokk.joinToString(separator = ","),
                "ID" to state.id
            )
        )
    }

    private val rowMapper: RowMapper<Spill.State> = RowMapper { resultSet: ResultSet, _: Int ->
        Spill.State(
            id = resultSet.getBigDecimal("ID").toLong(),
            status = Spill.Status.valueOf(resultSet.getString("STATUS")!!),
            kortstokk = resultSet.getString("KORTSTOKK")!!
                .split(",")
                .map { Kort(it) },
            spiller = null,
            dealer = null,
        )
    }


}