
import java.util.Random;

public class Grid{
	
	private final int SIZE = 20;
	private Cell[][] grid = new Cell [SIZE][SIZE];
	
	public Grid(){								
		for (int i = 0; i < SIZE; i++){
			for (int j = 0; j<SIZE; j++){
				grid[i][j] = new Cell();	
				//grid[i][j].sett(i,j);
			}
		}
		
		for (int i = 0; i < SIZE; i++){	
				for (int p = 0; p < SIZE; p++){
					if (i>0 ){
						grid[i-1][p].addNeighbor(grid[i][p]);	
						grid[i][p].addNeighbor(grid[i-1][p]);
					}
					if (p>0){
						
						grid[i][p-1].addNeighbor(grid[i][p]);						
						grid[i][p].addNeighbor(grid[i][p-1]);
					
					
					}
				}
		}
		
	}
	
	public void addRandomly(Animal other){
		Random randi = new Random();
		Random randj = new Random();
		
		int x = randi.nextInt(SIZE);
		int y = randj.nextInt(SIZE);
		while (!grid[x][y].isEmpty()){
			randi = new Random();
			randj = new Random();
			x = randi.nextInt(SIZE);
			y = randj.nextInt(SIZE);
			
		}
		grid[x][y].setAnimal(other);
		other.setCell(grid[x][y]);
		
	}
	
	public String toString(){
		String ret = "";//the return object
		int o;//the numbers of y axis
		String line;//each line of the grid
		System.out.println("   1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20");//the numbers of x axis
		
		
		for (int i = 0; i<SIZE; i++){		
			for (int j = 0; j<SIZE; j++){
				o=i+1;
				if (o<10){line=o+" ";}
				else{line=o+"";}
				
				
				
				if (grid[i][j].isEmpty() && j ==0){
					ret+=line+" _ ";
				}else if (grid[i][j].isEmpty() && j!=0){
					ret+=" _ ";
				
				}else if (!(grid[i][j].isEmpty()) && j==0){
					ret+=line+grid[i][j].getAnimal();
					
				}else if (!(grid[i][j].isEmpty()) && j!=0){
					ret+=grid[i][j].getAnimal();
				}
				
				
			}ret+="\n";			
		}
		
		return ret;
	}
	
	/*
	public static void main(String [] args){
			
			Grid a = new Grid();

			
			System.out.println(a);
			
			
		
	}
	*/
	
}
