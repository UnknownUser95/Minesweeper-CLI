package net.unknownuser.mines.types;

/**
 * An enum describing all possible types a field can have.<br>
 * There are 4 types: {@link #EMPTY}, {@link #MINE}, {@link #FLAG}, and {@link #UNKNOWN}.
 */
public enum FieldType {
	/**
	 * An empty field. Char is ·.
	 */
	EMPTY('·'),
	/**
	 * A field with a mine. Char is X.
	 */
	MINE('X'),
	/**
	 * A yet to be discovered field. Char is #.
	 */
	UNKNOWN('#'),
	/**
	 * A field with a flag. Char is F.
	 */
	FLAG('F');
	
	private FieldType(char character) {
		this.character = character;
	}
	
	private char character;
	
	/**
	 * Return the character associated with the type.
	 * 
	 * @see {@link #EMPTY}
	 * @see {@link #MINE}
	 * @see {@link #FLAG}
	 * @see {@link #UNKNOWN}
	 */
	public char getChar() {
		return character;
	}
}