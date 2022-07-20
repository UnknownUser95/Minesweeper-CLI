package net.unknownuser.mines.execs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import net.unknownuser.ansi.Cursor;
import net.unknownuser.mines.methods.CustomBoards;
import net.unknownuser.mines.types.Board;
import net.unknownuser.mines.types.BoardPreset;
import net.unknownuser.mines.types.Field;

public class SinglePlayer {
	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			BoardPreset boardPreset = null;
			Board gameField = null;
			
			System.out.printf("board presets: %s%nChoose mode: ", Arrays.toString(BoardPreset.values()));
			String inputMode = br.readLine().toLowerCase();
			
			for(BoardPreset pre : BoardPreset.values()) {
				if(pre.name.equals(inputMode)) {
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
			
			boolean done = false;
			boolean firstPrintDone = false;
			String errMsg = "";
			boolean plusOneLine = false;
			// only of a board has been generated a game can be played
			if(gameField != null) {
				System.out.printf("%n%n");
				// the main game loop
				do {
					// the first print of the board does not need any redrawing
					if(firstPrintDone) {
						// in case of an error message, the last line has to be completely cleared
						if(plusOneLine) {
							System.out.print(Cursor.SCROLL_DOWN);
							System.out.print(Cursor.ERASE_LINE);
							plusOneLine = false;
						}
						
						System.out.print(Cursor.multiEffect(gameField.height + 3, Cursor.UP));
					} else {
						firstPrintDone = true;
					}
					
					// always show the current state of the field
					System.out.println();
					System.out.println(gameField);
					
					// print error message
					if(!errMsg.isBlank()) {
						System.out.println(errMsg);
						errMsg = "";
						plusOneLine = true;
					}
					
					// ask for a new command
					// the line will be cleared first, as the entered text is form unknown length
					System.out.print(Cursor.ERASE_LINE);
					System.out.print("command: ");
					// commands are handled in lowercase
					String command = br.readLine().toLowerCase();
					
					// just end the game now
					if(command.equals("done") || command.equals("exit")) {
						done = true;
						break;
					}
					
					// placing a flag or clicking
					// both have a very similar syntax, so grouping them makes sense
					if(command.startsWith("flag") || command.startsWith("click")) {
						try {
							// at first split the command from the coordinates
							String coordinates = command.split(" ")[1];
							// then the coordinates themselves
							String[] cords = coordinates.split(",");
							// arrays start with 0, so an offset is applied
							int x = Integer.parseInt(cords[0]) - 1;
							int y = Integer.parseInt(cords[1]) - 1;
							
							// if the coordinates is inside of the board, actually do the command
							// the different commands are handled
							if(gameField.isInBounds(x, y)) {
								if(command.startsWith("flag ")) {
									if(!gameField.updateFlag(x, y)) {
										errMsg = String.format("unable to put a flag on field at (%d,%d)", x, y);
									}
								} else if(command.startsWith("click ") && gameField.clickField(x, y)) {
									// clickField returns true if a mine has been clicked
									// end the game in that case
									System.out.println("You clicked on a mine!");
									break;
								}
								
								done = gameField.isDone();
							} else {
								errMsg = "invalid number, number too large";
							}
							// any error, syntax or wrong patterns, are handled here
						} catch(NumberFormatException exc) {
							errMsg = "invalid number, number is not a number";
						} catch(Exception exc) {
							// catch both IndexOutOfBounds and PatternSyntax
							errMsg = "invalid syntax";
						}
						
						continue;
					}
					
					if(command.startsWith("debug")) {
						try {
							String debugCommand = command.split(" ")[1];
							
							if(debugCommand.equals("show")) {
								System.out.print(Cursor.multiEffect(gameField.height + 2, Cursor.UP));
								System.out.println(gameField.show());
								System.out.println();
								waitForInput(br);
								
							} else if(debugCommand.equals("value")) {
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
								
							} else {
								errMsg = "invalid debug command";
							}
							
						} catch(Exception exc) {
							// catch both IndexOutOfBounds and PatternSyntax
							errMsg = "invalid syntax";
						}
						continue;
					}
					
					errMsg = "unknown command";
				} while(!done);
			}
			
			System.out.println("game is finished\n");
			System.out.println((gameField != null) ? gameField.show() : "");
			
		} catch(NumberFormatException exc) {
			System.err.println("invalid number");
		} catch(IOException exc) {
			System.err.println("an error has occured");
			exc.printStackTrace();
		}
	}
	
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException ignore) {}
	}
	
	private static void waitForInput(BufferedReader br) {
		try {
			while(br.read() != '\n') {
				sleep(1);
			}
			System.out.print(Cursor.SCROLL_DOWN);
			System.out.print("\r");
			System.out.print(Cursor.ERASE_LINE);
		} catch(IOException ignore) {}
	}
}