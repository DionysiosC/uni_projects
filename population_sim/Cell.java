
import java.util.ArrayList;
import java.util.Random;

public class Cell {
	
	private ArrayList <Cell> neighbors = new ArrayList <Cell>();
	private Animal cellan = null;
	//private int x,y;/////
	
	public void addNeighbor(Cell other){
		neighbors.add(other);
	}
	
	public Cell  getRandomNeighbor(){		
		
		Random rand = new Random();
		int lim = 3;
		int ret = rand.nextInt(lim);
		if (ret<neighbors.size()){
			return neighbors.get(ret);
			
		}return null;
		
	}
	
	//accessors-mutators
	public  ArrayList <Cell> getNeighbors(){
		return neighbors;
	}
	
	public Animal getAnimal(){
		return cellan;
	}
	
	public void setAnimal(Animal other){
		cellan=other;
	}
	//
	
	public void removeAnimal(){
		cellan=null;
	}
	
	public boolean isEmpty(){
		return cellan==null;
	}
	
	public boolean containsRabbit(){
		if(cellan!=null){
			return cellan.isRabbit();
		}return false;
	}


	//////////
	/*
	public void sett(int x, int y){
		this.x=x;this.y=y;
	}
	public String toString(){
		return "Cell at "+ x+" "+y;
	}
	*/
}