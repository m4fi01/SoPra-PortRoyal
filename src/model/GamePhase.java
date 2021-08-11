package model;

/**
 * GamePhase provides the two phases of the game as enum, to specify the current game phase.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see Game
 */
public enum GamePhase {
    /**
     * Represents the discover phase.
     */
    DISCOVER,

    /**
     * Represents the trade and hire phase.
     */
    TRADE_AND_HIRE;
}
