package base.main.keyhandler;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class KeyHandler {
	public static final int NUM_KEYS = 32;

	public static int keyCode;

	public static boolean anyKey;
	public static boolean prevAnyKey;

	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];

	private static ArrayList<String> typed = new ArrayList<>();

	public static int[] registeredKeys = new int[]{
		KeyEvent.VK_Z,
		KeyEvent.VK_Q,
		KeyEvent.VK_S,
		KeyEvent.VK_D,

		KeyEvent.VK_SPACE, //Attack
		KeyEvent.VK_ENTER,//Accept
		KeyEvent.VK_E,//inventory
		KeyEvent.VK_F, //interact

		/**console has been invalidated and is opened with ctrl-shift
		 * tab isn't recognized by swing. (this is normal)*/
		KeyEvent.VK_TAB,//for console shouldn't be changeable. maybe for talking later ?
		KeyEvent.VK_B,//for bounding boxes. shouldn't be changeable

		KeyEvent.VK_1,
		KeyEvent.VK_2,
		KeyEvent.VK_3,
		KeyEvent.VK_4,
		KeyEvent.VK_5,
		KeyEvent.VK_6,
		KeyEvent.VK_7,
		KeyEvent.VK_8,
		KeyEvent.VK_9,
		KeyEvent.VK_M,//Place button for xbox controllers 

		KeyEvent.VK_ESCAPE,
		KeyEvent.VK_N, //escape button 2, for controllers only

		KeyEvent.VK_SHIFT,
		KeyEvent.VK_CONTROL,
		
		KeyEvent.VK_F5,
	};

	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;

	public static int SPACE = 4;
	public static int ENTER = 5;
	public static int INVENTORY = 6;
	public static int INTERACT = 7;

	public static int T = 8;
	public static int B = 9;

	public static int ONE = 10;
	public static int TWO = 11;
	public static int THREE = 12;
	public static int FOUR = 13;
	public static int FIVE = 14;
	public static int SIX = 15;
	public static int SEVEN = 16;
	public static int EIGHT = 17;
	public static int NINE = 18;
	public static int PLACE = 19;

	public static int ESCAPE = 20;

	public static int ESCAPE2 = 21;

	public static int SHIFT = 22;

	public static int CTRL = 23;

	public static int QUICKSAVE = 24;
	
	public static int ANYKEY = 25;


	public static boolean anyKeyPress() {
		for (int i = 0; i < NUM_KEYS; i++)
			if (keyState[i])
				return true;
		return false;
	}

	public static String keyPressed(int i, String string){
		//		System.out.println(string);

		if(typed.isEmpty())
			return string;

		if(keyState[ANYKEY] && !prevKeyState[ANYKEY] || typed.size() > 0){

			String s = typed.get(0);

			if(keyCode == KeyEvent.VK_8 && prevKeyState[SHIFT] || keyCode == KeyEvent.VK_MINUS && prevKeyState[SHIFT])
				string+="_";

			else if(s.length() == 1)
				string +=s;
			else if(keyCode == KeyEvent.VK_BACK_SPACE && string.length() > 0){
				String s2 = string.substring(0, string.length()-1);
				string = s2;
			}
			else if(keyCode == KeyEvent.VK_SPACE)
				string+=" ";


			typed.remove(0);
			typed.trimToSize();

			//			System.out.println("key typed "+s);
			//			System.out.println("string returned " + string);
		}
		return string.toLowerCase();
	}

	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i] ;
	}

	public static boolean isReleased(int i) {
		return !keyState[i] && prevKeyState[i];
	}


	public static boolean isValidationKeyPressed(){
		return (keyState[SPACE] && !prevKeyState[SPACE]) || (keyState[ENTER] && !prevKeyState[ENTER]);
	}

	/**prevents the game from registering every key and displaying it in the console*/
	private static boolean flag;
	
	public static void keySet(int i, boolean b){

		keyCode = i;
		
		if(i == KeyEvent.VK_SHIFT && prevKeyState[CTRL])//when console is opened
			flag = true;
		if(i == KeyEvent.VK_ESCAPE || i == KeyEvent.VK_ENTER)//when console is closed
			flag = false;
		
		if(b && flag)
			typed.add(KeyEvent.getKeyText(i));


		if (i == registeredKeys[UP])
			keyState[UP] = b;
		else if (i == registeredKeys[LEFT])
			keyState[LEFT] = b;
		else if (i == registeredKeys[RIGHT])
			keyState[RIGHT] = b;
		else if (i == registeredKeys[DOWN])
			keyState[DOWN] = b;

		else if (i == registeredKeys[SPACE])
			keyState[SPACE] = b;

		else if (i == registeredKeys[ENTER])
			keyState[ENTER] = b;

		else if (i == registeredKeys[ESCAPE])
			keyState[ESCAPE] = b;

		else if (i == registeredKeys[T])
			keyState[T] = b;

		else if (i == registeredKeys[INVENTORY])
			keyState[INVENTORY] = b;

		else if (i == registeredKeys[B])
			keyState[B] = b;

		else if (i == KeyEvent.VK_1){
			keyState[ONE] = b;
		}
		else if (i == KeyEvent.VK_2){
			keyState[TWO] = b;
		}
		else if (i == KeyEvent.VK_3){
			keyState[THREE] = b;
		}
		else if (i == KeyEvent.VK_4){
			keyState[FOUR] = b;
		}
		else if (i == KeyEvent.VK_5){
			keyState[FIVE] = b;
		}
		else if (i == KeyEvent.VK_6){
			keyState[SIX] = b;
		}
		else if (i == KeyEvent.VK_7){
			keyState[SEVEN] = b;
		}
		else if (i == KeyEvent.VK_8){
			keyState[EIGHT] = b;
		}
		else if (i == KeyEvent.VK_9){
			keyState[NINE] = b;
		}
		else if(i == registeredKeys[INTERACT]){
			keyState[INTERACT] = b;
		}else if(i == registeredKeys[SHIFT] )
			keyState[SHIFT] = b;
		else if(i == registeredKeys[CTRL] )
			keyState[CTRL] = b;
		else if (i == registeredKeys[QUICKSAVE])
			keyState[QUICKSAVE] = b;
		
		//xbox only.
		else if(i == registeredKeys[PLACE]){
			keyState[PLACE] = b;
		}
		else if(i == registeredKeys[ESCAPE2]){
			keyState[ESCAPE2] = b;
		}

		keyState[ANYKEY] = b;
	}

	public static void update() {
		for (int i = 0; i < NUM_KEYS; i++)
			prevKeyState[i] = keyState[i];
	}
}
