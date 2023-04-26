class CasinoCustomer{
	private String name;
	private double money;
	
	public CasinoCustomer(String name, double money){
		this.name=name;
		this.money=money;
	}
	
	public void payBet(double amount){
		this.money-=amount;
	}
	
	public void collectBet(double amount){
		this.money+=amount;
	}
	
	public boolean 	canCover(double amount){
		return amount<=this.money;
	}
	
	public boolean isBroke(){
		return money<1;
	}
	
	public String toString(){
		return ""+name;
	}
	
	public void printState(){
		System.out.println(name+" has  "+money + " left.");
	}
	
}