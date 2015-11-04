package game;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.gamestate.GameState;
import engine.image.Images;
import engine.window.GamePanel;
import game.content.Loading;
import game.content.save.Save;
import game.item.Items;
import game.util.Constants;

public class LoadingScreen extends GameState{

	private String currentlyLoading;

	private int step = 0;

	public LoadingScreen(GameStateManager gsm) {
		this.gsm = gsm;

	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		g.setColor(new Color(190,loading,20));
		g.setFont(Constants.FONT_HEADER);

		g.drawString("Loading " + currentlyLoading, 25, GamePanel.HEIGHT-75);

		if(step == 0)
			g.fillRect(25, GamePanel.HEIGHT - 50, ((GamePanel.WIDTH-50)/maxloading)*(maxloading-loading), 25);

		g.setColor(Color.white);
		g.drawRect(25, GamePanel.HEIGHT - 50, GamePanel.WIDTH-50, 25);
	}

	int loading = 200;
	final int maxloading = 200;

	@Override
	public void update() {

		if(loading == 200)
			currentlyLoading = "sounds";
		if(loading == 150)
			currentlyLoading = "items";
		if(loading == 100)
			currentlyLoading = "interface";
		if(loading == 50)
			currentlyLoading = "stalactites";

		loading--;

		if(loading <= 200)
			Loading.loadMusic();

		if(loading <= 150)
			Items.loadItems();

		if(loading <= 100)
			Save.readKeyBinds();

		if(loading <= 50)
			if(Loading.stalactites[0] == null) //check to only load once
				for(int i = 0; i < Loading.stalactites.length; i++){
					Loading.stalactites[i] = Images.loadImage("/background/stalactite_"+i+".png");
				}
		
		
		if(loading == 0){
			gsm.setState(GameStateManager.MENU);
		}
	}

}
