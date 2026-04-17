
package com.rekcode.yahtzee.game

/**
 * Represents a single score entry for a Yahtzee category.
 *
 * @property score The score assigned to the category.
 * - `null` indicates the category has not yet been scored.
 *
 * @property locked Indicates whether the category has been finalized.
 * - `true` means the score is permanent and cannot be changed.
 * - `false` means the category is still available for scoring.
 *
 * Design Notes:
 * - A category can technically have a score of 0 and still be locked.
 * - `score == null` is the only reliable indicator of "not yet scored".
 */
internal data class ScoreEntry(
    val score: Int? = null,
    val locked: Boolean = false
)