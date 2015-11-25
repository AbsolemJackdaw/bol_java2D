package engine.keyhandlers;

import java.awt.event.KeyEvent;
import java.util.ArrayList;



public class KeyHandler {
	public static final int NUM_KEYS = 32;

	public static int keyCode;

	public static boolean anyKey;
	public static boolean prevAnyKey;

	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];

	private static ArrayList<String> keysTyped = new ArrayList<>();

	public static int[] registeredKeys = new int[]{
		KeyEvent.VK_Z, //0
		KeyEvent.VK_Q, //1
		KeyEvent.VK_S, //2
		KeyEvent.VK_D, //3

		KeyEvent.VK_SPACE, //4 //Attack
		KeyEvent.VK_ENTER, //5 //Accept
		KeyEvent.VK_E, //6 //inventory
		KeyEvent.VK_F, //7 //interact

		KeyEvent.VK_C,//8 for crafting
		KeyEvent.VK_B,//9 //for bounding boxes. shouldn't be changeable
		KeyEvent.VK_F5, //10
		KeyEvent.VK_A, //11

		KeyEvent.VK_UP, //12
		KeyEvent.VK_DOWN, //13
		KeyEvent.VK_LEFT, //14
		KeyEvent.VK_RIGHT, //15

		KeyEvent.VK_1,
		KeyEvent.VK_2,
		KeyEvent.VK_3,
		KeyEvent.VK_4,
		KeyEvent.VK_5, //20
		KeyEvent.VK_6, 
		KeyEvent.VK_7,
		KeyEvent.VK_8,
		KeyEvent.VK_9,
		KeyEvent.VK_0, //25

		KeyEvent.VK_M,//Place button for xbox controllers 

		KeyEvent.VK_ESCAPE,
		KeyEvent.VK_N, //escape button 2, for controllers only

		KeyEvent.VK_SHIFT,
		KeyEvent.VK_CONTROL,
	};

	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;

	public static int SPACE = 4;
	public static int ENTER = 5;
	public static int INVENTORY = 6;
	public static int INTERACT = 7;

	public static int CRAFT = 8;
	public static int B = 9;
	public static int QUICKSAVE = 10;
	public static int JUNK = 11;

	public static int ARROW_UP = 12;
	public static int ARROW_DOWN = 13;
	public static int ARROW_LEFT = 14;
	public static int ARROW_RIGHT = 15;

	public static int ONE = 16;
	public static int TWO = 17;
	public static int THREE = 18;
	public static int FOUR = 19;
	public static int FIVE = 20;
	public static int SIX = 21;
	public static int SEVEN = 22;
	public static int EIGHT = 23;
	public static int NINE = 24;
	public static int ZERO = 25;

	//xboxOnly
	public static int PLACE = 26;
	public static int ESCAPE2 = 28;
	//

	public static int ESCAPE = 27;
	public static int SHIFT = 29;
	public static int CTRL = 30;

	public static int ANYKEY = 31;

	/**Returns true if any of the registered keys are pressed*/
	public static boolean anyKeyPress() {
		for (int i = 0; i < NUM_KEYS; i++)
			if (keyState[i])
				return true;
		return false;
	}

	/**Returns a the given string + the key typed*/
	public static String getKeyString(int i, String string){

		if(keysTyped.isEmpty())
			return string;

		if(keyState[ANYKEY] && !prevKeyState[ANYKEY] || keysTyped.size() > 0){

			String s = keysTyped.get(0);
			
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

			keysTyped.remove(0);
			keysTyped.trimToSize();

		}

		return string.toLowerCase();
	}

	/**
	 * If the key given is pressed.
	 * ea : returns once, if key is held, it will not continue firing
	 * @param keyID : Keyhandler's key id;
	 */
	public static boolean isPressed(int keyID) {
		return keyState[keyID] && !prevKeyState[keyID] ;
	}

	/**
	 * If the key has been released;
	 * @param keyID : Keyhandler's key id;
	 */
	public static boolean isReleased(int keyID) {
		return !keyState[keyID] && prevKeyState[keyID];
	}
	
	/**
	 * Wether the given key is being held down or not.
	 * This fires after {@link isPressed(int keyID)}  and before {@link isReleased(int keyID)}
	 * 
	 * @param Keyhandler's key id;
	 */
	public static boolean isHeldDown(int keyID) {
		return keyState[keyID] && prevKeyState[keyID];
	}

	public static boolean isValidationKeyPressed(){
		return (keyState[SPACE] && !prevKeyState[SPACE]) || (keyState[ENTER] && !prevKeyState[ENTER]);
	}

	/**prevents the game from registering every key and displaying it in the console*/
	private static boolean isConsoleOpen;

	public static void keySet(int i, boolean b){

		keyCode = i;

		if(i == KeyEvent.VK_SHIFT && prevKeyState[CTRL])//when console is opened
			isConsoleOpen = true;
		if(i == KeyEvent.VK_ESCAPE || i == KeyEvent.VK_ENTER)//when console is closed
			isConsoleOpen = false;

		if(b && isConsoleOpen)
			keysTyped.add(KeyEvent.getKeyText(i));


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

		else if (i == registeredKeys[CRAFT])
			keyState[CRAFT] = b;

		else if (i == registeredKeys[INVENTORY])
			keyState[INVENTORY] = b;

		else if (i == registeredKeys[B])
			keyState[B] = b;

		else if (i == KeyEvent.VK_1)
			keyState[ONE] = b;

		else if (i == KeyEvent.VK_2)
			keyState[TWO] = b;

		else if (i == KeyEvent.VK_3)
			keyState[THREE] = b;

		else if (i == KeyEvent.VK_4)
			keyState[FOUR] = b;

		else if (i == KeyEvent.VK_5)
			keyState[FIVE] = b;

		else if (i == KeyEvent.VK_6)
			keyState[SIX] = b;

		else if (i == KeyEvent.VK_7)
			keyState[SEVEN] = b;

		else if (i == KeyEvent.VK_8)
			keyState[EIGHT] = b;

		else if (i == KeyEvent.VK_9)
			keyState[NINE] = b;

		else if ( i == KeyEvent.VK_0)
			keyState[ZERO] = b;

		else if( i == KeyEvent.VK_UP)
			keyState[ARROW_UP] = b;

		else if( i == KeyEvent.VK_DOWN)
			keyState[ARROW_DOWN] = b;

		else if( i == KeyEvent.VK_LEFT)
			keyState[ARROW_LEFT] = b;

		else if( i == KeyEvent.VK_RIGHT)
			keyState[ARROW_RIGHT] = b;

		else if(i == registeredKeys[INTERACT])
			keyState[INTERACT] = b;
		else if(i == registeredKeys[SHIFT] )
			keyState[SHIFT] = b;
		else if(i == registeredKeys[CTRL] )
			keyState[CTRL] = b;
		else if (i == registeredKeys[QUICKSAVE])
			keyState[QUICKSAVE] = b;
		else if(i == registeredKeys[JUNK])
			keyState[JUNK] = b;

		//xbox only.
		else if(i == registeredKeys[PLACE] && XboxController.controller != null)
			keyState[PLACE] = b;
		else if(i == registeredKeys[ESCAPE2] && XboxController.controller != null)
			keyState[ESCAPE2] = b;

		keyState[ANYKEY] = b;

	}

	public static void update() {
		for (int i = 0; i < NUM_KEYS; i++)
			prevKeyState[i] = keyState[i];
	}

	public static boolean shiftIsHeld(){
		return prevKeyState[SHIFT] || keyState[SHIFT];
	}

	public static String getKeyName(int keyId) {
		return KeyEvent.getKeyText(KeyHandler.registeredKeys[keyId]);
	}

	public static boolean isUpKeyPressed(){
		return keyState[UP] || keyState[ARROW_UP];
	}

	public static boolean isDownKeyPressed(){
		return keyState[DOWN] || keyState[ARROW_DOWN];
	}

	public static boolean isLeftKeyPressed(){
		return keyState[LEFT] || keyState[ARROW_LEFT];
	}

	public static boolean isRightKeyPressed(){
		return keyState[RIGHT] || keyState[ARROW_RIGHT];
	}

	public static boolean isUpKeyHit(){
		return isPressed(UP) || isPressed(ARROW_UP);
	}

	public static boolean isDownKeyHit(){
		return isPressed(DOWN) || isPressed(ARROW_DOWN);
	}

	public static boolean isLeftKeyHit(){
		return isPressed(LEFT) || isPressed(ARROW_LEFT);
	}

	public static boolean isRightKeyHit(){
		return isPressed(RIGHT) || isPressed(ARROW_RIGHT);
	}
}
