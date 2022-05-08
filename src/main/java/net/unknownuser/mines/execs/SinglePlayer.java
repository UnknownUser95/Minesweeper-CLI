package net.unknownuser.mines.execs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
			// only of a board has been generated a game can be played
			if(gameField != null) {
				System.out.printf("%n%n");
				// the main game loop
				do {
					// always show the current state of the field
					System.out.println();
					System.out.println(gameField);
					// ask for a new command
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
									gameField.updateFlag(x, y);
								} else if(command.startsWith("click ") && gameField.clickField(x, y)) {
									// clickField returns true if a mine has been clicked
									// end the game in that case
									System.out.println("You clicked on a mine!");
									break;
								}
								
								done = gameField.isDone();
							} else {
								System.out.println("invalid number, number too large");
							}
							// any error, syntax or wrong patterns, are handled here
						} catch(NumberFormatException exc) {
							System.out.println("invalid number, number is not a number");
						} catch(Exception exc) {
							// catch both IndexOutOfBounds and PatternSyntax
							System.out.println("invalid syntax");
						}
						
						continue;
					}
					
					if(command.startsWith("debug")) {
						try {
							String debugCommand = command.split(" ")[1];
							
							if(debugCommand.equals("show")) {
								System.out.println(gameField.show());
							} else if(debugCommand.equals("value")) {
								Field[][] board = gameField.getEntireBoard();
								for(int y = 0; y < gameField.height; y++) {
									for(int x = 0; x < gameField.width; x++) {
										
										System.out.print((board[x][y].getValue() != -1) ? board[x][y].getValue() : "-");
									}
									System.out.println();
								}
							} else
								System.out.println("invalid debug command");
							
						} catch(Exception exc) {
							// catch both IndexOutOfBounds and PatternSyntax
							System.out.println("invalid syntax");
						}
						continue;
					}
					
					System.out.println("unknown command");
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
}