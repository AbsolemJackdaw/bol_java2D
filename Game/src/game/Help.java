package game;

import game.content.Images;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import base.main.GameState;
import base.main.GameStateManager;
import base.main.keyhandler.KeyHandler;
import base.tilemap.Background;

public class Help extends GameState{

	private Background bg;
	private Font font;
	private Color color = new Color(250, 231, 217);

	private int[] selection = new int[2]; 

	public Help(GameStateManager manager) {
		gsm = manager;
		bg = Images.instance.menuBackGround;
		font = new Font("Constantia", Font.PLAIN, 18);
	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);

		g.setFont(font);
		g.setColor(color);

		g.drawString("Controls", 10, 15);
		g.drawString("Movement Keys : (up, down, left, right)", 10, 35);
		//TODO make a list of the keys, when hit enter, ability to change key

		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.UP]), 40, 55);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.LEFT]), 90, 55);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.DOWN]), 140, 55);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.RIGHT]), 190, 55);

		g.drawString("Various Keys :", 10, 75);
		g.drawString("(Attack/Validation, Validation, Inventory, Interaction)",10,90);

		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.SPACE]), 40, 115);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.ENTER]), 90, 115);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.INVENTORY]), 140, 115);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.INTERACT]), 190, 115);


		g.drawString("Debugging Keys :", 10, 135);
		g.drawString("(Console, BB, x, x)",10,150);

		g.drawString("Ctrl + Shift", 40, 175);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.B]), 90, 175);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.QUICKSAVE]), 140, 115);
		//		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.INTERACT]), 190, 115);


		g.drawRect(37 + selection[0]*50,41 + selection[1]*60 ,48,16);


		font = new Font("Constantia", Font.PLAIN, 18);

	}

	@Override
	public void update() {
		if(KeyHandler.isPressed(KeyHandler.ESCAPE)|| KeyHandler.isPressed(KeyHandler.ESCAPE2)){
			gsm.setState(GameStateManager.MENUSTATE);
		}

		else if(KeyHandler.keyState[KeyHandler.ANYKEY] && !KeyHandler.prevKeyState[KeyHandler.ANYKEY]){
			if(KeyHandler.keyCode == KeyEvent.VK_LEFT){
				if(selection[0] > 0)
					selection[0] -= 1;
			}
			else if(KeyHandler.keyCode == KeyEvent.VK_RIGHT){
				if(selection[0] < 3)
					selection[0] += 1;
			}
			else if(KeyHandler.keyCode == KeyEvent.VK_DOWN){
				if(selection[1] < 2 )
					selection[1] += 1;
			}
			else if(KeyHandler.keyCode == KeyEvent.VK_UP){
				if(selection[1] > 0)
					selection[1] -= 1;
			}
			else{
				int i = (selection[1]*4) + selection[0];
				if( i == 12)
					return;
				KeyHandler.registeredKeys[i] = KeyHandler.keyCode;
			}

		}
	}

}
