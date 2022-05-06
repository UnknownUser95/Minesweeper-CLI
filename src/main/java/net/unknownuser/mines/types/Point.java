package net.unknownuser.mines.types;

class Point {
	public final int x;
	public final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return String.format("[%d|%d]", x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(o instanceof Point p) {
			return x == p.x && y == p.y;
		}
		return false;
	}
}