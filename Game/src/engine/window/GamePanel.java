package engine.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import engine.gamestate.GameStateManagerBase;
import engine.keyhandlers.KeyHandler;
import engine.window.gameAid.Window;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions
	public static final int WIDTH = Window.getWidth()/2; //(int)screenSize.getWidth()/2; //TODO apply screensize modifications possible ?
	public static final int HEIGHT = Window.getHeight()/2; //(int)screenSize.getHeight()/2;
	public static float SCALE = 2f;

	public static int SCALEDX;
	public static int SCALEDY;

	// game thread
	private Thread thread;
	private boolean running;
	
	private Thread musicThread;

	long lastLoopTime = System.nanoTime();
	final int TARGET_FPS = 60;
	final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; 
	long lastFpsTime;
	long fps;

	// image
	private BufferedImage image;
	private Graphics2D g;

	// game state manager
	protected GameStateManagerBase gsm;

	public GamePanel() {
		super();

		setPreferredSize();

		setBackground(Color.black);
	}

	public void setPreferredSize(){

		setPreferredSize(getDimension());
		setFocusable(true);
		requestFocus();
	}

	public Dimension getDimension(){

		float x = (WIDTH) * SCALE;
		float y = (HEIGHT) * SCALE;

		SCALEDX = (int)x;
		SCALEDY = (int)y;

		return new Dimension(SCALEDX, SCALEDY);
	}

	@Override
	public void addNotify() {
		super.addNotify();

		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
		
		if(musicThread == null){
			musicThread = new Thread(new MusicPanel());
			musicThread.start();
		}

	}

	/**
	 * Draws world to the game
	 */
	private void draw() {
		gsm.draw(g);
	}

	/**
	 * draws a scaled instance of the screensize to the screen.
	 */
	private void drawToScreen() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//draw in the center
		double posX = screenSize.getWidth()/2 - SCALEDX/2;
		double posY = screenSize.getHeight()/2 - SCALEDY/2;

		Graphics2D g2 = (Graphics2D)getGraphics();

		g2.drawImage(image, (int)posX, (int)posY, SCALEDX/*(int)screenSize.getWidth()*/, SCALEDY/*(int)screenSize.getHeight()*/, null);
		
//		g2.drawImage(image, 0 , 0, (int)screenSize.getWidth(), (int)screenSize.getHeight(), null);

		
		g2.dispose();
	}

	private void init() {

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		running = true;

		gsm = getGameStateManager();
	}

	/**
	 * Register
	 */
	protected GameStateManagerBase getGameStateManager(){
		return null;
	}

	@Override
	public void keyPressed(KeyEvent key) {
		KeyHandler.keySet(key.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent key) {
		KeyHandler.keySet(key.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}

	@Override
	public void run() {

		init();

		//Best Update System I found on the net !
		//http://entropyinteractive.com/2011/02/game-engine-design-the-game-loop/
		//thanksx1000 to this dude, as well as cuddos

		// convert the time to seconds
		double nextTime = (double)System.nanoTime() / 1000000000.0;
		double maxTimeDiff = 0.5;
		int skippedFrames = 1;
		int maxSkippedFrames = 5;
		double delta = 1.0/60.0;

		while(running)
		{
			// convert the time to seconds
			double currTime = (double)System.nanoTime() / 1000000000.0;
			if((currTime - nextTime) > maxTimeDiff) nextTime = currTime;
			if(currTime >= nextTime)
			{
				// assign the time for the next update
				nextTime += delta;

				update();
				
				if((currTime < nextTime) || (skippedFrames > maxSkippedFrames))
				{
					draw();
					drawToScreen();
					skippedFrames = 1;
				}
				else
				{
					skippedFrames++;
				}
			}
			else
			{
				// calculate the time to sleep
				int sleepTime = (int)(1000.0 * (nextTime - currTime));
				// sanity check
				if(sleepTime > 0)
				{
					// sleep until the next update
					try
					{
						Thread.sleep(sleepTime);
					}
					catch(InterruptedException e)
					{
						// do nothing
					}
				}
			}
		}
	}

	private void update() {
		gsm.update();
		KeyHandler.update();
	}
}
