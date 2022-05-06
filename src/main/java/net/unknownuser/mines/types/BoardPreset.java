package net.unknownuser.mines.types;

public enum BoardPreset {
	SMALL(9,9,10, "small"),
	MEDIUM(16,16,40, "medium"),
	LARGE(30,16,99, "large"),
	CUSTOM(0,0,0, "custom");
	
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