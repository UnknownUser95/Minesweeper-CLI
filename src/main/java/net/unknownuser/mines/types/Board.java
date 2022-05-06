package net.unknownuser.mines.types;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class Board {
	private final Field[][] boardField;
	
	public final int width;
	public final int height;
	
	ArrayList<Point> pointsToCheck = new ArrayList<>();
	
	public Board(int width, int height) {
		boardField = new Field[width][height];
		this.width = width;
		this.height = height;
		initializeBoard();
	}
	
	public Board(int width, int height, int mineAmount) {
		boardField = new Field[width][height];
		this.width = width;
		this.height = height;
		initializeBoard();
		generateMines(mineAmount);
		generateValues();
	}
	
	private void initializeBoard() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				boardField[x][y] = new Field(FieldType.EMPTY, x, y);
//				System.out.printf("filled [%d|%d]%n", x, y);
			}
		}
	}
	
	/**
	 * Sets the values of all fields
	 */
	private void generateValues() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(getField(x, y).getActualType() == FieldType.MINE) {
					continue;
				}
				getField(x, y).setValue(getFieldValue(x, y));
			}
		}
	}
	
	/**
	 * Gets the value of a field based of the amount of neighbouring mines
	 * 
	 * @param xMid x coordinate of the point
	 * @param yMid y coordinate of the point
	 * @return The value of the point
	 */
	private int getFieldValue(int xMid, int yMid) {
		int value = 0;
		for(int y = -1; y <= 1; y++) {
			for(int x = -1; x <= 1; x++) {
				if(isInBounds(xMid + x, yMid + y) && getField(xMid + x, yMid + y).getActualType() == FieldType.MINE) {
					value++;
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Generates mines on random point on this board
	 * 
	 * @param mines The amount of mines to be generated
	 */
	public void generateMines(int mines) {
		Random random = new Random();
		
		for(int i = 0; i < mines; i++) {
			boolean generated = false;
			
			do {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				
				if(getField(x, y).getActualType() == FieldType.EMPTY) {
					setField(x, y, new Field(FieldType.MINE, x, y));
					generated = true;
//					System.out.printf("created mine at %d, %d%n", x, y);
				}
			} while(!generated);
		}
	}
	
	/**
	 * Return a field on the given coordinate
	 * 
	 * @param xPos x coordinate of the point
	 * @param yPos y coordinate of the point
	 * @return The Field on the coordinates, {@code null} if the coordinates are out of bounds
	 */
	public Field getField(int xPos, int yPos) {
		if(isInBounds(xPos, yPos)) {
			return boardField[xPos][yPos];
		}
		return null;
	}
	
	/**
	 * Overwrites a field on this board. For debugging: the x and y coordinates of the point cannot be
	 * changed.
	 * 
	 * @param xPos  x coordinate of the to overwritten point
	 * @param yPos  y coordinate of the to overwritten point
	 * @param field
	 */
	public void setField(int xPos, int yPos, Field field) {
		if(isInBounds(xPos, yPos)) {
			boardField[xPos][yPos] = field;
		}
	}
	
	public void updateFlag(int x, int y) {
		Field field = boardField[x][y];
		
		switch (field.getShownType()) {
		case UNKNOWN:
			field.setShownType(FieldType.FLAG);
			break;
		case FLAG:
			field.setShownType(FieldType.UNKNOWN);
			break;
		default:
			System.out.printf("unable to put a flag on field at (%d,%d)%n", x, y);
		}
	}
	
	/**
	 * Imitates a click on a field.
	 * 
	 * @param x x coordinate of the clicked field
	 * @param y y coordinate of the clicked field
	 * @return {@code true} if a mine has been clicked, {@code false} otherwise
	 */
	public boolean clickField(int x, int y) {
		if(getField(x, y).getActualType() == FieldType.MINE) {
//			getField(x, y).update(); - not needed, since the board is shown anyways
			return true;
		}
		switch (getField(x, y).getShownType()) {
		case MINE:
			getField(x, y).update();
			return true;
		case EMPTY, FLAG:
			break;
		case UNKNOWN:
			pointsToCheck.add(new Point(x, y));
			addNeighbours(x, y);
			updateFields();
			break;
		}
		return false;
	}
	
	private void updateFields() {
		while(!pointsToCheck.isEmpty()) {
			Point point = pointsToCheck.get(0);
//			boardField[point.x][point.y].update();
			Field field = getField(point.x, point.y);
			if((field.getValue() == 0 && field.getShownType() == FieldType.UNKNOWN) || hasEmptyNeighour(point.x, point.y)) {
				getField(point.x, point.y).update();
			}
//			System.out.println(point);
			addNeighbours(point.x, point.y);
//			updateFields();
			pointsToCheck.remove(0);
		}
	}
	
	/**
	 * Adds all of the empty neighbouring points to the list of points to check.
	 * 
	 * @param x x coordinate of the middle
	 * @param y y coordinate of the middle
	 */
	private void addNeighbours(int x, int y) {
		// test if any field is shown as UNKNOWN and is EMPTY
		BiPredicate<Integer, Integer> isUnknownEmpty = (t, u) -> {
			if(isInBounds(t, u)) {
				Field field = getField(t, u);
				return field.getShownType() == FieldType.UNKNOWN && field.getActualType() == FieldType.EMPTY;
			}
			return false;
		};
		
		// adds the point if it is EMPTY and UNKNOWN; updates non-0 EMPTY fields
		BiConsumer<Integer, Integer> isValid = (t, u) -> {
			if(isUnknownEmpty.test(t, u) && hasEmptyNeighour(t, u)) {
				Point point = new Point(t, u);
				if(getField(t, u).getValue() > 0) {
					getField(t, u).update();
				} else if(!pointsToCheck.contains(point)) {
					pointsToCheck.add(point);
				}
			}
		};
		
		if(hasEmptyNeighour(x, y)) {
			isValid.accept(x, y + 1);
			isValid.accept(x, y - 1);
			isValid.accept(x + 1, y);
			isValid.accept(x - 1, y);
		}
	}
	
	/**
	 * Test, whether a point has at least one empty neighbour.
	 * 
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @return {@code true} if at least one of the neighbouring fields is empty and has a value of 0,
	 *         {@code false} otherwise.
	 */
	private boolean hasEmptyNeighour(int x, int y) {
		BiPredicate<Integer, Integer> hasEmpty = (t, u) -> {
//			Predicate<Field> emptyAndZero = arg0 -> arg0.getActualType() == FieldType.EMPTY && arg0.getValue() == 0;
			Field field = getField(t, u);
			return isInBounds(t, u) && field.getActualType() == FieldType.EMPTY && field.getValue() == 0;
		};
		
//		System.out.println(boardField[xMid][yMid]);
		return hasEmpty.test(x, y + 1) || hasEmpty.test(x, y - 1) || hasEmpty.test(x + 1, y) || hasEmpty.test(x - 1, y);
	}
	
	// TODO: no point add when value != 0
	
	/**
	 * Tests if a point is inside of the board.
	 * 
	 * @param x x coordinates of the point
	 * @param y y coordinates of the point
	 * @return {@code true} if the point is inside of the board, {@code false} otherwise
	 */
	public boolean isInBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				sb.append(getField(x, y).getChar());
//				sb.append(board[x][y].getType().getChar());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Tests whether this board is finished.
	 * 
	 * @return {@code true} if the board is finished, {@code false} otherwise
	 */
	public boolean isDone() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				Field field = getField(x, y);
				
				// any of these cases is a reason why the board is not finished
				if(field.getShownType() == FieldType.UNKNOWN) {
//					System.out.println("failure at " + field.toString());
					return false;
				}
				
				if(field.getActualType() == FieldType.MINE && field.getShownType() != FieldType.FLAG) {
//					System.out.println("failure at " + field.toString());
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Shows the actual field types of the board. It doesn't change any values.
	 * 
	 * @return The actual board as a String
	 */
	public String show() {
		StringBuilder sb = new StringBuilder();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				sb.append(getField(x, y).getAbsoluteChar());
//				sb.append(board[x][y].getType().getChar());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Returns a clone of the entire board.
	 * 
	 * @return A copy of this entire game field.
	 */
	public Field[][] getEntireBoard() {
		return boardField.clone();
	}
}