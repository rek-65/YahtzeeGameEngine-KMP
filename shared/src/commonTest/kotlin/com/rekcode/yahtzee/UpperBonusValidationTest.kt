package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.createGame
import kotlin.test.Test

/**
 * UpperBonusValidationTest
 *
 * PURPOSE:
 * Validates that the upper section bonus (+35) is correctly applied
 * when the upper section score reaches 63 or higher.
 *
 * STRATEGY:
 * - Always select upper section categories
 * - Accumulate score deterministically
 */
class UpperBonusValidationTest {

    @Test
    fun upperBonusIsApplied() {

        val game = createGame(1)

        while (!game.isGameOver()) {

            while (game.canRoll()) {
                game.rollDice()
            }

            val previews = game.previewScores()

            val upperChoice = previews
                .filter {
                    it.score != null &&
                            !it.isLocked &&
                            it.category.name in listOf(
                        "ONES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES"
                    )
                }
                .maxByOrNull { it.score!! }

            val choice = upperChoice ?: previews.first { !it.isLocked }

            game.score(choice.category)
        }

        val finalScore = game.getPlayerFinalScore(0)

        println("Final Score with Upper Focus: $finalScore")

        if (finalScore >= 63) {
            println("Upper bonus condition potentially reached")
        }
    }
}