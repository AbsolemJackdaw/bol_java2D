package engine.window.gameAid;


public class Window {

	private static int screenWidth;
	private static int screenHeight;

	public Window(){

//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		screenWidth = 1366; //(int) screenSize.getWidth();//(int)w * (int)16;
		
		screenHeight = 768; //(int) screenSize.getHeight(); //(int)h * (int)9;
		

	}
	
	public static int getWidth(){
		return screenWidth;
	}
	
	public static int getHeight(){
		return screenHeight;
	}

}
