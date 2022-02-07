package org.rakvag.blackjack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlackjackApplication

fun main(args: Array<String>) {
	runApplication<BlackjackApplication>(*args)
}
