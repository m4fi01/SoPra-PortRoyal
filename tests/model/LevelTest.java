package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for enum Level
 */
public class LevelTest {
    /**
     * testing the methode getAIPlayerName
     */
    @Test
    public void getAIPlayerName() {
        try {
            assertEquals(Level.HARD.getAIPlayerName(), "WhySoEZ");
            assertNotEquals(Level.MEDIUM.getAIPlayerName(), "Noobie");
            assertEquals(Level.EASY.getAIPlayerName(), "Noobie");
        }catch (UnsupportedOperationException  e){
            e.printStackTrace();
        }
    }

    /**
     * testing the values of an enum
     */
    @Test
    public void valueOf() {
        assertEquals(Level.valueOf("EASY"),Level.EASY);
        assertNotEquals(Level.valueOf("EASY"),Level.HARD);
        assertEquals(Level.valueOf("MEDIUM"),Level.MEDIUM);
    }
}