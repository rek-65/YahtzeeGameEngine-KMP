
package com.rekcode.yahtzee.game

/**
 * Internal engine constants for the Yahtzee game.
 *
 * These constants are strictly internal to the engine and must never
 * be exposed through the public API.
 *
 * Design Rules:
 * - All numeric literals used in engine logic must reference this object
 * - No magic numbers are permitted anywhere in the game package
 * - Constants are named by PURPOSE, not by value
 */
internal object YahtzeeConstants {

    // --- Dice ---

    /** Total number of dice in a Yahtzee game */
    const val DICE_COUNT = 5

    /** Minimum face value of a single die */
    const val MIN_DIE_VALUE = 1

    /** Maximum face value of a single die */
    const val MAX_DIE_VALUE = 6

    // --- Turn ---

    /** Maximum number of rolls allowed per turn */
    const val MAX_ROLLS_PER_TURN = 3

    // --- Scoring ---

    /** Number of matching dice required for a Yahtzee */
    const val YAHTZEE_DICE_COUNT = 5

    /** Number of consecutive dice required for a Small Straight */
    const val SMALL_STRAIGHT_LENGTH = 4

    /** Number of consecutive dice required for a Large Straight */
    const val LARGE_STRAIGHT_LENGTH = 5

    // --- Upper Section ---

    /** Minimum upper section score required to earn the bonus */
    const val UPPER_BONUS_THRESHOLD = 63

    /** Bonus points awarded for reaching the upper section threshold */
    const val UPPER_BONUS_AMOUNT = 35

    // --- Fixed Category Scores ---

    /** Fixed score awarded for a Full House */
    const val FULL_HOUSE_SCORE = 25

    /** Fixed score awarded for a Small Straight */
    const val SMALL_STRAIGHT_SCORE = 30

    /** Fixed score awarded for a Large Straight */
    const val LARGE_STRAIGHT_SCORE = 40

    /** Fixed score awarded for a Yahtzee */
    const val YAHTZEE_SCORE = 50

    /** Bonus points awarded for each additional Yahtzee beyond the first */
    const val YAHTZEE_BONUS_PER_EXTRA = 100
}