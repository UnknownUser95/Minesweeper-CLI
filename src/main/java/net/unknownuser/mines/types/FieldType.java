package net.unknownuser.mines.types;

public enum FieldType {
	EMPTY('·'),
	MINE('X'),
	UNKNOWN('#'),
	FLAG('F');
	
	private FieldType(char character) {
		this.character = character;
	}
	
	private char character;
	
	public char getChar() {
		return character;
	}
}