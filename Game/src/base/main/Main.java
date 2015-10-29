package base.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final JFrame window = new JFrame("The Brim of Life");
		
		GamePanel game = new GamePanel(window);
		
		window.getContentPane().add(game, BorderLayout.CENTER);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setUndecorated(true);
		window.setResizable(false);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		window.pack();
		window.setVisible(true);
		window.setLocationRelativeTo(null);

	}

}
