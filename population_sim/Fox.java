
import java.util.ArrayList;

public class Fox extends Animal{
	private int stepstodeath = 3;

	//abst
	public void move(){
		ArrayList <Cell> temparr = super.getCell().getNeighbors();
		boolean ate = false;
		
		for (Cell i: temparr){
			if (i.containsRabbit()){
				
				Rabbit r = (Rabbit) i.getAnimal();
				r.becomeEaten();
				r.setCell(null);
				
				super.getCell().removeAnimal();
				super.setCell(i);
				i.setAnimal(this);
				
				
				stepstodeath = 3;
				ate = !ate;
				break;
	
			}
		}			
		if (ate==false){
			super.randomMove();
			super.survived();
			stepstodeath--;
		}
			
	
	}

	public boolean isRabbit(){
		return false;
	}
	
	public boolean timeToBreed(){
		if ((super.getSteps()%8)==0 && super.getSteps()!=0){
			return true;
		}return false;
	}
	
	public Fox giveBirth(){
		super.setSteps(0);
		return new Fox();
	}
	//
	
	
	public 	boolean starve(){
		if (stepstodeath==0){
			return true;
		}return false;
	}
	
	public String toString(){
		return " X ";
	}
	
}
