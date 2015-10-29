package game.util;

import game.content.Images;
import game.entity.block.Block;
import game.entity.inventory.IInventory;
import game.item.ItemStack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import base.main.GamePanel;

public class Util {

	public Util() {
	}

	public static void decreaseStack(IInventory inv, int slot, int amount){

		inv.getStackInSlot(slot).stackSize-=amount;
		if(inv.getStackInSlot(slot).stackSize <= 0)
			inv.setStackInSlot(slot, null);
	}

	/**
	 * @param s : text to draw
	 * @param x : position x to draw text at
	 * @param y : position y to draw text at
	 * @param top : color of the top text
	 * @param shadow : color of the shadow text
	 */
	public static void drawStringWithShadow(String s, int x, int y, Graphics2D g, Color top, Color shadow){

		for(int i = 0; i < 2; i++){

			if(i == 0){
				g.setColor(shadow);
				g.drawString(s, x+1, y+1);

			}
			else{
				g.setColor(top);
				g.drawString(s, x, y);
			}
		}
	}

	/**
	 * @param s : text to draw
	 * @param x : position x to draw text at
	 * @param y : position y to draw text at
	 */
	public static void drawStringWithShadow(String s, int x, int y, Graphics2D g){

		for(int i = 0; i < 2; i++){

			if(i == 0){
				g.setColor(Color.darkGray);
				g.drawString(s, x+1, y+1);

			}
			else{
				g.setColor(Color.white);
				g.drawString(s, x, y);
			}
		}
	}

	public static void drawToolTipWindow(Graphics2D g, int[] pos, List<String> toolTip){

		FontMetrics metrics = g.getFontMetrics(Constants.FONT_ITEMS);

		int h = (metrics.getHeight()) * toolTip.size();
		int w = 0;

		for(String text : toolTip){
			if(metrics.stringWidth(text)+10 > w)
				w = metrics.stringWidth(text)+10;
		}

		g.setColor(new Color(0.35f, 0.15f, 0.3f, 0.5f));
		g.fillRoundRect(pos[0], pos[1], w, h, 10, 10);

		g.setColor(new Color(0.7f, 0.7f, 0.8f, 0.9f));
		g.drawRoundRect(pos[0], pos[1], w, h, 10, 10);

	}

	public static void drawToolTipText(Graphics2D g, ItemStack stack, int pos[]){

		if(stack != null){

			g.setFont(Constants.FONT_ITEMS);

			for(int index = 0; index < stack.getItem().getToolTip(stack).size(); index++){
				if(index == 0) // item name
					Util.drawStringWithShadow(stack.getItem().getToolTip(stack).get(index), pos[0] + 5, pos[1] + 11 + (index * 10), g);
				else
					Util.drawStringWithShadow(stack.getItem().getToolTip(stack).get(index), pos[0] + 5, pos[1] + 14 + (index * 10), g, new Color(0.4f, 0.5f, 0.7f), new Color(0.2f, 0.2f, 0.5f));

			}
		}
	}


	private static Color top = new Color(0.4f, 0.7f, 0.3f);
	private static Color shadow = new Color(0.1f, 0.35f, 0.2f);

	public static void drawToolTipText(Graphics2D g, Block block, int pos[]){

		if(block != null){

			g.setFont(Constants.FONT_ITEMS);

			for(int index = 0; index < block.getBlockInfo().size(); index++){
				if(index == 0) // item name
					Util.drawStringWithShadow(block.getBlockInfo().get(index), pos[0] + 5, pos[1] + 11 + (index * 10), g, top, shadow );
				else
					Util.drawStringWithShadow(block.getBlockInfo().get(index), pos[0] + 5, pos[1] + 14 + (index * 10), g, top, shadow);
			}
		}
	}

	public static void drawCenteredString(Graphics2D g, String text, Font f, int x, int y){

		FontMetrics metrics = g.getFontMetrics(f);

		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();

		g.drawString(text, x - (textWidth/2), y - (textHeight/2));

	}

	public static BufferedImage generateStalactiteBackGround(){

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		double width = Images.instance.stalactites[0].getWidth();
		double max = screenSize.getWidth() / width;

		BufferedImage img = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = img.createGraphics();

		g.setColor(Color.red);
		
		for(int i = 0; i < max; i++){
			BufferedImage theImg = Images.instance.stalactites[new Random().nextInt(Images.instance.stalactites.length)];
			g.drawImage(theImg, (int) (width*i), 0, null);
		}

		for(int i = 0; i < max; i++){
			BufferedImage theImg = Images.instance.stalactites[new Random().nextInt(Images.instance.stalactites.length)];
			g.drawImage(theImg, (int) (width*i), GamePanel.HEIGHT, theImg.getWidth(), -theImg.getHeight(),   null);
		}

		return img;
	}
}
