package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * test Class for the model class AIPlayer
 */
public class AIPlayerTest {
    /**
     * Creates and copies an AI player to test the copy constructor.
     */
    @Test
    public void copyConstructor() {
        AIPlayer aiPlayer = new AIPlayer(Level.EASY,"Bob");
        aiPlayer.setSimulationSpeed(1000);

        AIPlayer copiedAIPlayer = new AIPlayer(aiPlayer);
        assertEquals(aiPlayer.getLevel(), copiedAIPlayer.getLevel());
        assertEquals(aiPlayer.getSimulationSpeed(), copiedAIPlayer.getSimulationSpeed());
    }

    /**
     * testing both methods setSimulationSpeed() and getSimulationSpeed()
     */
    @Test
    public void setSimulationSpeed() {
        AIPlayer aiPlayer = new AIPlayer(Level.HARD,"Bob");
        aiPlayer.setSimulationSpeed(1000);

        assertEquals(aiPlayer.getSimulationSpeed(),1000);
    }

    /**
     * testing methode getLevel()
     */
    @Test
    public void getLevel() {
        AIPlayer aiPlayer = new AIPlayer(Level.HARD,"Bob");

        assertEquals(aiPlayer.getLevel(),Level.HARD);
    }
}