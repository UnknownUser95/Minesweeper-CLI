package net.unknownuser.mines.types;

import net.unknownuser.mines.methods.CustomBoards;

/**
 * An enum holding presets for minesweeper boards.<br>
 * There are 4 presets: {@link #SMALL}, {@link #MEDIUM}, {@link #LARGE}, and {@link #CUSTOM}.<br>
 * The CUSTOM should use {@link CustomBoards#createCustomBoard(java.io.BufferedReader)
 * CustomBoards.createCustomBoard()} method.
 */
public enum BoardPreset {
	/**
	 * A small board. 9x9 with 10 mines.
	 */
	SMALL(9, 9, 10, "small"),
	/**
	 * A medium board. 16x16 with 40 mines.
	 */
	MEDIUM(16, 16, 40, "medium"),
	/**
	 * A large board. 30x16 with 99 mines.
	 */
	LARGE(30, 16, 99, "large"),
	/**
	 * The custom preset. Everything is set to 0. To create a custom board
	 * {@link CustomBoards#createCustomBoard(java.io.BufferedReader)
	 * CustomBoards.createCustomBoard()}, or a similar method should be used.
	 */
	CUSTOM(0, 0, 0, "custom");
	
	public final int width;
	public final int height;
	public final int mineAmount;
	public final String name;
	
	private BoardPreset(int width, int height, int mineAmount, String name) {
		this.width = width;
		this.height = height;
		this.mineAmount = mineAmount;
		this.name = name;
	}
}
