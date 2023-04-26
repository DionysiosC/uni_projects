import java.util.Scanner;

class Player{
	private CasinoCustomer a;
	private Hand xeri;
	private double bet;
	
	public Player(CasinoCustomer a){
		this.a=a;
		this.xeri=new Hand();
		this.bet = 0;
	}
	
	public Player(CasinoCustomer a, Hand xeri, double bet){
		this.a=a;
		this.xeri=xeri;
		this.bet=bet;
		
	}
	
	//accessor methods
	public CasinoCustomer getCasinoCustomer(){
		return a;	
	}
		
	public Hand getHand(){
		return xeri;
	}
	
	public double getBet(){
		return bet;
	}
	//
	
	
	public void wins(){
		System.out.println(a+" wins! Gets " + bet);
		a.collectBet(bet);
	}
	
	public void winsBlackJack(){
		double q = 1.5*bet;
		System.out.println(a+" wins blackjack! Gets "+ q );
		a.collectBet(q);
	}
	
	
	public void loses(){
		System.out.println(a+ " has lost! Pays "+bet);
		a.payBet(bet);
		
	}
	
	public void placeBet(){
		
		a.printState();
		System.out.println(" Place a bet!");
		Scanner input =new Scanner(System.in);
		double b = input.nextDouble();
		boolean cond = a.canCover(b) && b>=1;
		while ( !cond ){
			a.printState();
			System.out.println(" Place a valid bet!");
			 b = input.nextDouble();
			 cond = a.canCover(b) && b>=1;
			  
		}
		bet+=b;		
	}
	
	
	public void doubleBet(){
		bet*=2;
	}
	
	public boolean wantsToDouble(){
		Scanner in =new Scanner(System.in);
		String c="";
		if ( a.canCover(2*bet) ){
			System.out.println(a+ " can double bet. Would like to? (y/n)");
			c = in.next();
			return c.equals("y");
			
		}else{
			System.out.println(a+" cannot double bet");
			return false;
		}
	}
	
	public boolean wantsToSplit(){
		Scanner in =new  Scanner(System.in);
		String c="";
		if ( a.canCover(2*bet) && xeri.canSplit() ){
			System.out.println(a+ " can split. Would like to? (y/n)");
			c = in.next();
			return c.equals("y");
		}else{
			System.out.println(a+ " cannot split");
			return false;
		}

	}
	
	
	public String toString(){
		return ""+a+" has this hand: "+ xeri;
		
	}
	
	
	//main
	
	
	public static void main(String [] args){
		CasinoCustomer  dennis = new CasinoCustomer("Dennis", 50);
		Player obj = new Player(dennis);
		
		obj.placeBet();
		obj.wantsToSplit();
		System.out.println(dennis+ " "); dennis.printState();
		obj.wantsToDouble();
		dennis.printState();
		obj.wins();
		dennis.printState();
		obj.winsBlackJack();
		dennis.printState();
		obj.loses();
		
		
		
	}
	
	
}