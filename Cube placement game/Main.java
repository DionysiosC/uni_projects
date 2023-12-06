/*
	This is a class that implements the basic interface that interacts with the user.
	This is the class that has our main class implemented.
	It basically creates an Engine instance and calls the most basic functions of the game.
	
	Functions: public static void main(String [] args)
*/
import java.util.*;

public class Main{
	public static void main(String [] args){
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to the cube placing game"+'\n'+"Give me a base number to calculate the number of cubes:");
		int ans = in.nextInt();
		System.out.println("Total number of cubes: "+3*ans+"\nProgram begins...");
		
		Engine eng = new Engine(ans);
		//eng.printInitialState();
		eng.playGame();
		eng.printInfo();
		
	}	
}