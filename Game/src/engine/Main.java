package engine;

import javax.swing.JFrame;

import engine.window.GamePanel;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//TODO get window name from launch settings
		final JFrame window = new JFrame("The Brim of Life");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setUndecorated(true);
		window.setResizable(false);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		window.pack();
		window.setVisible(true);
		window.setLocationRelativeTo(null);

	}

}
