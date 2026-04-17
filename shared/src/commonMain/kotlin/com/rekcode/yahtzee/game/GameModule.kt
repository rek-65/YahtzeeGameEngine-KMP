package com.rekcode.yahtzee.game

import com.rekcode.yahtzee.api.ScoreCategory
import com.rekcode.yahtzee.api.ScoreSheetItem
import com.rekcode.yahtzee.api.UpperBonusStatus
import com.rekcode.yahtzee.api.YahtzeeGame

/**
 * Yahtzee Game Module
 *
 * Version: 1.1.0
 *
 * Public API Stability:
 * - Backward compatibility is guaranteed for all public API methods
 * - Internal engine implementation may change without notice
 *
 * This class provides the internal implementation of the YahtzeeGame API.
 * It is not intended for direct use and must be instantiated via the API factory.
 */
internal class GameModule(numPlayers: Int) : YahtzeeGame {

    private val controller = YahtzeeGameController(numPlayers)

    /**
     * Returns the index of the current player.
     *
     * @return current player index
     */
    override fun getCurrentPlayerIndex(): Int =
        controller.getCurrentPlayerIndex()

    /**
     * Returns the current dice values.
     *
     * @return immutable list of dice values
     */
    override fun getCurrentDice(): List<Int> =
        controller.getCurrentPlayerDice()

    /**
     * Indicates whether the current player can roll.
     *
     * @return true if rolling is allowed, false otherwise
     */
    override fun canRoll(): Boolean =
        controller.canCurrentPlayerRoll()

    /**
     * Indicates whether the current player has rolled during this turn.
     *
     * @return true if the player has rolled, false otherwise
     */
    override fun hasRolled(): Boolean =
        controller.hasCurrentPlayerRolled()

    /**
     * Returns the number of rolls remaining for the current player.
     *
     * @return remaining roll count
     */
    override fun getRollsRemaining(): Int =
        controller.getCurrentPlayerRollsRemaining()

    /**
     * Indicates whether the game has completed.
     *
     * @return true if the game is over, false otherwise
     */
    override fun isGameOver(): Boolean =
        controller.isGameOver()

    /**
     * Returns the index of the winning player.
     *
     * @return winner index, or null if the game is not finished
     */
    override fun getWinnerIndex(): Int? =
        controller.getWinnerIndex()

    /**
     * Returns the total number of players in the game.
     *
     * @return number of players
     */
    override fun getPlayerCount(): Int =
        controller.getPlayerCount()

    /**
     * Returns the final score for the specified player.
     *
     * Includes base score, upper section bonus, and Yahtzee bonuses.
     *
     * @param playerIndex index of the player
     * @return total final score
     */
    override fun getPlayerFinalScore(playerIndex: Int): Int =
        controller.getPlayerFinalScore(playerIndex)

    /**
     * Returns the final score for the specified player.
     *
     * @param playerIndex index of the player
     * @return total final score
     */
    fun getPlayerScore(playerIndex: Int): Int =
        getPlayerFinalScore(playerIndex)

    /**
     * Returns a UI-safe representation of a player's score sheet.
     *
     * Internal score categories are mapped to API categories.
     *
     * @param playerIndex index of the player
     * @return list of score sheet items
     */
    override fun getScoreSheet(playerIndex: Int): List<ScoreSheetItem> {

        val scoreSheet = controller.getPlayerScoreSheet(playerIndex)

        return com.rekcode.yahtzee.game.ScoreCategory.entries.map { internalCategory ->

            val entry = scoreSheet[internalCategory]

            ScoreSheetItem(
                category = internalCategory.toApi(),
                displayName = internalCategory.name.replace("_", " "),
                score = entry?.score,
                isLocked = entry?.locked == true
            )
        }
    }

    /**
     * Returns preview scores for the current player based on current dice.
     *
     * @return list of score sheet items containing preview scores
     */
    override fun previewScores(): List<ScoreSheetItem> {

        val playerIndex = controller.getCurrentPlayerIndex()

        val scoreSheet = controller.getPlayerScoreSheet(playerIndex)

        val previewMap = controller.previewCurrentPlayerScores()

        return com.rekcode.yahtzee.game.ScoreCategory.entries.map { internalCategory ->

            val entry = scoreSheet[internalCategory]

            val preview = previewMap[internalCategory]

            ScoreSheetItem(
                category = internalCategory.toApi(),
                displayName = internalCategory.name.replace("_", " "),
                score = preview,
                isLocked = entry?.locked == true
            )
        }
    }

    /**
     * Returns the current Upper Section Bonus status for the specified player.
     *
     * Delegates to the internal score sheet utility without exposing
     * internal engine types to API consumers.
     *
     * @param playerIndex index of the player
     * @return [UpperBonusStatus] containing current score, threshold,
     *         bonus amount, achievement status, and points still needed
     */
    override fun getUpperSectionBonusStatus(playerIndex: Int): UpperBonusStatus {
        val scoreSheet = controller.getPlayerScoreSheet(playerIndex)
        val simulatedPlayer = PlayerState().apply {
            loadScoreSheet(scoreSheet)
        }
        return getUpperSectionBonusStatus(simulatedPlayer)
    }

    override fun getAdditionalYahtzeeBonus(playerIndex: Int): Int {
        return controller.getPlayerAdditionalYahtzeeBonus(playerIndex)
    }

    /**
     * Rolls the dice for the current player if rolls remain.
     */
    override fun rollDice() {
        controller.rollCurrentPlayerDice()
    }

    /**
     * Sets whether a die is held (excluded from rolling) for the current player.
     *
     * @param index index of the die (0–4)
     * @param hold true to hold the die, false to release it
     * @return immutable list representing current hold states
     * @throws IndexOutOfBoundsException if index is invalid
     */
    override fun setDieHold(index: Int, hold: Boolean): List<Boolean> {
        return controller.setCurrentPlayerDieHold(index, hold)
    }

    /**
     * Scores the current player's dice using the specified category.
     *
     * @param category API score category
     */
    override fun score(category: ScoreCategory) {
        val internalCategory = category.toInternal()
        controller.scoreCurrentPlayer(internalCategory)
    }

    /**
     * Advances the game to the next player.
     *
     * Typically invoked automatically after scoring.
     */
    override fun nextPlayer() {
        controller.nextPlayer()
    }
}