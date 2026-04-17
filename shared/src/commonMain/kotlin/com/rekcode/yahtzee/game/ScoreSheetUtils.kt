
package com.rekcode.yahtzee.game

import com.rekcode.yahtzee.api.UpperBonusStatus

/**
 * Utility functions for score sheet operations.
 *
 * All functions are pure:
 * - No side effects
 * - No mutation of game state
 * - Deterministic output
 *
 * These utilities support preview, simulation, and delta
 * calculations without affecting live game state.
 */

/**
 * Creates a new empty score sheet.
 *
 * Each [ScoreCategory] is initialized with an unlocked [ScoreEntry]
 * containing a null score.
 *
 * @return MutableMap of [ScoreCategory] to empty [ScoreEntry]
 */
internal fun createEmptyScoreSheet(): MutableMap<ScoreCategory, ScoreEntry> {
    return ScoreCategory.entries.associateWith {
        ScoreEntry()
    }.toMutableMap()
}

/**
 * Calculates the potential score for a category based on the current dice and score sheet state.
 *
 * This function implements standard Yahtzee scoring rules alongside the Yahtzee Joker Rule.
 * If the YAHTZEE category is locked with a score of zero and the current dice constitute
 * a Yahtzee, the FULL_HOUSE, SMALL_STRAIGHT, and LARGE_STRAIGHT categories are awarded
 * their respective fixed scores regardless of the dice pattern.
 *
 * @param category The [ScoreCategory] being evaluated for a potential score.
 * @param dice The current dice values to be evaluated.
 * @param scoreSheet A map representing the current state of all [ScoreCategory] entries.
 * @return The calculated preview score for the category, or null if the category is already locked.
 */
internal fun previewScore(
    category: ScoreCategory,
    dice: List<Int>,
    scoreSheet: Map<ScoreCategory, ScoreEntry>
): Int? {
    val entry = scoreSheet[category]

    if (entry?.locked == true) return null

    val yahtzeeEntry = scoreSheet[ScoreCategory.YAHTZEE]
    val isCurrentRollYahtzee = dice.toSet().size == 1
    val yahtzeeScoredAsZero = yahtzeeEntry?.locked == true && yahtzeeEntry.score == 0
    val yahtzeeScoredAsFifty = yahtzeeEntry?.locked == true &&
            yahtzeeEntry.score == YahtzeeConstants.YAHTZEE_SCORE

    val matchingUpperCategory = when (dice.firstOrNull()) {
        1 -> ScoreCategory.ONES
        2 -> ScoreCategory.TWOS
        3 -> ScoreCategory.THREES
        4 -> ScoreCategory.FOURS
        5 -> ScoreCategory.FIVES
        6 -> ScoreCategory.SIXES
        else -> null
    }

    val matchingUpperCategoryLocked =
        matchingUpperCategory != null && scoreSheet[matchingUpperCategory]?.locked == true

    val jokerRuleApplies =
        isCurrentRollYahtzee && (yahtzeeScoredAsZero || (yahtzeeScoredAsFifty && matchingUpperCategoryLocked))

    if (jokerRuleApplies) {
        return when (category) {
            ScoreCategory.FULL_HOUSE -> YahtzeeConstants.FULL_HOUSE_SCORE
            ScoreCategory.SMALL_STRAIGHT -> YahtzeeConstants.SMALL_STRAIGHT_SCORE
            ScoreCategory.LARGE_STRAIGHT -> YahtzeeConstants.LARGE_STRAIGHT_SCORE
            else -> calculateScore(category, dice)
        }
    }

    return calculateScore(category, dice)
}

/**
 * Returns preview scores for all categories.
 *
 * Locked categories return null.
 *
 * @param dice Current dice values
 * @param scoreSheet Current score sheet state
 *
 * @return Map of [ScoreCategory] to preview score (null if locked)
 */
internal fun previewAllScores(
    dice: List<Int>,
    scoreSheet: Map<ScoreCategory, ScoreEntry>
): Map<ScoreCategory, Int?> {
    return ScoreCategory.entries.associateWith { category ->
        previewScore(category, dice, scoreSheet)
    }
}

/**
 * Returns the delta between current score and previewed score.
 *
 * Useful for AI decisions or UI highlighting.
 *
 * @param category The category being evaluated
 * @param dice Current dice values
 * @param player The current player state
 *
 * @return Score difference, or null if preview is invalid
 */
internal fun previewScoreDelta(
    category: ScoreCategory,
    dice: List<Int>,
    player: PlayerState
): Int? {
    val baseScore = previewScore(category, dice, player.scoreSheet)
        ?: return null

    return baseScore
}

/**
 * Returns the current Upper Section Bonus status for a player.
 *
 * Evaluates the player's current upper section score against
 * [YahtzeeConstants.UPPER_BONUS_THRESHOLD] and returns a complete
 * status summary without modifying any game state.
 *
 * @param player The current player state
 *
 * @return [UpperBonusStatus] containing current score, threshold,
 *         bonus amount, achievement status, and points still needed
 */
internal fun getUpperSectionBonusStatus(player: PlayerState): UpperBonusStatus {
    val current = player.upperSectionScore()
    return UpperBonusStatus(
        currentScore = current,
        threshold = YahtzeeConstants.UPPER_BONUS_THRESHOLD,
        bonusAmount = if (current >= YahtzeeConstants.UPPER_BONUS_THRESHOLD)
            YahtzeeConstants.UPPER_BONUS_AMOUNT else 0,
        achieved = current >= YahtzeeConstants.UPPER_BONUS_THRESHOLD,
        pointsNeeded = if (current >= YahtzeeConstants.UPPER_BONUS_THRESHOLD)
            0 else YahtzeeConstants.UPPER_BONUS_THRESHOLD - current
    )
}