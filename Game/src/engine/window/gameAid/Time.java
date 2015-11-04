package engine.window.gameAid;

public class Time {

	private int gametime = 0;

	private final int timeCycle;
	private final int dayCycle;
	private final int nightCycle;
	
	public Time(int devidedTime, int dayCycle, int nightCycle){
		timeCycle = devidedTime;
		this.dayCycle = dayCycle;
		this.nightCycle = nightCycle;
	}

	public void writeCurrentGameTime(int time){
		gametime = time;
	}
	
	public void updateTime(){
		if(gametime < getTotalGameTime())
			gametime ++;
		else
			gametime = 0;
	}

	public int getCurrentTime(){
		return gametime;
	}

	public int getTotalGameTime(){
		return timeCycle * (dayCycle + nightCycle);
	}

	/**Returns the total day time*/
	public int getDawn(){
		return timeCycle * dayCycle;
	}
}
