package game.content;

public class WorldTask {

	/**UIN from item, block, or action to perform*/
	private String object;

	/**How much or how many the player has to collect/do before the task is completed*/
	private int totalCount;

	/**Counter starts at 0, counts up and sets achieved when count >= totalCount*/
	private int count;

	/**Whether the task has been achieved or not*/
	private boolean achieved;

	private EnumTask taskType;

	
	public static final String JUMP = "Perform a jump"; 
	public static final String WALK = "Walk around"; 
	public static final String SWIM = "Go for a swim"; 

	
	/**
	 *	@param object : UIN from item, block, or action to perform.
	 *	@param count : How much or how many the player has to collect/do before the task is completed.
	 *	@param type : The type of task
	 */
	public WorldTask(String object, int max, EnumTask taskType){

		this.object = object;
		this.totalCount = max;
		this.taskType = taskType;
	}

	public String object(){
		return object;
	}
	
	@Override
	public String toString() {
		return 
				taskType == EnumTask.ACTION ? 
						object+", " + count + "/" +totalCount : 
							taskType == EnumTask.COLLECTIBLE ? 
							"Collect " + object+", " + count + "/" +totalCount :
								"Craft " + object + ", " + count + "/" + totalCount;
	}

	/**
	 * update the task's count and checks if the goal is reached;
	 */
	public void update(int count){
		this.count += count;

		if(this.count >= totalCount)
			achieved = true;
	}

	/**Wether the task has been achieved or not*/
	public boolean isAchieved(){
		return achieved;
	}

	public enum EnumTask{
		COLLECTIBLE,
		ACTION,
		CRAFT
	}
}
