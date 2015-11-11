package engine.window.gameAid;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Utility {

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
	
	/**returns the length of a text based on the font of that text*/
	public int getTextWidth(Font font, String text, Graphics2D g){
		FontMetrics metrics = g.getFontMetrics(font);
		return metrics.stringWidth(text);
	}
	
	/**returns the height of a text based on the font of that text*/
	public int getTextHeight(Font font, String text, Graphics2D g){
		FontMetrics metrics = g.getFontMetrics(font);
		return metrics.getHeight();
	}
	
	/**Draws a centered string where x and y are the center of that string*/
	public static void drawCenteredString(Graphics2D g, String text, Font f, int x, int y){

		g.setFont(f);
		
		FontMetrics metrics = g.getFontMetrics(f);

		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();

		g.drawString(text, x - (textWidth/2), y - (textHeight/2));

	}
	
	/**Draws a centered string where x and y are the center of that string*/
	public static void drawCenteredStringWithShadow(Graphics2D g, String text, Font f, int x, int y){

		g.setFont(f);
		
		FontMetrics metrics = g.getFontMetrics(f);

		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();

		for(int i = 0; i < 2; i++){

			if(i == 0){
				g.setColor(Color.darkGray);
				g.drawString(text, x - (textWidth/2) + 1 , y - (textHeight/2) + 1);

			}
			else{
				g.setColor(Color.white);
				g.drawString(text, x - (textWidth/2), y - (textHeight/2));
			}
		}
	}
	
	/**Draws a centered string where x and y are the center of that string*/
	public static void drawCenteredStringWithShadow(Graphics2D g, String text, Font f, int x, int y, Color top, Color shadow){

		g.setFont(f);
		
		FontMetrics metrics = g.getFontMetrics(f);

		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();

		for(int i = 0; i < 2; i++){

			if(i == 0){
				g.setColor(shadow);
				g.drawString(text, x - (textWidth/2) + 1, y - (textHeight/2) + 1);

			}
			else{
				g.setColor(top);
				g.drawString(text, x - (textWidth/2), y - (textHeight/2));
			}
		}
	}
}
