package game.item.tool;

import engine.save.DataTag;

public class ToolModifier {

	public static final int DMG = 0;
	public static final int EFF = 1;
	public static final int DUR = 2;
	public static final int MOD = 3;

	private int ID = -1;
	private int modifier = -1;

	private int ID2 = -1;
	private int modifier2 = -1;

	public ToolModifier(DataTag tag){
		readModifier(tag);
	}

	public ToolModifier(int mod, int quantity) {
		ID = mod;
		modifier = quantity;
	}

	public ToolModifier(int mod, int quantity, int mod2, int quantity2) {
		ID = mod;
		modifier = quantity;

		ID2 = mod2;
		modifier2 = quantity2;
	}

	public int getModID(){
		return ID;
	}

	public int getModifier() {
		return modifier;
	}

	public int getModifier2() {
		return modifier2;
	}

	public int getModID2() {
		return ID2;
	}

	public void saveModifier(DataTag tag){
		tag.writeInt("modID1", ID);
		tag.writeInt("modID2", ID2);

		tag.writeInt("modifier1", modifier);
		tag.writeInt("modifier2", modifier2);
	}

	public void readModifier(DataTag tag){
		ID = tag.readInt("modID1");
		ID2 = tag.readInt("modID2");

		modifier = tag.readInt("modifier1");
		modifier2 = tag.readInt("modifier2");
	}

	@Override
	public String toString() {
		return "" + ID + " " + modifier + " " + ID2 + " " + modifier2;
	}
	
	@Override
	public boolean equals(Object compared) {

		if(compared instanceof ToolModifier){
			ToolModifier mod = (ToolModifier)compared;
			
			if(getModID() == mod.getModID())
				if(getModID2() == mod.getModID2())
					if(getModifier() == mod.getModifier())
						if(getModifier2() == mod.getModifier2())
							return true;
		}
		
		return false;
	}
}
