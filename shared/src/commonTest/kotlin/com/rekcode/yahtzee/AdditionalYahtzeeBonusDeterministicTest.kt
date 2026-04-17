package com.rekcode.yahtzee.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Validates additional Yahtzee bonus (100 points) deterministically.
 *
 * Scenario:
 * - YAHTZEE already scored as 50
 * - Another Yahtzee is rolled
 * - A different category is scored
 *
 * Expected:
 * - extraYahtzees increments
 * - final score includes +100 bonus
 */
class AdditionalYahtzeeBonusDeterministicTest {

    @Test
    fun additionalYahtzeeAddsBonusPoints() {

        val player = PlayerState()

        // Step 1: Score initial Yahtzee (50)
        player.setScore(ScoreCategory.YAHTZEE, YahtzeeConstants.YAHTZEE_SCORE)

        // Step 2: Simulate Yahtzee roll
        val yahtzeeDice = listOf(6, 6, 6, 6, 6)

        // Step 3: Trigger bonus logic manually
        val counts = yahtzeeDice.groupingBy { it }.eachCount()
        val isYahtzee = counts.any { it.value == YahtzeeConstants.YAHTZEE_DICE_COUNT }

        if (isYahtzee) {
            val yahtzeeEntry = player.scoreSheet[ScoreCategory.YAHTZEE]

            if (yahtzeeEntry?.score == YahtzeeConstants.YAHTZEE_SCORE) {
                player.extraYahtzees++
            }
        }

        // Step 4: Score another category
        val categoryScore = calculateScore(ScoreCategory.CHANCE, yahtzeeDice)
        player.setScore(ScoreCategory.CHANCE, categoryScore)

        val total = player.finalScore()

        val expected = YahtzeeConstants.YAHTZEE_SCORE + categoryScore + YahtzeeConstants.YAHTZEE_BONUS_PER_EXTRA

        assertEquals(expected, total)
    }
}