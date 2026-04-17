
package com.rekcode.yahtzee.api

import com.rekcode.yahtzee.api.ScoreCategory
import com.rekcode.yahtzee.api.ScoreSheetItem
import com.rekcode.yahtzee.api.UpperBonusStatus

/**
 * Public API contract for the Yahtzee engine.
 *
 * This interface defines ALL operations that external consumers
 * are allowed to perform on the game engine.
 *
 * Purpose:
 * - Locks the API surface
 * - Prevents accidental exposure of internal logic
 * - Enables safe future refactoring
 *
 * Implementations must guarantee:
 * - Encapsulation of internal state
 * - Deterministic scoring behavior
 * - Safe multi-player handling
 */
interface YahtzeeGame {

    // --- Game Actions ---

    /**
     * Rolls dice for the current player if rolls remain.
     */
    fun rollDice()

    /**
     * Sets whether a die is held (excluded from rolling) for the current player.
     *
     * Returns the updated list of hold states after the change.
     *
     * @param index Index of the die (0–4)
     * @param hold True to hold the die, false to release it
     * @return Immutable list representing current hold states
     * @throws IndexOutOfBoundsException if index is invalid
     */
    fun setDieHold(index: Int, hold: Boolean): List<Boolean>

    /**
     * Scores the current player's dice for a given category.
     *
     * @param category scoring category
     */
    fun score(category: ScoreCategory)

    /**
     * Advances to the next player.
     */
    fun nextPlayer()

    // --- Game State ---

    /**
     * @return index of the current player
     */
    fun getCurrentPlayerIndex(): Int

    /**
     * @return current dice values
     */
    fun getCurrentDice(): List<Int>

    /**
     * @return true if current player can roll
     */
    fun canRoll(): Boolean

    /**
     * @return true if current player has rolled this turn
     */
    fun hasRolled(): Boolean

    /**
     * @return remaining rolls for current player
     */
    fun getRollsRemaining(): Int

    /**
     * @return true if game is complete
     */
    fun isGameOver(): Boolean

    /**
     * @return winner index or null if game not finished
     */
    fun getWinnerIndex(): Int?

    /**
     * Returns total number of players.
     */
    fun getPlayerCount(): Int

    /**
     * Returns final score for a given player.
     *
     * @param playerIndex index of player
     */
    fun getPlayerFinalScore(playerIndex: Int): Int

    /**
     * Returns a UI-safe score sheet.
     *
     * @param playerIndex index of player
     */
    fun getScoreSheet(playerIndex: Int): List<ScoreSheetItem>

    /**
     * Returns all possible scores for the current dice roll.
     *
     * Pure function:
     * - Does NOT modify game state
     * - Does NOT lock categories
     */
    fun previewScores(): List<ScoreSheetItem>

    /**
     * Returns the current Upper Section Bonus status for the specified player.
     *
     * Evaluates the player's current upper section score against the bonus
     * threshold and returns a complete status summary.
     *
     * Pure function:
     * - Does NOT modify game state
     * - Does NOT lock categories
     *
     * @param playerIndex index of the player
     * @return [UpperBonusStatus] containing current score, threshold,
     *         bonus amount, achievement status, and points still needed
     */
    fun getUpperSectionBonusStatus(playerIndex: Int): UpperBonusStatus

    /**
     * Returns the total additional Yahtzee bonus points earned by the player.
     *
     * This value represents ONLY the accumulated 100-point bonuses
     * from additional Yahtzees beyond the first (if applicable).
     *
     * Does NOT include:
     * - base category scores
     * - upper section bonus
     *
     * @param playerIndex index of the player
     * @return total additional Yahtzee bonus points
     */
    fun getAdditionalYahtzeeBonus(playerIndex: Int): Int
}