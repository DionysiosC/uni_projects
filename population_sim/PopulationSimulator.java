
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Scanner;

class PopulationSimulator{
	
	private Grid g = new Grid();
	private HashSet <Fox> foxes = new HashSet <Fox> ();
	private HashSet <Rabbit> rabbs = new HashSet <Rabbit>();
	private final int NUM_OF_FOXES =5;
	private final int NUM_OF_RABBITS =100;
	
	
	
	//aux methods
	private void populateGrid(){
	
		//int f = 5;
		int f = NUM_OF_FOXES ;
		while (f>0){
			Fox f1 = new Fox();
			g.addRandomly(f1); foxes.add(f1);
			f--;		
		}
	
	
		int r = NUM_OF_RABBITS;
		//int r =10;
		while (r>0){
			Rabbit r1 = new Rabbit();
			g.addRandomly(r1); rabbs.add(r1);
			r--;
			
		}
		
	
	}
	
	private void handleFoxes(){
		ArrayList <Fox> atemp = new ArrayList <Fox>();
		ArrayList <Fox> rtemp = new ArrayList <Fox>();
		
		for (Fox i : foxes){	//actions	
			i.move();			
			if (! i.starve()){
			
				if(i.timeToBreed()){
					Animal temp = i.breed();
					Fox temp1 = (Fox) temp;
					
					if (temp1!=null){atemp.add(temp1);}
					
				}	
			}else if (i.starve()){
				i.getCell().removeAnimal();
				i.setCell(null);
				rtemp.add(i);
					
			}
			
		}		
		for (Fox p:atemp){//add newborn foxes
			foxes.add(p);
		}
		for (Fox q: rtemp){//remove dead foxes
			foxes.remove(q);
		}
	}
	
	private void handleRabbits(){
		ArrayList <Rabbit> atemp = new ArrayList <Rabbit>();
		ArrayList <Rabbit> rtemp = new ArrayList <Rabbit>();	
		
		for (Rabbit i: rabbs){//actions
			if (i.isEaten()){
			    
				rtemp.add(i);
				
			}else{
				i.move();
				if(i.timeToBreed()){						
					Animal temp = i.breed();
					Rabbit temp1 = (Rabbit)temp;
					
					if (temp1!=null){
						atemp.add(temp1);
					}	
				}			
			}	
		}
		for (Rabbit p:atemp){//add newborn rabbits
			rabbs.add(p);
		}
		for (Rabbit q: rtemp){//remove eaten rabbits
			rabbs.remove(q);
		}
	}
	//
	
	public void simulate(int temp){
		int ans = temp;
		Scanner input;
		populateGrid();
		int sum;
		
		do{			
			System.out.println("Foxes in the grid: "+ foxes.size() + "    new step     "+"Rabbits in the grid: "+rabbs.size());
			System.out.println(g);
			System.out.println("Hit ENTER to proceed: ");
			input = new Scanner(System.in);
			input.nextLine();
			handleFoxes();
			handleRabbits();
			ans--;
			sum = foxes.size()+rabbs.size();
		}while (ans>0 && sum<400 );
		
	}
	
	
	
	//main
	public static void main(String [] args){
		
		PopulationSimulator p = new PopulationSimulator();
		p.simulate(10);
		
		
	}
	
	
	
}
