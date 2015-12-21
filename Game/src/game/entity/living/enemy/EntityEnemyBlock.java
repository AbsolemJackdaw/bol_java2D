package game.entity.living.enemy;

import game.World;
import game.block.Blocks;
import game.entity.Entity;
import game.entity.block.Block;
import game.entity.living.environement.EntityBlockBreak;
import game.entity.living.environement.EntityDeathAnim;

public class EntityEnemyBlock extends EntityEnemy {

	private boolean turnedToBlock;

	public EntityEnemyBlock(World world, String uin) {
		super(world, uin);
	}

	@Override
	public void update() {
		super.update();

		if(getWorld().getPlayer().currentRow > this.currentRow + 5 || getWorld().getPlayer().currentRow < this.currentRow - 5 ||
				getWorld().getPlayer().currentColumn > this.currentColumn + 4 || getWorld().getPlayer().currentColumn < this.currentColumn - 4){

			Block b = Blocks.loadBlockFromString(getUin() +"block", getWorld());
			if(b != null){
				b.setPosition(getPosX(), getPosY());
				getWorld().addEntity(b);
				turnedToBlock = true;
				remove = true;
			}
		}
	}

	@Override
	public EntityDeathAnim getDeathAnimation() {

		EntityBlockBreak ebb = new EntityBlockBreak(getWorld(), Entity.DEATHBLOCK);
		Block b = Blocks.loadBlockFromString(getUin() +"block", getWorld());
		ebb.setParticleTexture(b.getAnimation().getImage());
		ebb.reloadTexture();
		ebb.setPosition(getPosX(), getPosY());
		ebb.setJumping(true);
		ebb.dy = ebb.jumpStart;

		return turnedToBlock ? ebb : super.getDeathAnimation(); 
	}

}
