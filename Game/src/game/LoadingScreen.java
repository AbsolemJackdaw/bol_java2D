package game;

import java.awt.Graphics2D;

import javax.swing.SwingWorker;

import engine.gamestate.GameState;
import engine.image.Images;
import game.content.Loading;
import game.content.Sounds;
import game.content.save.Save;
import game.item.Items;
import game.item.crafting.Crafting;
import game.util.Util;

public class LoadingScreen extends GameState{

	public LoadingScreen(GameStateManager gsm) {
		this.gsm = gsm;

		load();
	}

	private void load(){
		Util.startLoadIcon();

		new SwingWorker<Integer, Void>() {

			@Override
			protected Integer doInBackground() {

				try {
					Sounds.load();

					Crafting.loadRecipes();

					Items.loadItems();

					Save.readKeyBinds();

					if(Loading.stalactites[0] == null) //check to only load once
						for(int i = 0; i < Loading.stalactites.length; i++){
							Loading.stalactites[i] = Images.loadImage("/background/stalactite_"+i+".png");
						}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void done() {
				super.done();
				Util.stopLoadIcon();
				gsm.setState(GameStateManager.MENU);
			}
		}.execute();
	}

	@Override
	public void draw(Graphics2D g) {

	}

	@Override
	public void update() {
	}
}
