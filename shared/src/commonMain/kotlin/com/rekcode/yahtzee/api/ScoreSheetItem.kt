package com.rekcode.yahtzee.api

/**
 * UI-safe representation of a single score category.
 *
 * This class acts as a Data Transfer Object (DTO) between the game engine
 * and any external layer (UI, tests, etc.).
 *
 * It is intentionally immutable and contains only read-only data.
 *
 * Design Goals:
 * - Prevent exposure of internal engine structures (e.g., ScoreEntry)
 * - Provide a stable API contract for UI rendering
 * - Ensure no mutation of game state outside the engine
 *
 * @property category The score category (enum, safe to expose)
 * @property displayName Human-readable name for UI display
 * @property score The scored value (null if not yet scored)
 * @property isLocked Whether this category has been finalized
 */
data class ScoreSheetItem(
    val category: ScoreCategory,
    val displayName: String,
    val score: Int?,
    val isLocked: Boolean
)