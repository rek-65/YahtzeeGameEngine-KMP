
package com.rekcode.yahtzee.api

/**
 * Represents the current state of the Upper Section Bonus for a player.
 *
 * This is a read-only data transfer object intended for use by
 * consumers of the Yahtzee engine API, such as UI layers and
 * external integrations.
 *
 * The Upper Section Bonus is awarded when the combined score of
 * ONES through SIXES reaches [YahtzeeConstants.UPPER_BONUS_THRESHOLD].
 *
 * @property currentScore The player's current upper section score
 * @property threshold The score required to earn the bonus
 * @property bonusAmount The bonus awarded upon reaching the threshold
 * @property achieved Whether the bonus has been earned
 * @property pointsNeeded Points still required to reach the threshold,
 *           zero if already achieved
 */
data class UpperBonusStatus(
    val currentScore: Int,
    val threshold: Int,
    val bonusAmount: Int,
    val achieved: Boolean,
    val pointsNeeded: Int
)