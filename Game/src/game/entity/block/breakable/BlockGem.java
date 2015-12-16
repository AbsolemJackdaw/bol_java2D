package game.entity.block.breakable;

import game.World;
import game.block.Blocks;
import game.entity.Entity;
import game.entity.living.environement.EntitySparkle;
import game.item.Items;


public class BlockGem extends BlockOre {

	public BlockGem(World world) {
		super(world, Blocks.GEM);
		setHealth(75);
		setOre(Items.gem_blue);
		setDropAmmount(rand.nextInt(2) + 1);
	}
	
	@Override
	public void update() {
		super.update();
		
		int spark = rand.nextInt(250);
		
		if(spark == 0){
			EntitySparkle es = new EntitySparkle(getWorld(), Entity.SPARKLE);
			es.setPosition(this.getPosX()+((rand.nextDouble()*24)-12), this.getPosY()+((rand.nextDouble()*24d)-12d));
			getWorld().addEntity(es);
		}
	}
}
