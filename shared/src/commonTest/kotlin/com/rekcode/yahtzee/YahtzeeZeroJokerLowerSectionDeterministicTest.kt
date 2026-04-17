package com.rekcode.yahtzee.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Validates Joker Rule lower-section scoring when:
 * - YAHTZEE is already locked at zero
 * - another Yahtzee is rolled
 * - the matching upper section box is already filled
 *
 * Official rule result:
 * - no 100-point Yahtzee bonus
 * - score must follow Joker Rules in an open lower-section box
 */
class YahtzeeZeroJokerLowerSectionDeterministicTest {

    @Test
    fun zeroedYahtzeeUsesJokerRuleForLargeStraightWithoutBonus() {
        val player = PlayerState()

        player.setScore(ScoreCategory.YAHTZEE, 0)
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

        player.setScore(ScoreCategory.LARGE_STRAIGHT, preview!!)

        assertEquals(0, player.extraYahtzees)
        assertEquals(
            18 + YahtzeeConstants.LARGE_STRAIGHT_SCORE,
            player.finalScore()
        )
    }
}