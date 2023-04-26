
import java.util.Scanner;

class BlackjackTable{
	private River flow;
	private CasinoCustomer[] custlist;//the players
	private int  playernum;//the number of players
	
	public BlackjackTable(int playernum){
		this.playernum=playernum;
		flow = new River(6);
		custlist = new  CasinoCustomer[playernum];
		int p =0;
		for (int i = 0; i<playernum; i++){
			p++;
			System.out.println("For player number "+p+":");
			custlist[i] = (createCasinoCustomer());
			System.out.println();
			
		} 
		
	}
	
	private CasinoCustomer createCasinoCustomer(){ 
		
			Scanner in = new Scanner(System.in);
			System.out.println("Give name and initial amount of player : ");
			String tempname = in.next();
			
			double tempamount = in.nextDouble();
			while (tempamount<0){System.out.println("Invalid amount. "); tempamount = in.nextDouble(); } //checks if the amount given is valid (a positive)

		return new CasinoCustomer(tempname, tempamount);
		
		
	}
	
	public void play(){
		Round roun;//the new round variable, used below

		
		int c = 0; //the number of the broke players
		int active = playernum - c;//the number of the active players
		while (active>=1 ){//the game goes on while there are active players (at least one)
			
			if (flow.shouldRestart()){flow.restart();}
			
			roun = new Round(flow);
			
			c=0;
			//updating the useful variables below
			for (int i = 0; i<playernum; i++){
				if( custlist[i].isBroke()  ){c++;}else{ roun.addPlayer(custlist[i]); }
			}
			active = playernum - c;
			roun.playRound();
		
		
		}
	}
}