package model;

/**
 * AIPlayer provides necessary extensions to the Player class to allow artificial intelligence to control a particular
 * player of a game of Port Royal.
 * <p>
 * AIPlayer implements the Serializable-interface (inherited property) to allow the current game of Port Royal to be
 * stored by serialising the GameLog-object that represents it.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see Player
 * @see Level
 * @see GameLog
 */
public class AIPlayer extends Player {
	/**
	 * Indicates the minimum time in milliseconds that an AIPlayer takes to perform a single action. This is useful to
	 * track a game that is only played by the AI.
	 */
	private int simulationSpeed;

	/**
	 * Indicates the level of difficulty offered by this particular AIPlayer.
	 */
	private final Level level;

	/**
	 * Constructs a new player controlled by artificial intelligence with the specified, final level and a default
	 * simulation speed of 2000 milliseconds.
	 *
	 * @param level The desired level of difficulty for this artificial intelligence.
	 * @throws NullPointerException if the specified level of difficulty for this artificial intelligence is
	 * 						        {@code null}.
	 */
	public AIPlayer(Level level, String name) {
		super(name);
		this.simulationSpeed = 2000;
		this.level = level;
	}

	/**
	 *  Copy constructor for logging. Creates a log friendly deep alike copy of the given AIPlayer
	 * @param player The AIPlayer to copy for logging
	 */
	public AIPlayer(AIPlayer player) {
		super(player);
		this.simulationSpeed = player.simulationSpeed;
		this.level = player.level;
	}

	/**
	 * Sets the time in milliseconds that this AIPlayer should take to perform a single action. If the specified
	 * simulation speed is less than 0, the actual simulation speed is set to 0.
	 *
	 * @param simulationSpeed The time in milliseconds that this AIPlayer should take to perform a single action.
	 */
	public void setSimulationSpeed(int simulationSpeed) {
		this.simulationSpeed = Math.max(0, simulationSpeed);
	}

	/**
	 * Returns the current time in milliseconds this AIPlayer takes to perform a single action.
	 *
	 * @return The current time in milliseconds this AIPlayer takes to perform a single action.
	 */
	public int getSimulationSpeed() {
		return this.simulationSpeed;
	}

	/**
	 * Returns the level of difficulty of this AIPlayer.
	 *
	 * @return The level of difficulty of this AIPlayer.
	 */
	public Level getLevel() {
		return this.level;
	}
}
