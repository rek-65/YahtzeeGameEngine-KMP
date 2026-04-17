package com.rekcode.yahtzee.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Validates that additional Yahtzees do not award the 100-point bonus
 * when the YAHTZEE category was previously scored as zero.
 */
class AdditionalYahtzeeNoBonusWhenYahtzeeIsZeroTest {

    @Test
    fun additionalYahtzeeDoesNotAddBonusWhenYahtzeeCategoryIsZero() {

        val player = PlayerState()

        player.setScore(ScoreCategory.YAHTZEE, 0)

        val yahtzeeDice = listOf(6, 6, 6, 6, 6)

        val counts = yahtzeeDice.groupingBy { it }.eachCount()
        val isYahtzee = counts.any { it.value == YahtzeeConstants.YAHTZEE_DICE_COUNT }

        if (isYahtzee) {
            val yahtzeeEntry = player.scoreSheet[ScoreCategory.YAHTZEE]

            if (yahtzeeEntry?.score == YahtzeeConstants.YAHTZEE_SCORE) {
                player.extraYahtzees++
            }
        }

        val categoryScore = YahtzeeConstants.FULL_HOUSE_SCORE
        player.setScore(ScoreCategory.FULL_HOUSE, categoryScore)

        val total = player.finalScore()

        val expected = categoryScore

        assertEquals(expected, total)
        assertEquals(0, player.extraYahtzees)
    }
}