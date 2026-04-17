package com.rekcode.yahtzee.game

/**
 * Represents the dice state for a single player in a Yahtzee game.
 *
 * Responsibilities:
 * - Maintain current dice values ([YahtzeeConstants.MIN_DIE_VALUE]–[YahtzeeConstants.MAX_DIE_VALUE])
 * - Track which dice are locked between rolls
 * - Enforce encapsulation of dice and lock state
 *
 * External code can:
 * - Read dice values
 * - Read lock state
 *
 * External code cannot:
 * - Modify dice directly
 * - Modify locks directly
 *
 * All mutations must go through provided functions.
 *
 * @param dice Initial dice values (default: all [YahtzeeConstants.MIN_DIE_VALUE])
 * @param diceLocked Initial lock state (default: all unlocked)
 */
internal class PlayerDiceState(
    dice: MutableList<Int> = MutableList(YahtzeeConstants.DICE_COUNT) { YahtzeeConstants.MIN_DIE_VALUE },
    diceLocked: MutableList<Boolean> = MutableList(YahtzeeConstants.DICE_COUNT) { false }
) {

    /**
     * Public read-only view of dice values.
     *
     * Each value is guaranteed to be between [YahtzeeConstants.MIN_DIE_VALUE]
     * and [YahtzeeConstants.MAX_DIE_VALUE].
     */
    val dice: List<Int> get() = _dice

    /**
     * Public read-only view of dice lock states.
     *
     * True = locked (die will not reroll)
     * False = unlocked
     */
    val diceLocked: List<Boolean> get() = _diceLocked

    // --- Internal State ---

    /**
     * Internal mutable dice storage.
     */
    private val _dice = dice

    /**
     * Internal mutable lock state storage.
     */
    private val _diceLocked = diceLocked

    // --- Behavior ---

    /**
     * Rolls all unlocked dice.
     *
     * Each unlocked die is assigned a random value between
     * [YahtzeeConstants.MIN_DIE_VALUE] and [YahtzeeConstants.MAX_DIE_VALUE].
     * Locked dice retain their current values.
     */
    fun rollDice() {
        _dice.forEachIndexed { index, _ ->
            if (!_diceLocked[index]) {
                _dice[index] = (YahtzeeConstants.MIN_DIE_VALUE..YahtzeeConstants.MAX_DIE_VALUE).random()
            }
        }
    }

    /**
     * Toggles the lock state of a die.
     *
     * @param index Index of the die (0–[YahtzeeConstants.DICE_COUNT] minus 1)
     * @throws IndexOutOfBoundsException if index is invalid
     */
    fun toggleLock(index: Int) {
        _diceLocked[index] = !_diceLocked[index]
    }

    /**
     * Resets all dice to an unlocked state.
     *
     * Typically called at the start of a new turn.
     */
    fun resetLocks() {
        for (index in _diceLocked.indices) {
            _diceLocked[index] = false
        }
    }
}