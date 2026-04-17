package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.createGame
import kotlin.test.Test
import kotlin.test.fail

/**
 * DiceHoldTest
 *
 * PURPOSE:
 * Validates that held dice are not re-rolled.
 *
 * VALIDATION RULES:
 * - Held dice must retain their value after roll
 * - Unheld dice may change
 * - Test uses only public API
 */
class DiceHoldTest {

    /**
     * Verifies that a held die does not change value after rolling.
     */
    @Test
    fun heldDieDoesNotChange() {

        val game = createGame(1)

        game.rollDice()

        val initialDice = game.getCurrentDice()

        game.setDieHold(0, true)

        game.rollDice()

        val newDice = game.getCurrentDice()

        val heldInitial = initialDice[0]
        val heldAfter = newDice[0]

        if (heldInitial != heldAfter) {
            fail("Held die changed value. Expected: $heldInitial, Actual: $heldAfter")
        }
    }

    /**
     * Verifies that releasing a held die allows it to roll again.
     */
    @Test
    fun releasedDieBecomesUnlocked() {

        val game = createGame(1)

        game.rollDice()

        if (!game.setDieHold(0, true)[0]) {
            fail("Held die did not report locked state")
        }

        if (game.setDieHold(0, false)[0]) {
            fail("Released die did not report unlocked state")
        }
    }
}