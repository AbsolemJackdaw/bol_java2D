package game.entity.living.enemy;

import engine.image.Images;
import game.World;
import game.block.Blocks;
import game.entity.Entity;
import game.entity.block.Block;
import game.entity.living.environement.EntityDeathAnim;
import game.entity.living.environement.EntityExplosion;
import game.entity.living.environement.EntitySparkle;
import game.item.ItemStack;
import game.item.Items;
import game.util.WeightedStack;

import java.awt.Color;

public class EntityPyrius extends EntityEnemyBlock {

	public EntityPyrius(World world, String uin) {
		super(world, uin);

		initMaxSpeed(1.2 + rand.nextDouble()/5);
		initMoveSpeed(0.1 + rand.nextDouble()/10);

		getAnimation().setFrames(Images.loadMultiImage("/entity/pyrius/pyrius.png", 32, 0, 4));
		getAnimation().setDelay(200);

		stopSpeed = 0.1;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		entitySizeX = entitySizeY = width = height = 32;

		initHealth(50f);

		attackDelay = 125;
	}

	@Override
	public boolean isAgressive() {
		return true;
	}

	@Override
	public void update() {
		super.update();

		int spark = rand.nextInt(75);

		if(spark == 0){
			EntitySparkle es = new EntitySparkle(getWorld(), Entity.SPARKLE, new Color(0.4f, 0.32f, 0.32f));
			es.setPosition(this.getPosX()+((rand.nextDouble()*24)-12), this.getPosY()+((rand.nextDouble()*24d)-12d));
			getWorld().addEntity(es);
		}

		if(getWorld().getPlayer().currentRow > this.currentRow + 5 && getWorld().getPlayer().currentRow < this.currentRow - 5 ||
				getWorld().getPlayer().currentColumn > this.currentColumn + 4 && getWorld().getPlayer().currentColumn < this.currentColumn - 4){

			Block b = Blocks.loadBlockFromString(Blocks.PYRIUS, getWorld());
			if(b != null){
				b.setPosition(getPosX(), getPosY());
				getWorld().addEntity(b);
				remove = true;
			}
		}
	}

	@Override
	public float getAttackDamage() {
		return 1f;
	}

	@Override
	public WeightedStack[] getDrops(){
		return new WeightedStack[]{
				new WeightedStack(new ItemStack(Items.gunpowder,1), 1.0)
		};
	}

	@Override
	public EntityDeathAnim getDeathAnimation() {
		EntityExplosion explosion = new EntityExplosion(getWorld(), 32);
		return explosion ;
	}
}
