
package com.rekcode.yahtzee.api

/**
 * ============================================================
 * API SURFACE LOCK — DO NOT MODIFY WITHOUT VERSION BUMP
 * ============================================================
 *
 * PURPOSE:
 * This file enforces a compile-time contract for the public API.
 *
 * If any of these signatures change, the build will fail,
 * forcing a conscious version update.
 *
 * This protects:
 * - Binary compatibility
 * - External consumers
 * - Published artifacts (JAR / Maven)
 *
 * RULES:
 * - Only reference PUBLIC API types here
 * - DO NOT use internal/game package types
 * - Update ONLY when intentionally changing API
 *
 * VERSION: 1.0.0
 */
@Suppress("unused")
internal object ApiSurfaceLock {
    /**
     * Compile-time lock for factory creation signature.
     */
    val createGameRef: (Int) -> YahtzeeGame = ::createGame

    /**
     * Compile-time lock for dice roll action.
     */
    val rollDiceRef: (YahtzeeGame) -> Unit = { it.rollDice() }

    /**
     * Compile-time lock for scoring action.
     */
    val scoreRef: (YahtzeeGame, ScoreCategory) -> Unit = { g, c -> g.score(c) }

    /**
     * Compile-time lock for next-player action.
     */
    val nextPlayerRef: (YahtzeeGame) -> Unit = { it.nextPlayer() }

    /**
     * Compile-time lock for current-player index access.
     */
    val getCurrentPlayerIndexRef: (YahtzeeGame) -> Int = { it.getCurrentPlayerIndex() }

    /**
     * Compile-time lock for current dice access.
     */
    val getCurrentDiceRef: (YahtzeeGame) -> List<Int> = { it.getCurrentDice() }

    /**
     * Compile-time lock for roll permission query.
     */
    val canRollRef: (YahtzeeGame) -> Boolean = { it.canRoll() }

    /**
     * Compile-time lock for rolled-state query.
     */
    val hasRolledRef: (YahtzeeGame) -> Boolean = { it.hasRolled() }

    /**
     * Compile-time lock for remaining-rolls query.
     */
    val getRollsRemainingRef: (YahtzeeGame) -> Int = { it.getRollsRemaining() }

    /**
     * Compile-time lock for game-over query.
     */
    val isGameOverRef: (YahtzeeGame) -> Boolean = { it.isGameOver() }

    /**
     * Compile-time lock for winner query.
     */
    val getWinnerIndexRef: (YahtzeeGame) -> Int? = { it.getWinnerIndex() }

    /**
     * Compile-time lock for player-count query.
     */
    val getPlayerCountRef: (YahtzeeGame) -> Int = { it.getPlayerCount() }

    /**
     * Compile-time lock for player final score query.
     */
    val getPlayerFinalScoreRef: (YahtzeeGame, Int) -> Int =
        { g, i -> g.getPlayerFinalScore(i) }

    /**
     * Compile-time lock for score sheet query.
     */
    val getScoreSheetRef: (YahtzeeGame, Int) -> List<ScoreSheetItem> =
        { g, i -> g.getScoreSheet(i) }

    /**
     * Compile-time lock for upper section bonus status query.
     */
    val getUpperSectionBonusStatusRef: (YahtzeeGame, Int) -> UpperBonusStatus =
        { g, i -> g.getUpperSectionBonusStatus(i) }

    /**
     * Compile-time lock for additional Yahtzee bonus query.
     */
    val getAdditionalYahtzeeBonusRef: (YahtzeeGame, Int) -> Int =
        { g, i -> g.getAdditionalYahtzeeBonus(i) }
}