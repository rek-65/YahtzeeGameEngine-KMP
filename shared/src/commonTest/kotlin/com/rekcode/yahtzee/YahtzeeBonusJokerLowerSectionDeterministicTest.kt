package com.rekcode.yahtzee.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Validates combined behavior for:
 * - initial YAHTZEE scored as 50
 * - additional Yahtzee rolled later
 * - matching upper section already filled
 * - lower section Joker-rule fixed score applied
 * - 100-point additional Yahtzee bonus included
 */
class YahtzeeBonusJokerLowerSectionDeterministicTest {

    @Test
    fun additionalYahtzeeAddsBonusAndUsesJokerRuleForLargeStraight() {
        val player = PlayerState()

        player.setScore(ScoreCategory.YAHTZEE, YahtzeeConstants.YAHTZEE_SCORE)
        player.setScore(ScoreCategory.SIXES, 18)

        val scoreSheet = createEmptyScoreSheet().apply {
            putAll(player.scoreSheet)
        }

        val preview = previewScore(
            category = ScoreCategory.LARGE_STRAIGHT,
            dice = listOf(6, 6, 6, 6, 6),
            scoreSheet = scoreSheet
        )

        assertEquals(YahtzeeConstants.LARGE_STRAIGHT_SCORE, preview)

        player.extraYahtzees++
        player.setScore(ScoreCategory.LARGE_STRAIGHT, preview!!)

        assertEquals(1, player.extraYahtzees)
        assertEquals(
            YahtzeeConstants.YAHTZEE_SCORE +
                    18 +
                    YahtzeeConstants.LARGE_STRAIGHT_SCORE +
                    YahtzeeConstants.YAHTZEE_BONUS_PER_EXTRA,
            player.finalScore()
        )
    }
}