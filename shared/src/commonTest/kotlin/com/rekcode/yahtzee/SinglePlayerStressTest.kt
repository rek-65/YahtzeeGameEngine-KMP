package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.createGame
import kotlin.test.Test
import kotlin.test.fail

/**
 * Single Player Stress Test
 *
 * PURPOSE:
 * - Validate full game lifecycle stability
 * - Ensure no crashes across repeated playthroughs
 * - Verify game always reaches completion
 *
 * SCOPE:
 * - 1 player (most common usage)
 * - Randomized play decisions
 * - Full game completion required
 */
class SinglePlayerStressTest {

    @Test
    fun runSinglePlayerStressTest() {

        val totalGames = 1000
        var completedGames = 0

        repeat(totalGames) {

            val game = createGame(1)

            while (!game.isGameOver()) {

                while (game.canRoll()) {
                    game.rollDice()
                }

                val sheet = game.getScoreSheet(0)
                val availableCategories = sheet
                    .filter { !it.isLocked }
                    .map { it.category }

                if (availableCategories.isEmpty()) {
                    fail("No available categories but game not over")
                }

                val choice = availableCategories.random()

                game.score(choice)
            }

            if (!game.isGameOver()) {
                fail("Game did not complete")
            }

            val score = game.getPlayerFinalScore(0)

            if (score < 0) {
                fail("Invalid negative score: $score")
            }

            completedGames++
        }

        println("Completed $completedGames / $totalGames games successfully")
    }
}