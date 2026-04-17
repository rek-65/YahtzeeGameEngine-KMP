
package com.rekcode.yahtzee.game

import com.rekcode.yahtzee.api.ScoreCategory as ApiCategory

/**
 * Maps API ScoreCategory to internal engine ScoreCategory.
 *
 * This is a critical boundary layer that:
 * - Prevents API types from leaking into the engine
 * - Allows internal enums to evolve independently
 * - Preserves strict encapsulation
 *
 * RULE:
 * Every API category MUST be explicitly mapped.
 */
internal fun ApiCategory.toInternal(): ScoreCategory {
    return when (this) {

        // Yahtzee Upper Section
        ApiCategory.ONES -> ScoreCategory.ONES
        ApiCategory.TWOS -> ScoreCategory.TWOS
        ApiCategory.THREES -> ScoreCategory.THREES
        ApiCategory.FOURS -> ScoreCategory.FOURS
        ApiCategory.FIVES -> ScoreCategory.FIVES
        ApiCategory.SIXES -> ScoreCategory.SIXES

        // Yahtzee Lower Section
        ApiCategory.THREE_OF_A_KIND -> ScoreCategory.THREE_OF_A_KIND
        ApiCategory.FOUR_OF_A_KIND -> ScoreCategory.FOUR_OF_A_KIND
        ApiCategory.FULL_HOUSE -> ScoreCategory.FULL_HOUSE
        ApiCategory.SMALL_STRAIGHT -> ScoreCategory.SMALL_STRAIGHT
        ApiCategory.LARGE_STRAIGHT -> ScoreCategory.LARGE_STRAIGHT
        ApiCategory.YAHTZEE -> ScoreCategory.YAHTZEE
        ApiCategory.CHANCE -> ScoreCategory.CHANCE
    }
}

/**
 * Maps internal engine ScoreCategory to API ScoreCategory.
 *
 * This ensures that internal engine types are never exposed directly
 * to consumers of the API.
 *
 * RULE:
 * Every internal category MUST be explicitly mapped.
 */
internal fun ScoreCategory.toApi(): com.rekcode.yahtzee.api.ScoreCategory {
    return when (this) {
        ScoreCategory.ONES -> com.rekcode.yahtzee.api.ScoreCategory.ONES
        ScoreCategory.TWOS -> com.rekcode.yahtzee.api.ScoreCategory.TWOS
        ScoreCategory.THREES -> com.rekcode.yahtzee.api.ScoreCategory.THREES
        ScoreCategory.FOURS -> com.rekcode.yahtzee.api.ScoreCategory.FOURS
        ScoreCategory.FIVES -> com.rekcode.yahtzee.api.ScoreCategory.FIVES
        ScoreCategory.SIXES -> com.rekcode.yahtzee.api.ScoreCategory.SIXES

        ScoreCategory.THREE_OF_A_KIND -> com.rekcode.yahtzee.api.ScoreCategory.THREE_OF_A_KIND
        ScoreCategory.FOUR_OF_A_KIND -> com.rekcode.yahtzee.api.ScoreCategory.FOUR_OF_A_KIND
        ScoreCategory.FULL_HOUSE -> com.rekcode.yahtzee.api.ScoreCategory.FULL_HOUSE
        ScoreCategory.SMALL_STRAIGHT -> com.rekcode.yahtzee.api.ScoreCategory.SMALL_STRAIGHT
        ScoreCategory.LARGE_STRAIGHT -> com.rekcode.yahtzee.api.ScoreCategory.LARGE_STRAIGHT
        ScoreCategory.YAHTZEE -> com.rekcode.yahtzee.api.ScoreCategory.YAHTZEE
        ScoreCategory.CHANCE -> com.rekcode.yahtzee.api.ScoreCategory.CHANCE
    }
}