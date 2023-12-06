/*
	This is a class that is used as the "back-end" of our program.
	
	This is where the game runs according to the desire of the user, although it 
	interacts with the user as well since through the constructor the user
	is prompted to enter the coordinates of each cube etc.
	
	Functions:
		public void printInitialState() that is used for debugging and prints details
	about the root node
		public void printInfo() that is used to display the results of our program. It 
	is used to display the total runtime, extensions etc.
		
*/
// A hint to get a pair of number from user in java: https://stackoverflow.com/questions/21665910/java-scan-coordinates-as-x-y-format & https://stackoverflow.com/questions/18819497/reading-in-multiple-coordinates-on-one-line-java
// File operations: https://www.w3schools.com/java/java_files_create.asp
import java.util.*;
import java.io.*;

public class Engine{
	private ArrayList <Cube>  state;
	private int k;
	private int l,n,mode;
	private ArrayList <Node> path1;
	private ArrayList <Integer> x_coords;
	private long startTime, endTime, duration;
	private algorithm game;
	
	

	// 1st row is number 1 and 1st column is number 1
	public Engine(int k) {
		this.k = k;
		this.n = 3 * k;
		state = new ArrayList <Cube> ();
		
		Scanner in;
		String input;
		String coordinates[];
	
		mode = 0; // mode refers to the use of A* and UCS algorithm in the future
		
		int i, x, y, counter=1;
		//in.nextLine(); // for the \n
		in = new Scanner(System.in);
		for (counter = 1; counter <= n; counter++) {
			
			System.out.println("Give me coordinates (x,y) for cube no." + counter);
			input = in.nextLine();

			coordinates = input.split(",");
			x = Integer.parseInt(coordinates[0].trim());
			y = Integer.parseInt(coordinates[1].trim());
			
			state.add(new Cube(x,y,counter));

		}

		for (Cube c: state){
			c.checkForNeighbors(state);
		}
	
	}
	

	public void printInitialState(){
		int arr[][];
		
		Node root = new Node(state);
		
		System.out.println("Is the root valid? "+root.isValid());
		if (!root.isValid()){
			System.exit(0);
		}
		Node n = root.createFinalState();
		System.out.println("Final state:"+n);
		System.out.println("Is the root final? "+root.equals(n));
		if (root.equals(n)){
			System.exit(0);
		}
		/*
		System.out.println("Expanding root...");
		ArrayList <Node> test = root.expandNode(0);
		*/
		
		System.out.println("Root: \n"+root);
		//System.out.println("Root expansion test: \n"+test);	
		//System.out.println("Size of test:"+test.size());

						
		System.out.println("\n\nDetails of the initial state is:");
		for (Cube i:state){
			System.out.print("["+i.getCoordinates()[0]+" "+i.getCoordinates()[1]+"]");
			System.out.println(i);
			System.out.println();
			
		}System.out.println();
		
	}
	
	public void printInfo(){
		File myObj; FileWriter myWriter;
		
		int c;
		if (game != null){
			path1 = game.getPath();
			if (path1 == null || path1.size()==1){
				System.out.println("No solution found");
				System.exit(0);
			}
			
			c=path1.size()-1;
			
			if (path1.size()<30){
				System.out.println("Final path is:\n");
				
				for (Node i:path1){
					System.out.println((c--)+":"+i+"\n");
				}
			}else{
				System.out.println("The path is too large to print it here. Thus, a file (path.txt) has been created!");
				
				try {
					
					myObj = new File("path.txt");
						
					if (myObj.createNewFile()) {
						System.out.println("File created: " + myObj.getName());
					} else {
						System.out.println("File already exists.");
					}
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
				
				try {
					myWriter = new FileWriter("path.txt");
					
					myWriter.write("Calculated path:\n");
					for (Node i:path1){
						myWriter.write((c--)+":"+i+"\n");
					}		
					myWriter.write("End\n");					
					
					myWriter.close();
					System.out.println("Successfully wrote to the file.");
					
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
  
				
				
			}
			
			System.out.println("Total extensions: "+(path1.size()-1));
			System.out.println("Total time needed for A* to run: "+duration+" ms");
		}
		
	}
	
	public void playGame(){
		
		game = new algorithm(state,mode);
		
		startTime = System.nanoTime();
		game.run_astar();
		endTime = System.nanoTime();
		
		duration = ((endTime - startTime)/1000000 ); // calculate in ms 
		
	}
	

	
}