
import java.util.Scanner;

class  Blackjack {
	public static void main(String [] args){
		System.out.println("------------------------- Welcome to my casino!------------------------- ");
		Scanner in = new Scanner(System.in);

		System.out.print("Please state the number of the total players:  "); int num = in.nextInt();System.out.println();
		
		BlackjackTable thetable = new BlackjackTable(num);//creates the blackjack table
		thetable.play();//runs the game
		
		System.out.println();
		System.out.println("-------------------------Thanks for playing ------------------------- ");

	}
}
