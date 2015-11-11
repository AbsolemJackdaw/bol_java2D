package game.util;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import engine.keyhandlers.KeyHandler;



public class Constants {

	public static final	Font FONT_ITEMS = new Font("Century Gothic", Font.PLAIN, 10);
	public static final Font FONT_HEADER = new Font("Constantia", Font.PLAIN, 36);
	public static final Font FONT_HEADER_SMALL = new Font("Constantia", Font.PLAIN, 18);
	public static final Font FONT_CHOICES = new Font("Arial", Font.PLAIN, 12);
	public static final Font FONT_CHOICES_BIGGER = new Font("Arial", Font.PLAIN, 16);

	public static final Color COLOR_GENERAL = new Color(250, 231, 217);
	
	public static final Random RANDOM = new Random();

	public static int[] hotBarKeys = new int[]{
		KeyHandler.ONE, 
		KeyHandler.TWO,
		KeyHandler.THREE,
		KeyHandler.FOUR, 
		KeyHandler.FIVE,
		KeyHandler.SIX,
		KeyHandler.SEVEN,
		KeyHandler.EIGHT, 
		KeyHandler.NINE,
		KeyHandler.ZERO
	};

	public static final int BODY_IDLE = 0;
	public static final int HEAD_IDLE = 1;
	public static final int ARMS_IDLE = 2;
	public static final int LEGS_IDLE = 3;

	public static final int HEAD_JUMP = 4;
	public static final int ARM_JUMP = 5; //same as leg, but 1st frame
	public static final int LEG_JUMP = 5; //same as arm, but 2nd frame

	public static final int ARMS_RUN = 6;
	public static final int BODY_RUN = 7;
	public static final int LEGS_RUN = 8;

	public static final int ARMS_WEAPON = 9;

	public static final int ARMS_ATTACK = 10;
	public static final int LEGS_ATTACK = 11; //frame 0
	public static final int HEAD_ATTACK = 11; //frame 1
	public static final int BODY_ATTACK = 11; //frame 2

	public static final int ARMOR_EXTRA_IDLE = 0;
	public static final int ARMOR_EXTRA_ATTACK = 1;
	public static final int ARMOR_EXTRA_JUMP = 2;
	public static final int ARMOR_EXTRA_RUN = 3;

	public static final int ARMOR_HEAD_IDLE = 0;
	public static final int ARMOR_TORSO_IDLE = 1;

	public static final int ARMOR_HEAD_RUN = 3;
	public static final int ARMOR_TORSO_RUN = 4;

	public static final int ARMOR_HEAD_ATTACK = 6;
	public static final int ARMOR_TORSO_ATTACK = 7;

	public static final int ACTION_ATTACK = 0;
	public static final int ACTION_WALK = 1;
	public static final int ACTION_JUMPING = 2;
	public static final int ACTION_FALLING = 3;
	public static final int ACTION_IDLE = 4;
}
