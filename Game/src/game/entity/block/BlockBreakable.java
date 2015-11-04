package game.entity.block;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.game.MapObject;
import engine.map.TileMap;
import engine.music.Music;
import engine.save.DataTag;
import game.World;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.ItemTool;


public class BlockBreakable extends Block{

	private int health;
	private boolean jiggle;

	/**the tool that can be used to destroy this block*/
	private int effectiveTool;

	int tracker = 0;

	public BlockBreakable(TileMap tm, World world, String uin) {
		super(tm, world, uin);
	}

	public BlockBreakable(TileMap tm, World world, String uin, int toolEffectiveness) {
		this(tm, world, uin);
		setEffectiveTool(toolEffectiveness);
	}

	public BlockBreakable setEffectiveTool(int tool){
		effectiveTool = tool;
		return this;
	}

	public int getEffectiveTool(){
		return effectiveTool;
	}

	private int defaultHealth;

	public BlockBreakable setHealth(int i){
		health = i;
		defaultHealth = i;
		return this;
	}

	public void resetHealth(){
		health = defaultHealth;
	}

	public int getHealth(){
		return health;
	}

	@Override
	public void draw(Graphics2D g) {

		setMapPosition();

		if(jiggle){
			tracker ++;
			if(tracker < 2)
				xmap +=4;
			else if(tracker < 4)
				xmap-=4;
			else {
				jiggle = false;
				tracker = 0;
			}
		}
		if (facingRight)
			g.drawImage(getAnimation().getImage(),
					(int) ((xScreen + xmap) - (width / 2)),
					(int) ((yScreen + ymap) - (height / 2)), null);
		else
			g.drawImage(getAnimation().getImage(),
					(int) (((xScreen + xmap) - (width / 2)) + width),
					(int) ((yScreen + ymap) - (height / 2)), -width, height, null);

		if (getWorld().showBoundingBoxes) {
			g.setColor(Color.WHITE);
			g.draw(getRectangle());
		}
	}

	@Override
	public void onEntityHit(Player p, MapObject mo) {

		jiggle = true;

		int wepDmg = 0;

		ItemStack wep = world.getPlayer().invArmor.getWeapon();
		ItemTool tool = null;

		if(wep != null && wep.getItem() instanceof ItemTool)
			tool = ((ItemTool)wep.getItem());

		if(tool != null && effectiveTool == tool.getEffectiveness())
			wepDmg = tool.getEffectiveDamage();

		switch (getType()) {
		case ROCK:
			Music.play("hit_rock_" + (rand.nextInt(4)+1));
			break;
		case WOOD:
			Music.play("hit_wood_" + (rand.nextInt(5)+1));
			break;
		default:
			break;
		}

		if(tool == null){
			if(!needsToolToMine())
				health -= world.getPlayer().getAttackDamage();
		}else{
			if(needsToolToMine()){//if this block needs a tool, break only when tool is held
				if(effectiveTool == tool.getEffectiveness())
					health -= world.getPlayer().getAttackDamage() + wepDmg;
			}else
				if(effectiveTool == tool.getEffectiveness())//if the block doesnt need a tool, a bonus for wielding the right tool will kick in
					health -= world.getPlayer().getAttackDamage() + wepDmg;
				else
					health -= world.getPlayer().getAttackDamage() + (wepDmg/2);//if bonus tool is net yield, use half of the current weapons dmg as bonus

		}

		if(health <= 0)
			mine(p);

	}

	protected void mine(Player p){
		if(getDrop() != null){
			if(p.getInventory().setStackInNextAvailableSlot(getDrop())){
				remove = true;
				
				if(p.invArmor.getWeapon() != null){
					p.invArmor.getWeapon().damageStack(1);
				}
			}else {
				remove = false;
				health = defaultHealth;
			}
		}else{
			health = defaultHealth;
		}
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		data.writeInt("punchHealth", health);

	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		health = data.readInt("punchHealth");
	}

	public boolean needsToolToMine(){
		return false;
	}
}
