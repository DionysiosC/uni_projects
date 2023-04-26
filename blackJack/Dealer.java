//settle method also prints the score of the dealer and the player

class Dealer{
	private River flow;
	private Hand dhand ;
	
	public Dealer(River flow){
		this.flow=flow;
		dhand = new Hand();
	}
	
	
	public Hand getHand(){
		return dhand;
	}
	
	public void draw(){
			Card temp = flow.nextCard();
			dhand.addCard(temp);
			
	}
	
	public void deal(Player other){
		Card temp = flow.nextCard();
		other.getHand().addCard(temp);
		
		
	}
	
	
	public void play(){
		int tempscore= dhand.score();
		while ( tempscore<17){
			this.draw();
			tempscore= dhand.score();			
		}	
	}
	

	public String toString(){
		return "Dealer: "+dhand;
	}
	
	public void settle(Player other){ 
		System.out.println("Dealer settles with "+other.getCasinoCustomer());//prints the situation
		
		System.out.println(other);//prints the hand of the dealer after he has played
		System.out.println(this);// the hand of the player
	
		
		
		int dscore =dhand.score();
		int pscore =other.getHand().score();
		boolean dealerwins = dscore>=pscore && dscore<22;//dealer's win condition
		if (!dealerwins){ 
			other.wins();
		}
		else{
			other.loses();
		}System.out.println( dscore + " "+pscore);//extra: just prints the score
		
	}
	
	
	
	
	
	
	//main
	
	public static void main(String[] args){
		River fl = new River(1);
		Dealer d = new Dealer(fl);
		d.getHand().addCard(new Card("a"));d.getHand().addCard(new Card("k"));
		d.play();
		CasinoCustomer me = new CasinoCustomer("dennis",50);
		Player p1 = new Player(me);
		d.deal(p1);d.deal(p1);
		
		
		System.out.println(p1);
		
		d.settle(p1);
		
		
		
	} 
	

}