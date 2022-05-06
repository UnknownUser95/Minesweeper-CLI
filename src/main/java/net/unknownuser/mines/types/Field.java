package net.unknownuser.mines.types;

public class Field {
	private final FieldType actualType;
	private FieldType shownType;
	
	public final int x;
	public final int y;
	
	private int value = 0;
	
	public Field(FieldType type, int x, int y) {
		this.actualType = type;
		if(type == FieldType.MINE) {
			value = -1;
		}
		this.x = x;
		this.y = y;
		this.shownType = FieldType.UNKNOWN;
	}
	
	public char getChar() {
		if(value != 0 && shownType == FieldType.EMPTY) {
			return (char) (value + 48);
		}
		return shownType.getChar();
	}
	
	public FieldType getActualType() {
		return actualType;
	}
	
	public FieldType getShownType() {
		return shownType;
	}
	
	public void setShownType(FieldType type) {
		shownType = type;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public char getAbsoluteChar() {
		if(actualType == FieldType.MINE || value == 0) {
			return actualType.getChar();
		} else {
			return (char) (value + 48);
		}
	}
	
	public void update() {
		shownType = actualType;
	}

	@Override
	public String toString() {
		return String.format("[%d|%d], is %c, shown %c, value %d", x, y, actualType.getChar(), shownType.getChar(), value);
	}
}