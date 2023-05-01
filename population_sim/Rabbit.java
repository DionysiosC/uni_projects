
public class Rabbit extends Animal{
	private Boolean eaten = false;
	
	public void  becomeEaten(){
		eaten = true;
	}
	
	public boolean isEaten(){
		return eaten;
	}
	
	public String toString(){
		return " O ";
	}
	
	//abstract
	public boolean isRabbit(){
		return true;
	}
	
	public boolean timeToBreed(){
		if ((super.getSteps()%3)==0 && super.getSteps()!=0){
			return true;
		}return false;
	}
	
	public Animal giveBirth(){		
		super.setSteps(0);
		return new Rabbit();
	}
	
	public void move(){
		super.randomMove();
		super.survived();
	}
	//
	
	
	
}
