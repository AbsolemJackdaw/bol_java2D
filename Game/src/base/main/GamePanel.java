package base.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import base.main.keyhandler.KeyHandler;


@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions
	public static final int WIDTH = 512;
	public static final int HEIGHT = 300;
	public static float SCALE = 2f;

	public static int SCALEDX;
	public static int SCALEDY;

	// game thread
	private Thread thread;
	private boolean running;
	//	private final int FPS = 60;
	//	private final long targetTime = 1000 / FPS;

	long lastLoopTime = System.nanoTime();
	final int TARGET_FPS = 60;
	final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; 
	long lastFpsTime;
	long fps;

	// image
	private BufferedImage image;
	private Graphics2D g;

	// game state manager
	private GameStateManager gsm;

	public GamePanel() {
		super();
		float x = WIDTH*SCALE;
		float y = HEIGHT * SCALE;
		SCALEDX = (int)x;
		SCALEDY = (int)y;

		setPreferredSize(new Dimension(SCALEDX, SCALEDY));
		setFocusable(true);
		requestFocus();

	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	private void draw() {
		gsm.draw(g);
	}

	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, SCALEDX, SCALEDY, null);
		g2.dispose();
	}

	private void init() {

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		running = true;

		gsm = new GameStateManager();
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

		//BEST UPDATE SYSTEM EVER !
		//http://entropyinteractive.com/2011/02/game-engine-design-the-game-loop/
		
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
