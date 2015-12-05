package game.gui.game;

import engine.game.MapObject;
import engine.image.Images;
import engine.window.GamePanel;
import game.World;
import game.content.Loading;
import game.entity.block.Block;
import game.entity.living.player.Player;
import game.gui.Gui;
import game.item.ItemStack;
import game.util.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class GuiHud extends Gui{

	private BufferedImage img;

	public GuiHud(World world, Player p) {
		super(world, p);

		img = Images.loadImage("/gui/hud.png");
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		g.drawString(""+Loading.index , 10, 10);

		int center = centerX - (200/2);
		int slotStartX = center + 15;

		//inventory space
		g.drawImage(img.getSubimage(0, 63, 200, 18), center, GamePanel.HEIGHT - 25, null);

		for(int slot = 0; slot < 10; slot++){
			ItemStack stack = player.getStackInSlot(slot);
			if(stack != null){
				if(!stack.getItem().isStackable() && stack.getDamage() > 0){
					double dmg = (double)stack.getDamage()/(double)stack.getMaxDamage() * 15.0d;

					g.setColor(Color.DARK_GRAY);
					g.drawRect(slotStartX+ (17*slot),(GamePanel.HEIGHT - 10), 15, 1);
					g.setColor(Color.GREEN);
					g.drawRect(slotStartX+ (17*slot), (GamePanel.HEIGHT - 10), (int)dmg, 1);
				}
				stack.getItem().draw(g, slotStartX+ (17*slot), (GamePanel.HEIGHT - 24), stack);
			}
		}

		///draw held item///
		ItemStack heldItem = player.invArmor.getWeapon();
		if(heldItem != null){
			g.drawImage(img.getSubimage(0, 63, 32, 18), center, GamePanel.HEIGHT - 50, null);
			g.drawImage(img.getSubimage(185, 63, 15, 18), center + 32, GamePanel.HEIGHT - 50, null);
			g.drawImage(img.getSubimage(35, 0, 18, 18), slotStartX, GamePanel.HEIGHT - 49, null);

			double dmg = (double)heldItem.getDamage()/(double)heldItem.getMaxDamage() * 15.0d;

			g.setColor(Color.DARK_GRAY);
			g.drawRect(slotStartX,(GamePanel.HEIGHT - 35), 15, 1);
			g.setColor(Color.GREEN);
			g.drawRect(slotStartX, (GamePanel.HEIGHT - 35), (int)dmg, 1);

			heldItem.getItem().draw(g, slotStartX, (GamePanel.HEIGHT - 48), heldItem);
		}

		if(player.isCollidingWithBlock){
			for(MapObject mo : player.getCollidingMapObjects()){
				if(mo instanceof Block){
					Block b = (Block)mo;
					Util.drawToolTipWindow(g, new int[]{player.posX() + 18 ,player.posY() - 20}, b.getBlockInfo());
					Util.drawToolTipText(g, b, new int[]{player.posX() + 18 ,player.posY() - 20});
				}
			}
		}

		int maxX = 0;

		if(player.getMaxHealth() < 5)
			maxX = (int)(player.getMaxHealth()-1) * 13;
		else
			maxX = 4*13;

		for(int i = 0; i < player.getMaxHealth(); i ++){
			int yOffset = ((int)i/5)*12;
			int xOffset = (int)i%5 *13;

			g.drawImage(img.getSubimage(16, 0, 16, 16), centerX + (100 - maxX) - 32 + xOffset , GamePanel.HEIGHT - 40 - yOffset, null);
		}

		for(float i = 0; i < player.getHealth(); i+=0.5f){

			int yOffset = ((int)i/5)*12;
			int xOffset = (int)i%5 *13;

			if((i*10)%2 == 0){
				g.drawImage(img.getSubimage(0, 0, 8, 16), centerX + 100 -maxX - 32 + xOffset, GamePanel.HEIGHT - 40 - yOffset, null);
			}else if((i*10)%2 == 1.0f){
				g.drawImage(img.getSubimage(8, 0, 8, 16), centerX + 100 - maxX + 8 - 32 + xOffset, GamePanel.HEIGHT - 40 - yOffset, null);
			}
		}

		if(player.isInWater()){
			int air = player.getAirSupply() / 200;
			
		for(int i = 0; i < air + 1; i++){
				int xOffset = i%5 * 12;
				int yOffset = ((int)i/5)*12;
				g.drawImage(img.getSubimage(64, 0, 16, 16), centerX + 100 - maxX - 10 - 32 - xOffset, GamePanel.HEIGHT - 36 - yOffset, 10, 10, null);
			}
		}
	}

	@Override
	public boolean pausesGame() {
		return false;
	}

}
