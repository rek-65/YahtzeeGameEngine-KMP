package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.createGame
import com.rekcode.yahtzee.api.ScoreCategory
import kotlin.test.Test
import kotlin.test.fail

/**
 * TurnResetTest
 *
 * PURPOSE:
 * Validates that dice locks are cleared after a turn ends.
 *
 * This specifically targets the TODO in PlayerDiceState.resetLocks().
 *
 * VALIDATION RULES:
 * - After scoring (ending turn), all dice should be unlocked
 */
class TurnResetTest {

    @Test
    fun locksAreClearedAfterTurnEnds() {

        val game = createGame(1)

        // Roll so scoring is allowed
        game.rollDice()

        // Lock a die
        game.setDieHold(0, true)

        // Score to end turn
        game.score(ScoreCategory.CHANCE)

        // After turn reset, attempt to roll
        game.rollDice()

        // If lock was NOT cleared, die 0 will remain unchanged across rolls
        val first = game.getCurrentDice()[0]

        var changed = false

        repeat(10) {
            game.rollDice()
            if (game.getCurrentDice()[0] != first) {
                changed = true
                return@repeat
            }
        }

        if (!changed) {
            fail("Die remained locked after turn reset — resetLocks() is not working")
        }
    }
}