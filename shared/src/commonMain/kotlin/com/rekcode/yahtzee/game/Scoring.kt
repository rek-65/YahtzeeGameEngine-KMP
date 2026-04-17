package com.rekcode.yahtzee.game

/**
 * Calculates the score for a given [ScoreCategory] based on the provided dice.
 *
 * This function is pure:
 * - No side effects
 * - No mutation
 * - Deterministic output
 *
 * This is the primary public entry point for all scoring logic in the engine.
 *
 * @param category The scoring category
 * @param dice A list of 5 dice values (expected range 1–6)
 *
 * @return The calculated score for the category
 */
internal fun calculateScore(category: ScoreCategory, dice: List<Int>): Int {
    val counts = dice.groupingBy { it }.eachCount()
    val sum = dice.sum()

    return when (category) {

        // --- Upper Section ---
        ScoreCategory.ONES -> dice.filter { it == 1 }.sum()
        ScoreCategory.TWOS -> dice.filter { it == 2 }.sum()
        ScoreCategory.THREES -> dice.filter { it == 3 }.sum()
        ScoreCategory.FOURS -> dice.filter { it == 4 }.sum()
        ScoreCategory.FIVES -> dice.filter { it == 5 }.sum()
        ScoreCategory.SIXES -> dice.filter { it == 6 }.sum()

        // --- Lower Section ---

        /**
         * Scores the sum of all dice if at least three dice have the same value.
         */
        ScoreCategory.THREE_OF_A_KIND ->
            if (counts.any { it.value >= 3 }) sum else 0

        /**
         * Scores the sum of all dice if at least four dice have the same value.
         */
        ScoreCategory.FOUR_OF_A_KIND ->
            if (counts.any { it.value >= 4 }) sum else 0

        /**
         * Scores [YahtzeeConstants.FULL_HOUSE_SCORE] if the dice contain exactly:
         * - One pair
         * - One three-of-a-kind
         *
         * Note: A four-of-a-kind does NOT count as a full house.
         */
        ScoreCategory.FULL_HOUSE ->
            if (counts.values.sorted() == listOf(2, 3))
                YahtzeeConstants.FULL_HOUSE_SCORE else 0

        /**
         * Scores [YahtzeeConstants.SMALL_STRAIGHT_SCORE] if the dice contain
         * a sequence of at least [YahtzeeConstants.SMALL_STRAIGHT_LENGTH]
         * consecutive numbers.
         */
        ScoreCategory.SMALL_STRAIGHT ->
            if (hasStraight(dice, YahtzeeConstants.SMALL_STRAIGHT_LENGTH))
                YahtzeeConstants.SMALL_STRAIGHT_SCORE else 0

        /**
         * Scores [YahtzeeConstants.LARGE_STRAIGHT_SCORE] if the dice contain
         * a sequence of [YahtzeeConstants.LARGE_STRAIGHT_LENGTH]
         * consecutive numbers.
         */
        ScoreCategory.LARGE_STRAIGHT ->
            if (hasStraight(dice, YahtzeeConstants.LARGE_STRAIGHT_LENGTH))
                YahtzeeConstants.LARGE_STRAIGHT_SCORE else 0

        /**
         * Scores [YahtzeeConstants.YAHTZEE_SCORE] if all dice have the same value.
         */
        ScoreCategory.YAHTZEE ->
            if (counts.any { it.value == YahtzeeConstants.YAHTZEE_DICE_COUNT })
                YahtzeeConstants.YAHTZEE_SCORE else 0

        /**
         * Scores the sum of all dice (always valid).
         */
        ScoreCategory.CHANCE -> sum
    }
}

/**
 * INTERNAL HELPER
 *
 * Determines whether the dice contain a straight of a given length.
 *
 * This function is intentionally private:
 * - It is an implementation detail of scoring
 * - It should not be accessed outside this file
 *
 * Duplicate dice values are ignored.
 *
 * Examples:
 * - [1,2,3,4,6] → true for length 4
 * - [2,3,4,5,6] → true for length 5
 *
 * @param dice Dice values
 * @param length Required straight length
 *
 * @return true if a straight of the given length exists
 */
private fun hasStraight(dice: List<Int>, length: Int): Boolean {
    val unique = dice.toSet().sorted()
    var streak = 1

    for (i in 1 until unique.size) {
        if (unique[i] == unique[i - 1] + 1) {
            streak++
            if (streak >= length) return true
        } else {
            streak = 1
        }
    }
    return false
}