package net.unknownuser.mines.methods;

import java.io.BufferedReader;
import java.io.IOException;

import net.unknownuser.mines.types.Board;

public abstract class CustomBoards {
	
	/**
	 * A function for handling the creation of a custom board.<br>
	 * Requires a bufferedReader, since closing the stream System.in here closes it everywhere.
	 * @return The player created board, {@code null} if an error occurs during the creation.
	 */
	public static Board createCustomBoard(BufferedReader br) {
		try {
			System.out.print("width of the board: ");
			int width = Integer.parseInt(br.readLine());
			
			System.out.print("height of the board: ");
			int height = Integer.parseInt(br.readLine());
			
			System.out.print("number of mines: ");
			int amountMines = Integer.parseInt(br.readLine());
			
			System.out.printf("%nBoard (%dx%d) generated with %d mines", width, height, amountMines);
			return new Board(width, height, amountMines);
		} catch(IOException exc) {
			System.out.printf("an error occured: %s%n", exc.getMessage());
		}
		return null;
	}
	
}