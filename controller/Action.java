package controller;

/**
 * Action is an enumeration of all the actions a player can make. It is used for the action log
 */
public enum Action {
    /**
     * Is used if a new game has started
     */
    START_GAME,

    /**
     * Is used if the discover phase started
     */
    START_DISCOVER_PHASE,

    /**
     * Is used if the trade and hire phase is started
     */
    START_TRADE_AND_HIRE_PHASE,

    /**
     * Is used if a player started an expedition
     */
    START_EXPEDITION,

    /**
     * Is used if a player drew a card
     */
    DRAW_CARD,

    /**
     * Is used if a player repelled a ship
     */
    REPEL_SHIP,

    /**
     * Is used if a player traded a ship card
     */
    TRADE,

    /**
     * Is used if a player hired a person card
     */
    HIRE;
}
