package net.unknownuser.mines.execs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
					boardPreset = pre;
				}
			}
			
			if(boardPreset != null) {
				if(boardPreset == BoardPreset.CUSTOM) {
					System.out.print("width of the board: ");
					int width = Integer.parseInt(br.readLine());
					
					System.out.print("height of the board: ");
					int height = Integer.parseInt(br.readLine());
					
					System.out.print("number of mines: ");
					int amountMines = Integer.parseInt(br.readLine());
					
					gameField = new Board(width, height, amountMines);
					System.out.printf("%nBoard (%dx%d) generated with %d mines%n%n", width, height, amountMines);
				} else {
					gameField = new Board(boardPreset.width, boardPreset.height, boardPreset.mineAmount);
					System.out.printf("%nBoard (%dx%d) generated with %d mines%n%n", boardPreset.width, boardPreset.height, boardPreset.mineAmount);
				}
			}
			
			boolean done = false;
			if(gameField != null)
				do {
					System.out.println();
					System.out.println(gameField);
					System.out.print("command: ");
					String command = br.readLine().toLowerCase();
					
					if(command.equals("done") || command.equals("exit")) {
						done = true;
						break;
					}
					
					if(command.startsWith("flag") || command.startsWith("click")) {
						try {
							String coordinates = command.split(" ")[1];
							String[] cords = coordinates.split(",");
//						System.out.println(Arrays.toString(cords));
//						System.out.println(cords.length);
							int x = Integer.parseInt(cords[0]) - 1;
							int y = Integer.parseInt(cords[1]) - 1;
							
							if(gameField.isInBounds(x, y)) {
								if(command.startsWith("flag ")) {
									gameField.updateFlag(x, y);
								} else {
									
									if(command.startsWith("click ")) {
										if(gameField.clickField(x, y)) {
											System.out.println("You clicked on a mine!");
											break;
										}
									}
								}
								
								done = gameField.isDone();
							} else {
								throw new NumberFormatException();
							}
						} catch(NumberFormatException exc) {
							System.out.println("invalid number");
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
			
			System.out.println("game is finished\n");
			System.out.println(gameField.show());
			
		} catch(NumberFormatException exc) {
			System.err.println("invalid number");
		} catch(IOException exc) {
			System.err.println("an error has occured");
			exc.printStackTrace();
		}
	}
}