package game.item.armor;

import engine.image.Images;
import game.item.Item;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ItemArmor extends Item{

	public static final int HEAD = 0;
	public static final int BODY = 1;
	public static final int EXTRA = 2;
	
	private int bodyPart;
	private String path;
	
	protected float damageReduction = 0.1f;
	
	protected int[] animationLengthArray;
	
	public ItemArmor(String uin, String displayName, String armorTexture, int bodyPart) {
		super(uin, displayName);
		this.bodyPart = bodyPart;
		this.path = armorTexture;
	}
	
	public ItemArmor(String uin, String displayName, int bodyPart) {
		this(uin, displayName, uin, bodyPart);
	}
	
	public int getIndex(){
		return bodyPart;
	}
	
	public ArrayList<BufferedImage[]> getAnimationSheet(){
		return Images.loadMultiAnimation(animationLengthArray, 32, 32, "/player/armor/"+path+".png");
	}

	public float getDamageReduction() {
		return damageReduction;
	}
	
	public ItemArmor setDamageReduction(float damageReduction) {
		this.damageReduction = damageReduction;
		return this;
	}
	
}
