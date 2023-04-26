class Card{
		private String card;
		private int val;
		
		public Card(String card){
			this.card=card;
			this.val=this.getValue();
		}
		
		public boolean isAce(){
			if (card.equals("a")){
				return true;
			}
			return false;
		}
		
		public int getValue(){
			boolean cond1 =this.card.equals("k") || this.card.equals("q")||this.card.equals("j");
			
			if (this.isAce()){
				return 1;
			}else if(cond1){
				return 10;
			}else{
				return Integer.valueOf(this.card);
			}
			
		}
		
		public String toString(){
			return ""+card;
		}
		
		public boolean equals(Card other){
			return (this.card.equals(other.card));
		}
		
		
	/**public static void main(String [] args){
		Card a = new Card("8");
		Card b = new Card("8");
		System.out.println(a);
		System.out.println(a.isAce());System.out.println(a.equals(b));
		
	}**/
}
