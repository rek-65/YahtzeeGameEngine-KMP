package com.rekcode.yahtzee.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Validates Joker Rule preview behavior using direct engine setup.
 *
 * This test bypasses the public API intentionally so rare scenarios
 * can be reproduced deterministically.
 */
class YahtzeeJokerRulePreviewDeterministicTest {

    @Test
    fun fullHousePreviewsTwentyFiveUnderJokerRule() {
        val scoreSheet = createEmptyScoreSheet().apply {
            this[ScoreCategory.YAHTZEE] = ScoreEntry(score = 0, locked = true)
        }

        val preview = previewScore(
            category = ScoreCategory.FULL_HOUSE,
            dice = listOf(6, 6, 6, 6, 6),
            scoreSheet = scoreSheet
        )

        assertEquals(YahtzeeConstants.FULL_HOUSE_SCORE, preview)
    }

    @Test
    fun smallStraightPreviewsThirtyUnderJokerRule() {
        val scoreSheet = createEmptyScoreSheet().apply {
            this[ScoreCategory.YAHTZEE] = ScoreEntry(score = 0, locked = true)
        }

        val preview = previewScore(
            category = ScoreCategory.SMALL_STRAIGHT,
            dice = listOf(6, 6, 6, 6, 6),
            scoreSheet = scoreSheet
        )

        assertEquals(YahtzeeConstants.SMALL_STRAIGHT_SCORE, preview)
    }

    @Test
    fun largeStraightPreviewsFortyUnderJokerRule() {
        val scoreSheet = createEmptyScoreSheet().apply {
            this[ScoreCategory.YAHTZEE] = ScoreEntry(score = 0, locked = true)
        }

        val preview = previewScore(
            category = ScoreCategory.LARGE_STRAIGHT,
            dice = listOf(6, 6, 6, 6, 6),
            scoreSheet = scoreSheet
        )

        assertEquals(YahtzeeConstants.LARGE_STRAIGHT_SCORE, preview)
    }
}