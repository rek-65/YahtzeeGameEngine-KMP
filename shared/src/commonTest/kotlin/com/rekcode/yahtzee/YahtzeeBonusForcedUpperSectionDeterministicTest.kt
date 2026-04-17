package com.rekcode.yahtzee.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Validates the forced upper-section placement rule for an additional Yahtzee.
 *
 * Scenario:
 * - YAHTZEE already scored as 50
 * - another Yahtzee is rolled
 * - matching upper section category is still open
 *
 * Expected:
 * - the matching upper section score is the correct normal upper score
 * - the 100-point additional Yahtzee bonus is also included
 */
class YahtzeeBonusForcedUpperSectionDeterministicTest {

    @Test
    fun additionalYahtzeeUsesOpenMatchingUpperCategoryAndAddsBonus() {
        val player = PlayerState()

        player.setScore(ScoreCategory.YAHTZEE, YahtzeeConstants.YAHTZEE_SCORE)

        val dice = listOf(6, 6, 6, 6, 6)

        val preview = previewScore(
            category = ScoreCategory.SIXES,
            dice = dice,
            scoreSheet = player.scoreSheet
        )

        assertEquals(30, preview)

        player.extraYahtzees++
        player.setScore(ScoreCategory.SIXES, preview!!)

        assertEquals(
            YahtzeeConstants.YAHTZEE_SCORE +
                    30 +
                    YahtzeeConstants.YAHTZEE_BONUS_PER_EXTRA,
            player.finalScore()
        )
    }
}