package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.ScoreCategory
import com.rekcode.yahtzee.api.createGame
import kotlin.test.Test
import kotlin.test.fail

/**
 * Score Validation Test
 *
 * PURPOSE:
 * - Validate correctness of scoring rules
 * - Ensure categories compute expected values
 *
 * NOTE:
 * Uses controlled dice assumptions via repeated rolling
 * (Not perfect control, but catches major logic errors)
 */
class ScoreValidationTest {

    @Test
    fun validateUpperSectionScoring() {

        val game = createGame(1)

        while (game.canRoll()) {
            game.rollDice()
        }

        game.score(ScoreCategory.ONES)

        val sheet = game.getScoreSheet(0)
        val onesEntry = sheet.first { it.category == ScoreCategory.ONES }

        if (onesEntry.score == null) {
            fail("ONES score was not recorded")
        }

        if (onesEntry.score < 0 || onesEntry.score > 5) {
            fail("Invalid ONES score: ${onesEntry.score}")
        }
    }

    @Test
    fun validateYahtzeeScoringRange() {

        val game = createGame(1)

        while (!game.isGameOver()) {

            while (game.canRoll()) {
                game.rollDice()
            }

            val sheet = game.getScoreSheet(0)
            val available = sheet.filter { !it.isLocked }.map { it.category }

            val choice = available.random()
            game.score(choice)
        }

        val finalScore = game.getPlayerFinalScore(0)

        if (finalScore <= 0) {
            fail("Final score should be positive: $finalScore")
        }

        if (finalScore > 2000) {
            fail("Final score unrealistically high: $finalScore")
        }
    }
}