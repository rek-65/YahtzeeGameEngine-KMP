package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.createGame
import com.rekcode.yahtzee.api.ScoreCategory
import kotlin.test.Test

/**
 * UpperBonusWithHoldTest
 *
 * PURPOSE:
 * Validates that the upper section bonus is achievable when
 * using dice hold mechanics with a consistent per-turn strategy.
 *
 * RULES:
 * - Uses only public API
 * - Commits to a single category per turn
 * - Applies dice holds based on that category
 * - Prints minimal output for verification
 */
class UpperBonusWithHoldTest {

    /**
     * Runs multiple games and prints only when the upper section
     * total exceeds the bonus threshold.
     */
    @Test
    fun validateUpperBonusReachable() {

        val totalGames = 200

        repeat(totalGames) { gameIndex ->

            val game = createGame(1)

            while (!game.isGameOver()) {

                var targetCategory: ScoreCategory? = null

                while (game.canRoll()) {

                    game.rollDice()

                    val dice = game.getCurrentDice()

                    if (targetCategory == null) {
                        val previews = game.previewScores()

                        targetCategory = previews
                            .filter { !it.isLocked }
                            .firstOrNull { it.category.ordinal < 6 }
                            ?.category
                            ?: previews.first { !it.isLocked }.category
                    }

                    val targetValue = targetCategory.ordinal + 1

                    dice.forEachIndexed { index, value ->
                        game.setDieHold(index, value == targetValue)
                    }
                }

                game.score(targetCategory!!)
            }

            val sheet = game.getScoreSheet(0)

            val upperTotal = sheet
                .take(6)
                .sumOf { it.score ?: 0 }

            if (upperTotal > 64) {

                println("Game $gameIndex")
                println("Upper Total: $upperTotal")

                sheet.take(6).forEach {
                    println("${it.displayName}: ${it.score}")
                }

                println("-----")
            }
        }
    }
}