
package com.rekcode.yahtzee.api

import com.rekcode.yahtzee.game.GameModule

/**
 * Factory for creating instances of the Yahtzee game.
 *
 * This is the ONLY approved way to instantiate the game engine
 * from outside the module.
 *
 * Design Intent:
 * - Hides the concrete implementation (GameModule)
 * - Returns only the API interface (YahtzeeGame)
 * - Allows future swapping of implementations without breaking consumers
 *
 * Usage:
 * val game = createGame(1)
 *
 * @param numPlayers number of players in the game
 * @return YahtzeeGame API instance
 */
fun createGame(numPlayers: Int): YahtzeeGame {
    return GameModule(numPlayers)
}