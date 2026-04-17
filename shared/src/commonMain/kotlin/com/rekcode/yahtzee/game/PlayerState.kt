package com.rekcode.yahtzee.game

/**
 * Represents the full game state for a single player.
 *
 * Responsibilities:
 * - Manage dice state
 * - Track scoring across all categories
 * - Control turn mechanics (rolls remaining)
 * - Calculate totals and bonuses
 * - Provide safe preview and helper queries
 *
 * Encapsulation:
 * - Score sheet is read-only externally
 * - Rolls can only be modified through controlled functions
 */
internal class PlayerState {

    /**
     * The player's dice state.
     */
    val diceState = PlayerDiceState()

    /**
     * Public read-only view of the score sheet.
     */
    val scoreSheet: Map<ScoreCategory, ScoreEntry>
        get() = _scoreSheet

    /**
     * Number of rolls remaining in the current turn.
     *
     * Starts at [YahtzeeConstants.MAX_ROLLS_PER_TURN] and is decremented
     * as the player rolls. Fully encapsulated and cannot be accessed
     * directly outside this class.
     */
    private var rollsRemaining: Int = YahtzeeConstants.MAX_ROLLS_PER_TURN

    /**
     * Number of additional Yahtzees beyond the first.
     * Used to calculate Yahtzee bonus.
     */
    internal var extraYahtzees: Int = 0

    // --- Internal State ---

    /**
     * Internal mutable score sheet.
     */
    private val _scoreSheet = createEmptyScoreSheet()

    // --- Turn Management ---

    /**
     * Decrements rolls remaining if possible.
     */
    fun decrementRolls() {
        if (rollsRemaining > 0) {
            rollsRemaining--
        }
    }

    /**
     * Returns the number of rolls remaining in the current turn.
     *
     * @return Int rolls remaining (0–[YahtzeeConstants.MAX_ROLLS_PER_TURN])
     */
    fun getRollsRemaining(): Int = rollsRemaining

    /**
     * Sets the hold state of a die for this player.
     *
     * Returns the updated list of hold states after the change.
     *
     * @param index Index of the die (0–4)
     * @param hold True to hold the die, false to release it
     * @return Immutable list representing current hold states
     * @throws IndexOutOfBoundsException if index is invalid
     */
    fun setDieHold(
        index: Int,
        hold: Boolean
    ): List<Boolean> {
        val currentlyHeld = diceState.diceLocked[index]

        if (currentlyHeld != hold) {
            diceState.toggleLock(index)
        }

        return diceState.diceLocked
    }

    /**
     * Resets the player's turn.
     *
     * - Rolls reset to [YahtzeeConstants.MAX_ROLLS_PER_TURN]
     * - All dice locks cleared
     */
    fun resetTurn() {
        rollsRemaining = YahtzeeConstants.MAX_ROLLS_PER_TURN
        diceState.resetLocks()
    }

    // --- Scoring Actions ---

    /**
     * Assigns a score to a category and locks it.
     *
     * @param category The scoring category
     * @param score The calculated score
     */
    fun setScore(category: ScoreCategory, score: Int) {
        _scoreSheet[category] = ScoreEntry(score, true)
    }

    /**
     * Loads a full score sheet (used for testing or persistence).
     *
     * @param newSheet The new score sheet to apply
     */
    internal fun loadScoreSheet(newSheet: Map<ScoreCategory, ScoreEntry>) {
        _scoreSheet.clear()
        _scoreSheet.putAll(newSheet)
    }

    // --- Score Calculations ---

    /**
     * Calculates the total score without bonuses.
     *
     * @return Int sum of all scored categories
     */
    fun totalScore(): Int {
        return scoreSheet.values.sumOf { it.score ?: 0 }
    }

    /**
     * Calculates the final score including all bonuses.
     *
     * @return Int total score including upper bonus and Yahtzee bonuses
     */
    fun finalScore(): Int {
        return totalScore() + upperBonus() + yahtzeeBonus()
    }

    /**
     * Calculates the total of the upper section (ONES–SIXES).
     *
     * @return Int sum of all upper section scored categories
     */
    fun upperSectionScore(): Int {
        return listOf(
            ScoreCategory.ONES,
            ScoreCategory.TWOS,
            ScoreCategory.THREES,
            ScoreCategory.FOURS,
            ScoreCategory.FIVES,
            ScoreCategory.SIXES
        ).sumOf { scoreSheet[it]?.score ?: 0 }
    }

    /**
     * Calculates the upper section bonus.
     *
     * Bonus of [YahtzeeConstants.UPPER_BONUS_AMOUNT] is awarded if
     * upper section score >= [YahtzeeConstants.UPPER_BONUS_THRESHOLD].
     *
     * @return Int bonus amount or zero if threshold not reached
     */
    fun upperBonus(): Int {
        return if (upperSectionScore() >= YahtzeeConstants.UPPER_BONUS_THRESHOLD)
            YahtzeeConstants.UPPER_BONUS_AMOUNT else 0
    }

    /**
     * Calculates Yahtzee bonus from additional Yahtzees.
     *
     * Rules:
     * - First Yahtzee must be scored as [YahtzeeConstants.YAHTZEE_SCORE]
     * - Each additional Yahtzee adds [YahtzeeConstants.YAHTZEE_BONUS_PER_EXTRA] points
     *
     * @return Int total Yahtzee bonus points
     */
    fun yahtzeeBonus(): Int {
        val yahtzeeEntry = scoreSheet[ScoreCategory.YAHTZEE]

        if (yahtzeeEntry?.score != YahtzeeConstants.YAHTZEE_SCORE) return 0

        return extraYahtzees * YahtzeeConstants.YAHTZEE_BONUS_PER_EXTRA
    }

    // --- Preview & Queries ---

    /**
     * Returns a preview score for a category without modifying state.
     *
     * Rules:
     * - Returns null if category is already locked
     * - Returns null if player has not rolled yet
     *
     * @param category The scoring category to preview
     * @return Int preview score or null if unavailable
     */
    fun previewScore(category: ScoreCategory): Int? {
        val entry = scoreSheet[category]

        if (entry?.locked == true) return null
        if (rollsRemaining == YahtzeeConstants.MAX_ROLLS_PER_TURN) return null

        return calculateScore(category, diceState.dice)
    }

    /**
     * Returns all categories that have not yet been scored.
     *
     * @return List of available [ScoreCategory] values
     */
    fun getAvailableCategories(): List<ScoreCategory> {
        return scoreSheet
            .filter { !it.value.locked }
            .keys
            .toList()
    }

    /**
     * Returns whether a category can be scored.
     *
     * @param category The scoring category to check
     * @return true if the category is available for scoring
     */
    fun canScore(category: ScoreCategory): Boolean {
        val entry = scoreSheet[category]
        return entry?.locked != true
    }

    /**
     * Returns true if the player has rolled at least once this turn.
     *
     * @return true if at least one roll has been used
     */
    fun hasRolled(): Boolean {
        return rollsRemaining < YahtzeeConstants.MAX_ROLLS_PER_TURN
    }

    /**
     * Returns true if the player can still roll.
     *
     * @return true if rolls remaining is greater than zero
     */
    fun canRoll(): Boolean {
        return rollsRemaining > 0
    }
}