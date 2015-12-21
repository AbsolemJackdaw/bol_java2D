package game.entity.block.spawner;

import engine.image.Images;
import engine.imaging.Animation;
import game.World;
import game.entity.Entity;
import game.entity.block.Block;
import game.entity.living.EntityLiving;

public class BlockSpawner extends Block {

	final String eluin;
	
	public BlockSpawner(World world, String uin, String entityLivingUIN, int animLenght) {
		super(world, uin);

		eluin = entityLivingUIN;
		
		getAnimation().setFrames(Images.loadMultiImage("/blocks/entity/"+ uin +".png", 32,0,animLenght));
		getAnimation().setDelay(Animation.NONE);

		width = height = entitySizeX = entitySizeY = 32;
	}

	@Override
	public void update() {
		super.update();

		if(getWorld().getPlayer().currentRow < this.currentRow + 4 && getWorld().getPlayer().currentRow > this.currentRow - 4 &&
				getWorld().getPlayer().currentColumn < this.currentColumn + 2 && getWorld().getPlayer().currentColumn > this.currentColumn - 2){
			getAnimation().setDelay(75);
		}

		if(getAnimation().hasPlayedOnce()){
			EntityLiving entity = Entity.createEntityFromUIN(eluin, getWorld());
			entity.setPosition(getPosX(), getPosY());
			getWorld().addEntity(entity);
			remove = true;
		}
	}
}
