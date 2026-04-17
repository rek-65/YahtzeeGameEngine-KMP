package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.createGame
import kotlin.test.Test

/**
 * UpperBonusObservationTest
 *
 * PURPOSE:
 * Runs multiple games with minimal output.
 *
 * OUTPUT STRATEGY:
 * - Prints progress every 10 games
 * - Prints FULL details ONLY when upper section total > 64
 *
 * This ensures:
 * - Low noise
 * - High-value validation data only
 */
class UpperBonusObservationTest {

    /**
     * Observes upper bonus behavior with minimal logging.
     */
    @Test
    fun observeUpperBonusHighSignalOnly() {

        val totalGames = 1000

        repeat(totalGames) { gameIndex ->

            if (gameIndex % 100 == 0) {
                println("Progress: Game $gameIndex")
            }

            val game = createGame(1)

            while (!game.isGameOver()) {

                while (game.canRoll()) {
                    game.rollDice()
                }

                val previews = game.previewScores()

                val best = previews
                    .filter { it.score != null && !it.isLocked }
                    .maxByOrNull { it.score!! }
                    ?: previews.first { !it.isLocked }

                game.score(best.category)
            }

            val finalScore = game.getPlayerFinalScore(0)
            val sheet = game.getScoreSheet(0)

            var upperTotal = 0
            var sheetTotal = 0

            sheet.forEach { item ->
                val score = item.score ?: 0
                sheetTotal += score

                if (item.category.name in listOf(
                        "ONES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES"
                    )
                ) {
                    upperTotal += score
                }
            }

            if (upperTotal > 64) {

                println("===================================")
                println("BONUS CONDITION HIT (Game $gameIndex)")
                println("Final Score: $finalScore")
                println("Sheet Total: $sheetTotal")
                println("Upper Total: $upperTotal")

                println("--- SCORE SHEET ---")
                sheet.forEach { item ->
                    println("${item.displayName}: ${item.score ?: 0}")
                }

                println("-------------------")
                println("EXPECTED BONUS: +35")
                println("ACTUAL DELTA: ${finalScore - sheetTotal}")
                println("===================================")
            }
        }
    }
}