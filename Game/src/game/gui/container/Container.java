package game.gui.container;

public interface Container {
	
	/**
	 * Returns the x position of the first slot of an IInventory. 
	 * Used when switching from one inventory to another.
	 */
	public int getFirstSlotLocationX();

	/**
	 * Returns the y position of the first slot of an IInventory. 
	 * Used when switching from one inventory to another.
	 */
	public int getFirstSlotLocationY();

	/**
	 * returns how many space is in between every slot on the X axis to draw the selection square accordingly 
	 */
	public int getSlotSpacingX();

	/**
	 * returns how many space is in between every slot on the Y axis to draw the selection square accordingly 
	 */
	public int getSlotSpacingY();

	/**returns how many slots the x axis contains*/
	public int rowsX();

	/**returns how many slots the y axis contains*/
	public int rowsY();
}
