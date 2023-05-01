
import java.util.Scanner;

class  Simulation{
	public static void main(String [] args){
		System.out.println("----------------------Welcome to my simulation----------------------");
		System.out.println("Give the number of steps: ");
		Scanner input = new Scanner(System.in);
		int ans = input.nextInt();
		while (ans<=0){// a check
			System.out.println("Give a VALID number of steps: ");
			input = new Scanner(System.in);
			ans = input.nextInt();
		}
		
		PopulationSimulator p = new PopulationSimulator();
		p.simulate(ans);
		
		System.out.println("\n"+"------------------------End------------------------");
	}
	
}