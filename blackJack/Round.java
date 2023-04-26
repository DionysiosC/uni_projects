import java.util.Scanner;
import java.util.ArrayList;
class Round{
	private Dealer thedealer;
	private ArrayList <Player> playerlist ;
	private ArrayList <Player> gottasettle ;
	
	public Round(River a){
		this.thedealer = new Dealer (a);
		playerlist = new ArrayList <Player> ();
		gottasettle = new ArrayList <Player> ();
	}
	
	public void addPlayer(CasinoCustomer b){
		playerlist.add(new Player(b));		
	}
	
	public void playRound(){
		System.out.println("<-------------------------  New Round -------------------------> ");
		
		for (int i = 0; i<playerlist.size(); i++){//every player  place bet
			playerlist.get(i).placeBet();	
		}
		
		for (int i = 0; i<playerlist.size(); i++){//1st deal of cards
			thedealer.deal(playerlist.get(i));
			//playerlist.get(i).getHand().addCard(new Card("k"));
		}
		
		thedealer.draw();System.out.println(thedealer);//1st dealer's card --> can be seen
		
		for (int i = 0; i<playerlist.size(); i++){//2nd deal of cards
			thedealer.deal(playerlist.get(i));
			//playerlist.get(i).getHand().addCard(new Card("k"));
		}	
			
		thedealer.draw();//2nd dealer's card-->cannot be seen
		
		for (int i = 0; i<playerlist.size(); i++){ //prints the hand of each player
			System.out.println(" * "+playerlist.get(i) + " * ");
		}
		
		boolean cond = thedealer.getHand().isBlackjack();//dealer blackjack cond
		if (cond){			
			System.out.println("Dealer has Black Jack!");
			for (int i = 0; i<playerlist.size(); i++){//the players who don't have blackjack pay the bet, round ends
				if (!playerlist.get(i).getHand().isBlackjack()){ 
					playerlist.get(i).loses();
				}else{ 
					System.out.println(playerlist.get(i)+" has blackjack so he gets his money back");
				}
				
			}System.out.println();
			
		}else{ //game goes on...
			
			System.out.println("Dealer doesn't have BlackJack.Game goes on...");
			for (int i = 0; i<playerlist.size(); i++){ //checks if any player has a natural
				
				boolean cond2 = playerlist.get(i).getHand().isBlackjack();				
				if (cond2){
					 playerlist.get(i).winsBlackJack();
					 
				}else{  // the player-who-doesn't-have-blackjack 's turn
					System.out.println();
					System.out.println("Time for "+ playerlist.get(i).getCasinoCustomer()+ " to play.");
					System.out.println(playerlist.get(i));
					playPlayer( playerlist.get(i)); // the player plays
					
				}System.out.println();
			}	
		}
		
		if (gottasettle.size() != 0){		// dealer settles if necassary/if the gottasettle list is not empty
			thedealer.play();//dealer is playing
			System.out.println("The dealer has played: "+thedealer);
			System.out.println();
			
			boolean burntdeal = thedealer.getHand().score()>21;			
			if (burntdeal){// checks if the dealer has lost or has to settle
				
				System.out.println("The dealer has lost. Players who have not lost or won blackjack get the bet...");
						for (int i = 0; i<gottasettle.size(); i++){ 
							gottasettle.get(i).wins();
						}
				
				
			}else{
				
				for (int i = 0 ; i < gottasettle.size() ; i++){ 				
					thedealer.settle(gottasettle.get(i));		
					System.out.println();
				}
			}	
		
		}	
	}
	

	
	//aux methods
	private void playPlayer(Player other){					
		if ( other.wantsToSplit()){
			playSplitHand(other);
			
		}else if(other.wantsToDouble()){
			playDoubleHand(other);
			
		}else{
			playNormalHand(other);
								
		}
	}	
	
	private void playNormalHand(Player other){
		
		
		Scanner in = new Scanner(System.in);	
		String ans ="";
		boolean cond1 = ans.equals("y");
		boolean cond2 = other.getHand().isBust();
		boolean cond3 = other.getHand().isBlackjack();
		
		while(true ){ //the player is taking cards while he has not lost, has not a blackjack or has not decided to stand
			System.out.println(other +". Hit? (y/n)");
			ans = in.next();	
			cond1 = ans.equals("y");
			
			if (cond1 ){
				thedealer.deal(other);				
				cond2 = other.getHand().isBust();
				cond3 = other.getHand().isBlackjack();
			}else{break;}
			
			if ( cond2){break;}
			else if (cond3){break;}
								
		}
		
		if (cond3){System.out.println(other); other.winsBlackJack();}
		else if (cond2){System.out.println(other); other.loses();}
		else if (!cond1 && !cond2) {gottasettle.add(other); }
	}
	
	private void playDoubleHand(Player other){
		other.doubleBet();
		thedealer.deal(other);
		System.out.println(other);
		
		if (other.getHand().isBust()){other.loses();}
		else{gottasettle.add(other);}
	}
	
	private void playSplitHand(Player other){
		
		Hand[] a = other.getHand().split(); // the new hand 
		
		Player other1 = new Player(other.getCasinoCustomer(),a[0], other.getBet()  );  

		Player other2 = new Player(other.getCasinoCustomer(), a[1], other.getBet() );
		
		
		System.out.println(other.getCasinoCustomer() + " has decided to split: ");
		System.out.println(other.getCasinoCustomer()+" is now playing with this hand: "+a[0]);
		playNormalHand(other1); //plays with the first of the two new hands
		
		System.out.println(other.getCasinoCustomer()+" is now playing with this hand: "+a[1]);
		playNormalHand(other2);// plays with the second of the new hands
		
		
	}
	//end of aux methods
	
	
	
	
	



//main


	public static void main(String [] args){
		River riv = new River(1);
		Round roun = new Round(riv);
		CasinoCustomer a = new CasinoCustomer("adam", 50);roun.addPlayer(a);
		CasinoCustomer b = new CasinoCustomer("eva", 40);roun.addPlayer(b);
		roun.playRound();
	
		
		
		
	}

}