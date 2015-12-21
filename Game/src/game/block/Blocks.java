package game.block;

import engine.image.Images;
import game.World;
import game.entity.Entity;
import game.entity.block.Block;
import game.entity.block.breakable.BlockCraftingTable;
import game.entity.block.breakable.BlockGem;
import game.entity.block.breakable.BlockIron;
import game.entity.block.breakable.BlockLight;
import game.entity.block.breakable.BlockOven;
import game.entity.block.breakable.BlockRock;
import game.entity.block.breakable.BlockWood;
import game.entity.block.environement.BlockDroppedItem;
import game.entity.block.environement.BlockInfoPane;
import game.entity.block.spawner.BlockSpawner;

public class Blocks {

	public static final String VINE = "vine";
	public static final String CRAFTINGTABLE = "workdesk";
	public static final String IRON = "iron_ore";
	public static final String GEM = "gem_ore";
	public static final String ROCK = "rock";
	public static final String CAMPFIRE = "campfire";
	public static final String OVEN = "oven";
	public static final String ADVANCEDCRAFT = "advanced_workdesk";
	public static final String SIGN = "sign";
	public static final String ITEM = "itemblock";
	public static final String PYRIUS = Entity.PYRIUS+"block";
	public static final String WORM = Entity.WORM+"block";

	public static Block loadBlockFromString(String uin, World w){
		switch (uin) {
		case VINE:
			return new BlockWood(w).setType(Block.WOOD);
		case CRAFTINGTABLE:
			return new BlockCraftingTable(w, 5, false, CRAFTINGTABLE).setType(Block.WOOD);
		case IRON:
			return (Block) new BlockIron(w).setType(Block.ROCK).setEntityTexture(Images.loadImage("/blocks/rock_iron.png"));
		case GEM:
			return (Block) new BlockGem(w).setType(Block.ROCK).setEntityTexture(Images.loadImage("/blocks/gem_deposit.png"));
		case ROCK:
			return new BlockRock(w).setType(Block.ROCK);
		case CAMPFIRE:
			return new BlockLight(w, CAMPFIRE).setRadius(200);
		case OVEN:
			return new BlockOven(w).setType(Block.ROCK);
		case ADVANCEDCRAFT:
			return new BlockCraftingTable(w, 20, true, ADVANCEDCRAFT).setType(Block.WOOD);
		case SIGN:
			return new BlockInfoPane(w, SIGN);
		case ITEM:
			return new BlockDroppedItem(w, ITEM);
		case PYRIUS:
			return new BlockSpawner(w, PYRIUS, Entity.PYRIUS,7);
		case WORM:
			return new BlockSpawner(w, WORM, Entity.WORM,5);
		default:
			break;
		}
		return null;
	}
	
}
