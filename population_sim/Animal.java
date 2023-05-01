
import java.util.ArrayList;

public abstract class Animal{
	
	private Cell ancell = null;
	private int steps=0;
	
	//abstract methods
	public abstract boolean isRabbit();
	
	public abstract boolean timeToBreed();
	
	public abstract Animal giveBirth();
	
	public abstract void move();
	//
	
	
	
	//accessors-mutators 
	public Cell getCell(){
		return ancell;
	}
	
	public void setCell(Cell newcell){
		ancell = newcell;
	}
	
	public int getSteps(){
		return steps;
	}
	
	public void setSteps(int steps){
		this.steps=steps;
	}
	//
	
	
	//other
	public void survived(){ 
		this.steps+=1;
	}
	
	public void randomMove(){
		Cell temp = ancell.getRandomNeighbor();
		if (temp!=null){ // update the fields
			if(temp.isEmpty()){
				
				ancell.removeAnimal();
				Animal temp1 = this;
				ancell = temp;				
				temp.setAnimal(temp1);
				
			}
		}

	}
	
	public Animal breed(){
		
		if (this.timeToBreed()){
		
			ArrayList <Cell> templist = new ArrayList <Cell> ();
			templist = ancell.getNeighbors();
			
			for (int i = 0; i<templist.size(); i++){
				if (templist.get(i).isEmpty()){		
					
					Animal pup = this.giveBirth();
					templist.get(i).setAnimal(pup);
					pup.setCell(templist.get(i));
					
					
					return pup;
					
				}
			}			
		}return null;
		
	}
	//
	
	
}
