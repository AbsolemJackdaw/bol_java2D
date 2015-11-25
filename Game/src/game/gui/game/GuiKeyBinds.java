package game.gui.game;

import static game.util.Constants.COLOR_GENERAL;
import engine.keyhandlers.KeyHandler;
import game.World;
import game.content.save.Save;
import game.entity.living.player.Player;
import game.gui.Gui;
import game.util.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GuiKeyBinds extends Gui {

	private int[] selection = new int[2]; 

	public GuiKeyBinds(World world, Player p) {
		super(world, p);
	}

	@Override
	public void draw(Graphics2D g) {

		g.setFont(Constants.FONT_HEADER_SMALL);

		g.setColor(Constants.COLOR_GENERAL);

		
		g.drawString("Controls", 10, 15);
		g.drawString("Various Keys :", 10, 75);
		g.drawString("Debugging Keys :", 10, 135);

		g.setColor(Color.white.darker());

		g.drawString("Movement Keys : (up, left, down, right)", 10, 35);
		g.drawString("(Attack/Validation, Validation, Inventory, Interaction)",10,90);
		g.drawString("(Console, BB, QuickSave, Destroy Item)",10,150);

		g.setColor(Color.white);

		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.UP]), 40, 55);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.LEFT]), 90, 55);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.DOWN]), 140, 55);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.RIGHT]), 190, 55);

		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.SPACE]), 40, 115);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.ENTER]), 90, 115);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.INVENTORY]), 140, 115);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.INTERACT]), 190, 115);

		g.setColor(COLOR_GENERAL);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.CRAFT]), 40, 175);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.B]), 90, 175);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.QUICKSAVE]), 140, 175);
		g.drawString(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.JUNK]), 190, 175);

		int i = (selection[1]*4) + selection[0];
		if(i == 4 || i == 5)
			g.drawRect(37 + selection[0]*50,41 + selection[1]*60 , 48, 16);
		else
			g.drawRect(37 + selection[0]*50,41 + selection[1]*60 , 20, 16);


	}

	@Override
	public void handleGuiKeyInput() {
		if(KeyHandler.isPressed(KeyHandler.ESCAPE)|| KeyHandler.isPressed(KeyHandler.ESCAPE2)){

			Save.writeKeyBinds();

			world.displayGui(null);

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
				if(i != 8)
					KeyHandler.registeredKeys[i] = KeyHandler.keyCode;
			}
		}
	}
}
