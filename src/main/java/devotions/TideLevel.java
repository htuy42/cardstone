package devotions;

public enum TideLevel {

	LOW,HIGH,RISING,FALLING;
	
	
	public TideLevel next(){
		switch(this){
			case LOW:
				return RISING;
			case RISING:
				return HIGH;
			case HIGH:
				return FALLING;
			case FALLING:
				return LOW;
			default:
				return RISING;
		}
	}
}
