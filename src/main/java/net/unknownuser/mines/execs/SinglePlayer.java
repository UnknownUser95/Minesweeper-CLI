package net.unknownuser.mines.execs;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import net.unknownuser.ansi.*;
import net.unknownuser.mines.methods.*;
import net.unknownuser.mines.types.*;

public class SinglePlayer {
	private static ArrayList<String> gameplayCommands = new ArrayList<>();
	private static ArrayList<String> debugCommands = new ArrayList<>();
	private static ArrayList<String> endCommands = new ArrayList<>();
	
	private static Board gameField = null;
	private static String errMsg = "";
	private static String command = "";
	
	private static boolean done = false;
	
	private static void initialize() {
		gameplayCommands.add("click");
		gameplayCommands.add("flag");
		
		debugCommands.add("show");
		debugCommands.add("value");
		
		endCommands.add("done");
		endCommands.add("exit");
	}
	
	public static void main(String[] args) {
		initialize();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			BoardPreset boardPreset = null;
			
			System.out.printf("board presets: %s%nChoose mode: ", Arrays.toString(BoardPreset.values()));
			String chosenPreset = br.readLine().toLowerCase();
			
			for(BoardPreset pre : BoardPreset.values()) {
				if(pre.name.equals(chosenPreset)) {
					// if the input matches the name of any preset, use it.
					boardPreset = pre;
					break;
				}
			}
			
			// only continue if the player chose a valid preset
			if(boardPreset != null) {
				if(boardPreset == BoardPreset.CUSTOM) {
					System.out.println();
					gameField = CustomBoards.createCustomBoard(br);
				} else {
					gameField = new Board(boardPreset.width, boardPreset.height, boardPreset.mineAmount);
					System.out.printf("%n%s board (%dx%d) generated with %d mines", boardPreset.name, boardPreset.width, boardPreset.height, boardPreset.mineAmount);
				}
			}
			
			// only used when the player creates an invalid custom board
			if(gameField == null) {
				System.out.println("no board created");
				// we know there's no reason to continue
				return;
			}
			
			boolean firstPrintDone = false;
			boolean plusOneLine = false;
			// only of a board has been generated a game can be played
			System.out.printf("%n%n%n");
			// the main game loop
			do {
				// the first print of the board does not need any redrawing
				if(firstPrintDone) {
					// in case of an error message, the last line has to be completely cleared
					// otherwise text will remain under the command line
					if(plusOneLine) {
						System.out.print(Cursor.UP);
						System.out.print(Cursor.ERASE_LINE);
						plusOneLine = false;
					}
					
					// move to the top of the board + buffer for the input lines
					System.out.print(Cursor.multiEffect(gameField.height + 2, Cursor.UP));
				} else {
					firstPrintDone = true;
				}
				
				// always show the current state of the field
				System.out.println(gameField);
				
				// print error message
				if(!errMsg.isBlank()) {
					System.out.println(errMsg);
					errMsg = "";
					plusOneLine = true;
				}
				
				// ask for a new command
				// the line will be cleared first, as the last entered text is of unknown length
				System.out.print(Cursor.ERASE_LINE);
				System.out.print("# ");
				// commands are handled in lowercase
				command = br.readLine().toLowerCase();
				
				// commands to instantly end the game
				if(listContainsAny(endCommands, command)) {
					done = true;
					break;
				}
				
				// placing a flag or clicking
				if(listStartsWithAny(gameplayCommands, command)) {
					handleGameplayCommands();
					continue;
				}
				
				// player wants to use a debug command
				if(command.startsWith("debug")) {
					handleDebugCommands(br);
					continue;
				}
				
				// not a registered command has been entered
				errMsg = "unknown command";
			} while(!done);
			
			// post game
			System.out.println("game is finished\n");
			// show the revealed board to the player
			System.out.println(gameField.show());
			
		} catch(NumberFormatException exc) {
			// I'm not quite sure anymore where this is needed
			System.err.println("invalid number");
		} catch(IOException exc) {
			System.err.println("an error has occured");
			exc.printStackTrace();
		}
	}
	
	// groups all commands for debugging purposes
	// the BufferedReader is necessary, as it has to wait for affirmation
	private static void handleDebugCommands(BufferedReader br) {
		try {
			// reassign command for proper debug command
			command = command.split(" ")[1];
			
			if(!listStartsWithAny(debugCommands, command)) {
				errMsg = "invalid debug command";
			} else {
				
				switch (command) {
				case "show" -> {
					System.out.print(Cursor.multiEffect(gameField.height + 2, Cursor.UP));
					System.out.println(gameField.show());
					System.out.println();
					waitForInput(br);
				}
				
				case "value" -> {
					System.out.print(Cursor.multiEffect(gameField.height + 2, Cursor.UP));
					Field[][] board = gameField.getEntireBoard();
					
					for(int y = 0; y < gameField.height; y++) {
						for(int x = 0; x < gameField.width; x++) {
							System.out.print((board[x][y].getValue() != -1) ? board[x][y].getValue() : "-");
						}
						System.out.println();
					}
					
					System.out.printf("%n%n");
					waitForInput(br);
				}
				}
			}
			
		} catch(IndexOutOfBoundsException | PatternSyntaxException exc) {
			// not enough parameter | no space
			errMsg = "invalid syntax";
		}
	}
	
	// groups all commands for gameplay
	// commands are specified in the ArrayList gameplayCommands
	private static void handleGameplayCommands() {
		try {
			String[] arguments = command.split(" ");
			
			// reassign the command (from "click <location>" to just "click")
			command = arguments[0];
			// at first split the command from the coordinates
			String coordinates = arguments[1];
			// then the coordinates themselves
			String[] cords = coordinates.split(",");
			// arrays start with 0, so an offset is applied
			int x = Integer.parseInt(cords[0]) - 1;
			int y = Integer.parseInt(cords[1]) - 1;
			
			// if the coordinates is inside of the board, actually do the command
			// the different commands are handled
			if(!gameField.isInBounds(x, y)) {
				errMsg = "invalid number; out of bounds";
			} else {
				
				switch (command) {
				case "flag" -> {
					if(!gameField.updateFlag(x, y)) {
						errMsg = String.format("unable to put a flag on field at (%d,%d)", x, y);
					}
				}
				
				case "click" -> {
					if(gameField.clickField(x, y)) {
						System.out.println("you clicked on a mine!");
						break;
					}
				}
				}
				
				// only update the done status when an action happened
				done = gameField.isDone();
			}
			
		} catch(NumberFormatException exc) {
			// x and/or y are not a number 
			errMsg = "invalid number, number is not a number";
		} catch(IndexOutOfBoundsException | PatternSyntaxException exc) {
			// not enough parameter | no comma
			errMsg = "invalid syntax";
		}
	}
	
	/**
	 * {@link Thread#sleep(long) Thread.sleep}, but handles the exception.
	 */
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException ignore) {}
	}
	
	/**
	 * Waits on the BufferedReader, until &lt;enter&gt; is pressed.
	 * 
	 * @param br The BufferedReader, which will be waited on.
	 */
	private static void waitForInput(BufferedReader br) {
		System.out.print(Cursor.UP);
		try {
			while(br.read() != '\n') {
				sleep(1);
			}
		} catch(IOException ignore) {}
	}
	
	/**
	 * Tests if any string in the given list starts with any of the given strings.
	 * 
	 * @param list    The list of base strings.
	 * @param strings The strings, which may be the start of any the given list.
	 * @return {@code true} if any string in the list starts with any of the given ones, {@code false}
	 *         otherwise.
	 */
	private static boolean listStartsWithAny(List<String> list, String... strings) {
		for(String baseStr : list) {
			for(String compareStr : strings) {
				if(compareStr.startsWith(baseStr)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Tests if the given List contains any of the following objects.
	 * 
	 * @param list    The list, which contents are matched against.
	 * @param objects The objects, which are tested to be in the list.
	 * @return {@code true} if any of the objects are in the list, {@code false} otherwise.
	 */
	private static boolean listContainsAny(List<?> list, Object... objects) {
		for(Object obj : objects) {
			if(list.contains(obj)) {
				return true;
			}
		}
		
		return false;
	}
}