package engine;

import java.awt.Container;

import javax.swing.JFrame;

import engine.window.gameAid.Window;

public class Main {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

				new Window();
				
				final JFrame window = new JFrame(args[0]);
				window.setContentPane(getContainerClass(args[1]));
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setUndecorated(true);
				window.setResizable(false);
				window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				window.pack();
				window.setVisible(true);
				window.setLocationRelativeTo(null);				
	}

	private static Container getContainerClass(String path){

		Class<?> cl = null; 

		try {
			cl = Class.forName(path);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}

		Container container = null;

		try {
			container = (Container) cl.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(0);
		}

		if(container==null){
			System.exit(0);
		}

		return container;

	}
}
