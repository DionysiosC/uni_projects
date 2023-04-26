import java.util.Random;

class River{
	private Card [] deckCards;
	private int cardsLeft;
	private int totalcards;//numberOfCards
	
	public River(int decks){		
		this.totalcards = 52*decks;   
		this.cardsLeft= (52*decks);
		int quads = decks*4;
		String [] temp = {"a","2","3","4","5","6","7","8","9","10","j","q","k"};//13 se size, 12 theseis
		deckCards = new Card [totalcards];
				
		int size = 0;
		while (size<totalcards ){
			for (int i = 0; i<13; i++){
				deckCards[size]=new Card(temp[i]);
				size++;
			}	
		}
	} 
	
	public Card nextCard(){
		if (cardsLeft==0){return null;}
			Random rand = new Random();
			int lim = cardsLeft-1;
			int pos = rand.nextInt(lim + 1);
			Card retval = deckCards[pos];
			
			Card temp = deckCards[pos];			//swap
			deckCards[pos] = deckCards[lim]; 
			deckCards[lim]=temp;
			
			cardsLeft--;
			
		return retval;

	}
	
	public boolean shouldRestart(){
		return ((cardsLeft*4) < (totalcards));
	}
	
	public void restart(){
		cardsLeft=totalcards;
	}
	
	
	
	//main
	public static void main(String[] main){
		River a = new River(1);

		while (!a.shouldRestart()){
				Card b = a.nextCard();
				System.out.print(b + " ");	
			}a.restart();
			
		System.out.println("Restarted");
		
		Card b = a.nextCard();
		boolean cond = (b==null);
		while (!cond){
				b = a.nextCard();
				System.out.print(b + " ");	
				cond = (b==null);
		}	
		
	}
	
	
}