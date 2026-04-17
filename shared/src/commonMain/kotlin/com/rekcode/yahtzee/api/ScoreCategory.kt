
package com.rekcode.yahtzee.api

/**
 * Public scoring categories exposed by the Yahtzee API.
 *
 * This enum defines all valid scoring options available to consumers
 * of the engine.
 *
 * API CONTRACT RULES:
 * - This enum is part of the public API surface
 * - Do NOT rename or remove entries after release
 * - New entries can only be added with versioning consideration
 *
 * Internal engine logic uses a separate enum to preserve encapsulation.
 */
enum class ScoreCategory {

    ONES,
    TWOS,
    THREES,
    FOURS,
    FIVES,
    SIXES,

    THREE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    SMALL_STRAIGHT,
    LARGE_STRAIGHT,
    YAHTZEE,
    CHANCE
}