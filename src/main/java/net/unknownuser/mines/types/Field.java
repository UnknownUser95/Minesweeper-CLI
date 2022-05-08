package net.unknownuser.mines.types;

/**
 * A class designed to describe a field on a minesweeper game board.
 */
public class Field {
	// there has to be a difference of the actual and shown type
	// technically also possible with a boolean "isShown", but this is also good
	// the actual type also should not be changed once a field is created
	private final FieldType actualType;
	// every field start out as an unknown one
	private FieldType shownType = FieldType.UNKNOWN;
	
	// coordinates on the field
	public final int x;
	public final int y;
	
	// the value of the field (as in the amount of neighbouring mines)
	private int value = 0;
	
	public Field(FieldType type, int x, int y) {
		this.actualType = type;
		if(type == FieldType.MINE) {
			value = -1;
		}
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the symbol / char of this field. In case it is {@code EMPTY} and has a non-zero value,
	 * it is a number.
	 * 
	 * @return The character of this field.
	 */
	public char getChar() {
		// non-zero value conversion
		if(value != 0 && shownType == FieldType.EMPTY) {
			// ASCII offset of numbers are 48 (0 is 48)
			return (char) (value + 48);
		}
		return shownType.getChar();
	}
	
	/**
	 * Returns the actual type of this field. This is not the shown type.
	 * 
	 * @return The actual type
	 * @see {@link #getShownType()}
	 */
	public FieldType getActualType() {
		return actualType;
	}
	
	/**
	 * Return the shown / visible type of this field.
	 * 
	 * @return The visible type of this field
	 * @see {@link #getActualType()}
	 */
	public FieldType getShownType() {
		return shownType;
	}
	
	/**
	 * Overwrites the shown / visible type with the given one.
	 * 
	 * @param type The new shown type of this field.
	 */
	public void setShownType(FieldType type) {
		shownType = type;
	}
	
	/**
	 * Return the value (number of neighbouring mines) of this field.<br>
	 * By default it is 0, or -1 in case of mines. It has to be set via {@link Board#generateValues()}
	 * to be useful.
	 * 
	 * @return The value of this field.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Overwrites the value of this field.
	 * 
	 * @param value The new value of this field.
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Returns the actual char of this field. In case it is {@link FieldType.EMPTY}, but has a value,
	 * the value is returned.
	 * 
	 * @return The actual character of this field.
	 */
	public char getAbsoluteChar() {
		if(actualType == FieldType.MINE || value == 0) {
			return actualType.getChar();
		} else {
			return (char) (value + 48);
		}
	}
	
	/**
	 * Sets the shown type of this field as its actual type.
	 */
	public void update() {
		shownType = actualType;
	}
	
	@Override
	public String toString() {
		return String.format("[%d|%d], is %c, shown %c, value %d", x, y, actualType.getChar(), shownType.getChar(), value);
	}
}