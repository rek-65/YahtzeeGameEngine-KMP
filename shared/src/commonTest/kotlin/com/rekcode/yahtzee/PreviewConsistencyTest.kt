package com.rekcode.yahtzee

import com.rekcode.yahtzee.api.createGame
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * PreviewConsistencyTest
 *
 * PURPOSE:
 * Validates that previewScores() produces results identical to actual scoring.
 *
 * IMPORTANT:
 * - Uses ONLY public API
 * - Does NOT access internal engine
 * - Does NOT modify engine
 *
 * WHAT THIS VALIDATES:
 * - Preview scores match actual applied scores
 * - Preview system is reliable for decision making
 */
class PreviewConsistencyTest {

    /**
     * Runs multiple full games and verifies that previewed scores
     * match the actual scores when applied.
     */
    @Test
    fun previewMatchesActualScore() {

        val totalGames = 50

        repeat(totalGames) {

            val game = createGame(1)

            while (!game.isGameOver()) {

                while (game.canRoll()) {
                    game.rollDice()
                }

                val previews = game.previewScores()

                val choice = previews.firstOrNull {
                    it.score != null && !it.isLocked
                }

                assertNotNull(choice, "No valid preview category found")

                val expectedScore = choice.score

                println("Category: ${choice.category}")
                println("Preview: ${choice.score}")
                println("Dice: " + game.getCurrentDice())

                game.score(choice.category)

                val sheet = game.getScoreSheet(0)

                val actualEntry = sheet.first {
                    it.category == choice.category
                }

                val actualScore = actualEntry.score

                println("Actual: $actualScore")
                println("-----")

                assertEquals(
                    expectedScore,
                    actualScore,
                    "Preview score does not match actual score"
                )
            }
        }
    }
}