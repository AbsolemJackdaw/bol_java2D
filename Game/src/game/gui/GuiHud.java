package game.gui;

import game.Loading;
import game.World;
import game.content.Images;
import game.entity.living.player.Player;
import game.item.ItemStack;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.main.GamePanel;

public class GuiHud extends Gui{

	private BufferedImage img;

	public GuiHud(World world, Player p) {
		super(world, p);

		img = Images.getSimpleImage("/gui/hud.png");
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		g.drawString(""+Loading.index , 10, 10);

		//inventory space
		g.drawImage(img.getSubimage(0, 63, 200, 18), 150, GamePanel.HEIGHT - 25, null);

		for(int slot = 0; slot < 10; slot++){
			ItemStack stack = player.getStackInSlot(slot);
			if(stack != null){
				if(!stack.getItem().isStackable() && stack.getDamage() > 0){
					double dmg = (double)stack.getDamage()/100.0d * 15.0d;
					
					g.setColor(Color.DARK_GRAY);
					g.drawRect(165+ (17*slot),(GamePanel.HEIGHT - 10), 15, 1);
					g.setColor(Color.GREEN);
					g.drawRect(165+ (17*slot), (GamePanel.HEIGHT - 10), (int)dmg, 1);
				}
				stack.getItem().draw(g, 165+ (17*slot), (GamePanel.HEIGHT - 24), stack);
			}
		}
	}

	@Override
	public boolean pausesGame() {
		return false;
	}

}
