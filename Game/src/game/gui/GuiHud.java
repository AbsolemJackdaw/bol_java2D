package game.gui;

import engine.image.Images;
import engine.window.GamePanel;
import game.Loading;
import game.World;
import game.entity.MapObject;
import game.entity.block.Block;
import game.entity.living.player.Player;
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
					double dmg = (double)stack.getDamage()/100.0d * 15.0d;

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

			double dmg = (double)heldItem.getDamage()/100.0d * 15.0d;

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
	}

	@Override
	public boolean pausesGame() {
		return false;
	}

}
