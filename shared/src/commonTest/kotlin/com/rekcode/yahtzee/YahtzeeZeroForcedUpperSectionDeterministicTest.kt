package com.rekcode.yahtzee.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Validates the forced upper-section placement rule when:
 * - YAHTZEE is already locked at zero
 * - another Yahtzee is rolled
 * - matching upper section category is still open
 *
 * Expected:
 * - matching upper section scores normally
 * - no 100-point additional Yahtzee bonus is awarded
 */
class YahtzeeZeroForcedUpperSectionDeterministicTest {

    @Test
    fun zeroedYahtzeeUsesOpenMatchingUpperCategoryWithoutBonus() {
        val player = PlayerState()

        player.setScore(ScoreCategory.YAHTZEE, 0)

        val dice = listOf(6, 6, 6, 6, 6)

        val preview = previewScore(
            category = ScoreCategory.SIXES,
            dice = dice,
            scoreSheet = player.scoreSheet
        )

        assertEquals(30, preview)

        player.setScore(ScoreCategory.SIXES, preview!!)

        assertEquals(0, player.extraYahtzees)
        assertEquals(30, player.finalScore())
    }
}