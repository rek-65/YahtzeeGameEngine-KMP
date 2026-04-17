package com.rekcode.yahtzee.game

/**
 * Controls the flow of a Yahtzee game.
 *
 * Responsibilities:
 * - Manage player turns
 * - Coordinate dice rolls
 * - Handle scoring actions
 * - Enforce turn rules
 * - Determine game completion and winner
 *
 * This class does NOT handle UI.
 */
internal class YahtzeeGameController(numPlayers: Int) {

    /**
     * List of all players in the game.
     */
    private val players: List<PlayerState> = List(numPlayers) { PlayerState() }

    /**
     * Index of the current player.
     */
    private var currentPlayerIndex = 0
        private set

    /**
     * Convenience accessor for the active player.
     */
    private val currentPlayer: PlayerState
        get() = players[currentPlayerIndex]

    /**
     * Returns the index of the player whose turn is currently active.
     *
     * This is a controlled accessor used by the API layer (GameModule)
     * to avoid exposing internal state directly.
     *
     * @return Int index of the current player
     */
    fun getCurrentPlayerIndex(): Int = currentPlayerIndex

    /**
     * Returns the current player's dice values.
     *
     * This provides read-only access to dice for the API layer,
     * without exposing the PlayerState or dice internals.
     *
     * @return List<Int> representing the current dice values
     */
    fun getCurrentPlayerDice(): List<Int> = currentPlayer.diceState.dice

    /**
     * Indicates whether the current player can roll dice.
     *
     * A player can roll if they have remaining rolls in their turn.
     * This method provides controlled access for the API layer.
     *
     * @return true if the player can roll, false otherwise
     */
    fun canCurrentPlayerRoll(): Boolean = currentPlayer.canRoll()

    /**
     * Indicates whether the current player has rolled at least once this turn.
     *
     * Determined by checking if rollsRemaining is less than
     * [YahtzeeConstants.MAX_ROLLS_PER_TURN].
     * Provides controlled access for the API layer.
     *
     * @return true if the player has rolled, false otherwise
     */
    fun hasCurrentPlayerRolled(): Boolean = currentPlayer.hasRolled()

    /**
     * Returns the number of rolls remaining for the current player.
     *
     * A player starts each turn with [YahtzeeConstants.MAX_ROLLS_PER_TURN] rolls.
     * This value decreases as the player rolls dice during their turn.
     *
     * @return Int number of rolls remaining (0–[YahtzeeConstants.MAX_ROLLS_PER_TURN])
     */
    fun getCurrentPlayerRollsRemaining(): Int = currentPlayer.getRollsRemaining()

    /**
     * Returns the final score for a specific player.
     *
     * Provides controlled access to player scoring data without exposing
     * the internal PlayerState list.
     *
     * @param playerIndex Index of the player
     * @return Int final score including all bonuses
     */
    fun getPlayerFinalScore(playerIndex: Int): Int {
        return players[playerIndex].finalScore()
    }

    /**
     * Returns the total additional Yahtzee bonus points for a player.
     *
     * This is calculated as 100 points per additional Yahtzee beyond the first.
     *
     * @param playerIndex Index of the player
     * @return total additional Yahtzee bonus points
     */
    fun getPlayerAdditionalYahtzeeBonus(playerIndex: Int): Int {
        return players[playerIndex].extraYahtzees * YahtzeeConstants.YAHTZEE_BONUS_PER_EXTRA
    }

    /**
     * Sets the hold state of a die for the current player.
     *
     * Returns the updated list of hold states after the change.
     *
     * @param index Index of the die (0–[YahtzeeConstants.DICE_COUNT] minus 1)
     * @param hold True to hold the die, false to release it
     * @return Immutable list representing current hold states
     * @throws IndexOutOfBoundsException if index is invalid
     */
    fun setCurrentPlayerDieHold(index: Int, hold: Boolean): List<Boolean> {
        return currentPlayer.setDieHold(index, hold)
    }

    /**
     * Returns a read-only view of a player's score sheet.
     *
     * Prevents external modification while allowing the API layer
     * to display scores and game progress.
     *
     * @param playerIndex Index of the player
     * @return Map of [ScoreCategory] to [ScoreEntry]
     */
    fun getPlayerScoreSheet(playerIndex: Int): Map<ScoreCategory, ScoreEntry> {
        return players[playerIndex].scoreSheet
    }

    /**
     * Returns a list of scoring categories still available
     * for the current player.
     *
     * A category is available if it has not yet been scored (unlocked).
     *
     * @return List of available [ScoreCategory] values
     */
    fun getAvailableCategoriesForCurrentPlayer(): List<ScoreCategory> {
        return currentPlayer.getAvailableCategories()
    }

    /**
     * Computes preview scores for the current player based on current dice.
     *
     * Evaluates all score categories without modifying game state.
     *
     * @return Map of internal [ScoreCategory] to preview score (null if invalid)
     */
    fun previewCurrentPlayerScores(): Map<ScoreCategory, Int?> {
        val dice = currentPlayer.diceState.dice
        val sheet = currentPlayer.scoreSheet
        return previewAllScores(dice, sheet)
    }

    /**
     * Returns the total number of players in the game.
     *
     * The only approved way to query player count, ensuring the
     * internal player list remains encapsulated.
     *
     * @return Int number of players
     */
    fun getPlayerCount(): Int = players.size

    /**
     * Advances to the next player and resets their turn state.
     */
    fun nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        currentPlayer.resetTurn()
    }

    /**
     * Rolls dice for the current player if rolls remain.
     *
     * Rules enforced:
     * - Cannot roll more than [YahtzeeConstants.MAX_ROLLS_PER_TURN] per turn
     */
    fun rollCurrentPlayerDice() {
        if (currentPlayer.getRollsRemaining() > 0) {
            currentPlayer.diceState.rollDice()
            currentPlayer.decrementRolls()
        }
    }

    /**
     * Scores the current player's dice for the given category.
     *
     * Rules enforced:
     * - Cannot score before at least one roll
     * - Cannot score a locked category
     * - Handles Yahtzee bonus logic
     *
     * Ends the player's turn after scoring.
     *
     * @param category The [ScoreCategory] to score
     */
    fun scoreCurrentPlayer(category: ScoreCategory) {
        val player = currentPlayer

        if (player.getRollsRemaining() == YahtzeeConstants.MAX_ROLLS_PER_TURN) return

        val entry = player.scoreSheet[category]

        if (entry?.locked == true) return

        val diceValues = player.diceState.dice

        val counts = diceValues.groupingBy { it }.eachCount()
        val isYahtzeeRoll = counts.any { it.value == YahtzeeConstants.YAHTZEE_DICE_COUNT }

        if (isYahtzeeRoll) {
            val yahtzeeEntry = player.scoreSheet[ScoreCategory.YAHTZEE]

            if (yahtzeeEntry?.score == YahtzeeConstants.YAHTZEE_SCORE &&
                category != ScoreCategory.YAHTZEE) {
                player.extraYahtzees++
            }
        }

        val score = calculateScore(category, diceValues)

        player.setScore(category, score)

        nextPlayer()
    }

    /**
     * Checks whether all players have completed all categories.
     *
     * @return true if the game is finished
     */
    fun isGameOver(): Boolean {
        return players.all { player ->
            player.scoreSheet.values.all { it.locked }
        }
    }

    /**
     * Determines the winning player.
     *
     * @return Index of winning player, or null if game is not finished
     */
    fun getWinnerIndex(): Int? {
        if (!isGameOver()) return null

        return players
            .mapIndexed { index, player -> index to player.finalScore() }
            .maxByOrNull { it.second }
            ?.first
    }
}