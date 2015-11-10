package game.entity.block.environement;

import engine.map.TileMap;
import game.World;
import game.entity.block.Block;
import game.entity.living.player.Player;

public class BlockEnvironement extends Block{

	public BlockEnvironement(TileMap tm, World world, String uin) {
		super(tm, world, uin);
		
		width = 32;
		height = 32;

		entitySizeX = 32;
		entitySizeY = 32;

		moveSpeed = 0; 
		stopSpeed = 0;
		fallSpeed = 0.05;
		maxFallSpeed = 2;

		facingRight = true;
		
		blockInfo.clear(); // blockinfo initially has text ! clear to reset and add own info
	}
	
	@Override
	public void onEntityHit(float damage) {
		return;
	}
	
	@Override
	public void onEntityHit(Player player) {
		return;
	}
}
