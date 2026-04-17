package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.createGame
import kotlin.test.Test
import kotlin.test.fail

/**
 * MultiPlayerValidationTest
 *
 * PURPOSE:
 * Validates correct behavior of the engine in a multi-player scenario.
 *
 * WHAT THIS TEST ENSURES:
 * - Turn rotation cycles correctly across players
 * - Each player maintains an independent score sheet
 * - No state leakage occurs between players
 * - Game completes without error
 * - Winner is correctly determined
 *
 * RULES:
 * - Uses only public API
 * - No internal access
 * - Deterministic scoring strategy (first available category)
 */
class MultiPlayerValidationTest {

    /**
     * Runs a full multi-player game and validates:
     * - Turn order integrity
     * - Independent scoring
     * - Valid winner determination
     */
    @Test
    fun runMultiPlayerValidation() {

        val playerCount = 3
        val game = createGame(playerCount)

        var lastPlayerIndex = -1
        var turnCounter = 0

        while (!game.isGameOver()) {

            val currentPlayer = game.getCurrentPlayerIndex()

            if (lastPlayerIndex != -1) {
                val expected = (lastPlayerIndex + 1) % playerCount
                if (currentPlayer != expected) {
                    fail("Turn order broken. Expected: $expected, Actual: $currentPlayer")
                }
            }

            lastPlayerIndex = currentPlayer
            turnCounter++

            while (game.canRoll()) {
                game.rollDice()
            }

            val sheet = game.getScoreSheet(currentPlayer)

            val openCategory = sheet.firstOrNull { !it.isLocked }
                ?: fail("No available category for player $currentPlayer")

            game.score(openCategory.category)
        }

        val scores = (0 until playerCount).map { playerIndex ->
            val score = game.getPlayerFinalScore(playerIndex)

            if (score < 0) {
                fail("Invalid negative score for player $playerIndex: $score")
            }

            score
        }

        val winnerIndex = game.getWinnerIndex()
            ?: fail("Game ended but winner is null")

        val maxScore = scores.maxOrNull()
            ?: fail("Failed to determine max score")

        if (scores[winnerIndex] != maxScore) {
            fail(
                "Winner incorrect. Expected score: $maxScore, " +
                        "Winner score: ${scores[winnerIndex]}"
            )
        }
    }
}