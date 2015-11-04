package game.content;

import engine.game.entity.EntityLiving;
import game.World;
import game.entity.Entity;

import java.util.Random;

public class SpawningLogic {

	public static void spawnNightCreatures(World world, boolean forceGeneration) {

		// TODO random chance
		// get map size
		// check for air block
		// spawn creature

		if (world.gametime.getCurrentTime() == world.gametime.getDawn() || forceGeneration) {
			Random rand = new Random();
			// number of entities
			int r = rand.nextInt(9) + 2; // min 2, max 10

			for (int i = 0; i < r; i++) {
				EntityLiving el = (EntityLiving) Entity.createEntityFromUIN(
						Entity.BAT, world.tileMap, world);

				int x = new Random().nextInt(world.tileMap.getXRows());
				int y = new Random().nextInt(world.tileMap.getYRows());

				if (y + 1 < world.tileMap.getYRows())
					if (world.tileMap.getBlockID(x, y) == 0) {
						if (world.tileMap.getBlockID(x, y + 1) > 0) {
							el.setPosition(x * 32 + 16, y * 32 + 16);
							world.listWithMapObjects.add(el);
							System.out.println("added bat at " + x + " " + (y));
						}
					}
			}
		}
		world.hasCreaturesSpawned = true;
	}
}
