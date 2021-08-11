package controller;

import model.GameLog;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * IOController provides all the necessary methods for loading data from and saving data as files as required in
 * different contexts.
 *
 * @author Thomas Alexander Hövelmann
 *
 * @see GameController
 * @see GameLog
 */
public class IOController {
	/**
	 * The coordinator GameController of this IOController.
	 */
	private GameController gameController;

	/**
	 * Specifies the relative path of the CSV-file containing the high score list.
	 */
	public static final String HIGH_SCORE_PATH = "./src/resources/highScore.csv";

	public static final String STANDARD_PILE_PATH = "./src/resources/all_cards.csv";

	/**
	 * Specifies the separator used in the supported CSV-files.
	 */
	public static final String CSV_SEPARATOR = ",";

	/**
	 * Constructs a new IOController with the specified GameController.
	 *
	 * @param gameController The coordinator GameController of this IOController.
	 * @throws NullPointerException if the specified GameController is {@code null}.
	 */
	public IOController(GameController gameController){
		if(gameController == null) {
			throw new NullPointerException();
		}
		this.gameController = gameController;
	}

	/**
	 * Serialises the currently running game of Port Royal {@link GameController#getGameLog()} and saves it as the
	 * specified file.
	 *
	 * @param saveFile the file the game will be saved to
	 * @return {@code true} if, and only if, the game was successfully saved.
	 * @throws FileNotFoundException if the file exists but is a directory rather than a regular file, does not exist
	 *                               but cannot be created, or cannot be opened for any other reason.
	 * @throws SecurityException if a security manager exists and its checkWrite method denies write access to the file.
	 * @see FileOutputStream#FileOutputStream(File)
	 */
	public boolean saveGame(File saveFile) throws FileNotFoundException,SecurityException {
		OutputStream fOS = null;
		try {
			fOS = new FileOutputStream(saveFile);
			try {
				ObjectOutputStream oOS = new ObjectOutputStream(fOS);
				GameLog gameLog = gameController.getGameLog();
				oOS.writeObject(gameLog);
			}
			catch (IOException | SecurityException | NullPointerException unreportedEx) {
				// Exceptions that are not specified and reported back to the user.
				unreportedEx.printStackTrace();
				return false;
			}
		}
		finally {
			try {
				if(fOS != null) {
					fOS.close();
				}
			}
			catch (IOException iOEx) {
				iOEx.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Loads and returns a serialised game of Port Royal {@link GameLog} stored in the specified file.
	 *
	 * @param loadFile the file the game will be loaded from
	 * @return The loaded GameLog object or {@code null} if an exception has occurred that should not be reported back
	 *         to the user.
	 * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some
	 *                               other reason cannot be opened for reading.
	 * @throws SecurityException if a security manager exists and its checkRead method denies read access to the file.
	 * @see FileInputStream#FileInputStream(File)
	 */
	public GameLog loadGame(File loadFile) throws FileNotFoundException,SecurityException {
		InputStream fIS = null;
		GameLog loadedGame = null;
		try {
			fIS = new FileInputStream(loadFile);
			try {
				ObjectInputStream oIS = new ObjectInputStream(fIS);
				loadedGame = (GameLog)oIS.readObject();
			}
			// Exceptions that are not specified and reported back to the user.
			catch(IOException | SecurityException | NullPointerException | ClassNotFoundException unreportedEx) {
				unreportedEx.printStackTrace();
			}
		}
		finally {
			try {
				if(fIS != null) {
					fIS.close();
				}
			}
			catch(IOException iOEx) {
				iOEx.printStackTrace();
			}
		}
		return loadedGame;
	}

	/**
	 * Loads the CSV-file stored in the specified file and returns it as a list of its lines represented as
	 * String-objects.
	 *
	 * @param csvFile The CSV-file to be loaded.
	 * @return A list of the lines represented as String-objects that are contained in the specified file.
	 * @throws IllegalArgumentException if the specified file is no CSV-file.
	 * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some
	 * 	                             other reason cannot be opened for reading.
	 * @see FileReader#FileReader(File)
	 */
	private ArrayList<String> loadCSV(File csvFile) throws FileNotFoundException {
		if(!csvFile.getName().endsWith(".csv")) {
			throw new IllegalArgumentException();
		}
		ArrayList<String> rows = new ArrayList<>();
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
			String line = bufferedReader.readLine();
			while(line != null) {
				rows.add(line);
				line = bufferedReader.readLine();
			}
		}
		catch(IOException iOEx) {
			iOEx.printStackTrace();
		}
		return rows;
	}

	/**
	 * Loads the CSV-file stored in the specified file and returns it as a list of its lines represented as
	 * String-objects. Used to load a card pile with a predefined order.
	 *
	 * @param csvFile The CSV-file to be loaded.
	 * @return A list of the lines represented as String-objects that are contained in the specified file.
	 * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some
	 * 	  	                         other reason cannot be opened for reading.
	 * @see #loadCSV(File)
	 */
	public ArrayList<String> loadPile(File csvFile) throws FileNotFoundException {
		ArrayList<String> pile = this.loadCSV(csvFile);
		if(pile.size() > PileController.INITIAL_PILE_SIZE + 1) {
			pile.remove(0);
		}
		return pile;
	}

	/**
	 * Returns a list of String-objects each representing a single entry in the high score list.
	 *
	 * @param highScoreFile The CSV-file were the high score is stored.
	 * @return A list of String-objects each representing a single entry in the high score list, or {@code null} if the
	 *         CSV file could not be found at {@link #HIGH_SCORE_PATH}.
	 */
	public ArrayList<String> loadHighScore(File highScoreFile) {
		if(highScoreFile == null || !highScoreFile.exists()) {
			return null;
		}
		try {
			return this.loadCSV(highScoreFile);
		}
		catch(FileNotFoundException | IllegalArgumentException exception) {
			return null;
		}
	}

	/**
	 * Saves the current high score list {@link GameController#getHighScore()} to a CSV-file at
	 * {@link #HIGH_SCORE_PATH}.
	 *
	 * @param highScoreFile The CSV-file were the high score is to be stored.
	 * @return {@code true} if, and only if, the high score list was successfully saved.
	 */
	public boolean saveHighScore(File highScoreFile) {
		if(highScoreFile == null) {
			System.err.println("ERROR: 'highScoreFile' was 'null'.");
			return false;
		}
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(highScoreFile))) {
			for(String line : this.gameController.getHighScore()) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
		}
		catch(IOException iOEx) {
			iOEx.printStackTrace();
			return false;
		}
		return true;
	}
}
