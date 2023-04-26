import java.util.ArrayList;

class Hand{
	private ArrayList <Card> onhand = new ArrayList <Card> ();
	
	
	public void addCard(Card other){
		this.onhand.add(other);
	}
	

	public int score(){
		int thescore = 0;//return value
		int tempscore = 0;//local value 
		
		boolean dec = false;
		for (int i = 0; i < onhand.size(); i++){ //check if there is an ace in hand
				tempscore += onhand.get(i).getValue();
				if (onhand.get(i).isAce() && !dec){dec = ! dec;}
		}
		int q = tempscore +10;//local value
		if (dec && q<=21){thescore= q;}else{thescore=tempscore;}

		return thescore;		
	
	}
	

	
	public boolean canSplit(){
		if ( onhand.get(0).equals(onhand.get(1)) && onhand.size()==2 ){
			return true;
		}return false;
	
	}
	
	public Hand[] split(){
			Hand[] ret = new Hand[2];// new hand/ret value
			
			Hand a = new Hand();// hand #1
			a.addCard(this.onhand.get(0));
			
			Hand b = new Hand();// hand #2
			b.addCard(this.onhand.get(1));
			
			ret[0]=a; ret[1]=b;//fill the new "split" hand array

		return ret;
	}   					
	
	public boolean isBlackjack(){
		return ((this.score() == 21) && (onhand.size()==2));
	}
	
	public boolean isBust(){
		return (this.score()>21);
	}
	
	public String toString(){
		String c = "";
		for (int i = 0; i<onhand.size(); i++){
			c+=onhand.get(i)+ " ";
		}return c;
	}
	
	
	
	
	//main
	public static void main(String[] args){
		Hand a = new Hand();
		a.addCard(new Card("a"));a.addCard(new Card("a"));
		
		int c = a.score();
		System.out.println(a+" "+ a.canSplit()+ " "+ c);
		
		Hand [] test = a.split();
		
		for (int i = 0; i<2; i++){
			int s = i+1;
			System.out.print("Hand "+s+": "+test[i]);
		}System.out.println();
		
		test[0].addCard(new Card("k"));
		
		c = test[0].score();
		
		System.out.println(c +" " +test[0]+" BJ:"+ test[0].isBlackjack());
		
		test[0].addCard(new Card("a"));
		
		c =test[0].score();
		
		System.out.println(c +" " +test[0]+" BJ:"+ test[0].isBlackjack());
		
	}	
	
}