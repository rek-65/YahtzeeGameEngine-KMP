
package com.rekcode.yahtzee.game

/**
 * Internal scoring categories.
 *
 * Not exposed outside the engine.
 * All scoring interactions must go through the API layer.
 */
internal enum class ScoreCategory(val displayName: String) {

    // --- Upper Section ---

    /** Sum of all dice showing 1 */
    ONES("Ones"),

    /** Sum of all dice showing 2 */
    TWOS("Twos"),

    /** Sum of all dice showing 3 */
    THREES("Threes"),

    /** Sum of all dice showing 4 */
    FOURS("Fours"),

    /** Sum of all dice showing 5 */
    FIVES("Fives"),

    /** Sum of all dice showing 6 */
    SIXES("Sixes"),

    // --- Lower Section ---

    /** At least three dice the same; score = sum of all dice */
    THREE_OF_A_KIND("3 of a Kind"),

    /** At least four dice the same; score = sum of all dice */
    FOUR_OF_A_KIND("4 of a Kind"),

    /** Three of one number and two of another; fixed score */
    FULL_HOUSE("Full House"),

    /** Sequence of four consecutive numbers; fixed score */
    SMALL_STRAIGHT("Small Straight"),

    /** Sequence of five consecutive numbers; fixed score */
    LARGE_STRAIGHT("Large Straight"),

    /** All five dice the same; fixed score */
    YAHTZEE("Yahtzee"),

    /** Any combination; score = sum of all dice */
    CHANCE("Chance")
}