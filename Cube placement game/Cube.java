/*
	This is the class that describes the characteristics and behavior of a cube of the table.
	
	The fields here are the x,y coordinates, the list that contains a reference to each neighbor
	of the cube, the number of it and a matrix that holds the final position of the cube on the table.
	i.e. if the game is being run for 6 cubes, the final_pos matrix of cube no. 6 contains this data:
	[2,3].
	
	The most basic functions here are the checkForNeighbors(...) that update the neighbor list of
	the current cube and the neighboring ones, and the findFreeTops(...) that checks the input ArrayList
	that represents a placement of cubes on the table for valid free spots on the whole table.
*/
import java.util.ArrayList;
public class Cube{
	private int x,y;
	private Cube neighbors []; // pos.0: up, pos.1: left, pos.2: right, pos.3: down
	int num;
	int final_pos[]; // useless field when UCS is used
	
	public Cube(int x, int y, int num){
		this.num = num;
		this.x=x;
		this.y=y;	
		this.neighbors=new Cube[4];		
		this.neighbors[0] = null; this.neighbors[1] = null; this.neighbors[2] = null; this.neighbors[3] = null;
		
		this.final_pos = new int[2];
		final_pos[0] = -1; final_pos[1] = -1;
		
	}
	
	public void setFinalPos(int [] f){
		final_pos[0]=f[0]; final_pos[1]=f[1];
	}
	
	public int [] getFinalPos(){
		return final_pos;
	}
	
	public boolean isFree(){
		return (neighbors[0]==null);
	}
	
	public void setCoordinates(int x, int y){
		this.x=x;
		this.y=y;				
	}
	
	public int [] getCoordinates(){
		int arr [] = new int [2];
		arr[0]=x; arr[1]=y;
		return arr;
	}
	
	public void setNeighbor(int n, Cube c){ //n defines the neighbor aka the position in the neighbors matrix
		this.neighbors[n] = c;		
	}
	
	public int getNumber(){
		return num;
	}
	
	public Cube getNeighbor(int n){ //n defines the neighbor aka the position in the neighbors matrix
		return neighbors[n];
	}
	
	public void checkForNeighbors(ArrayList <Cube> arr){
		int current_coords [] =  new int [2];
		
		for (Cube i:arr){
			current_coords = i.getCoordinates();
			
			if (current_coords[0]-1 == x &&  current_coords[1] == y){ // check if this is the left neighbor of i
				i.setNeighbor(1,this);
				neighbors[2] = i;
				
			}else if (current_coords[0]+1 == x &&  current_coords[1] == y){ // check if this is the right neighbor of i
				i.setNeighbor(2,this);
				neighbors[1] = i;
				
			}else if (current_coords[0] == x &&  current_coords[1] == y+1){ // check if this is the neighbor above of i
				i.setNeighbor(3,this);
				neighbors[0] = i;
				
			}else if (current_coords[0] == x &&  current_coords[1] == y-1){ // check if this is the neighbor below of i
				i.setNeighbor(0,this);
				neighbors[3] = i;
				
			}
		}
		
	}

	public int [][] findFreeTops(ArrayList <Cube> array){
		int k = array.size()/3;
		int l = k*4;
		
		int arr [][] = new int [l][2]; // the total free spots for a cube (if arr[i][0]=-1 or arr[i][1] = -1, the spot is not available)
		ArrayList <Cube> temp;
		
		for (int i=0; i<l;i++){
			if (i+1==x){
				arr[i][0]=-1;arr[i][1]=-1;
				
			}else{				
				temp = new ArrayList <Cube>();
				//System.out.println("This is find spots: x="+x);
				
				//check the free spots for each column
				for (Cube a:array){
					if (a.getCoordinates()[0]==i+1){
						temp.add(a);
					}
				}
				
				if (temp.size()==0){
					arr[i][0]=i+1;
					arr[i][1]=1;
				}else if(temp.size()==1){
					arr[i][0]=i+1;
					arr[i][1]=2;				
				}else if(temp.size()==2){
					arr[i][0]=i+1;
					arr[i][1]=3;				
				}else{
					arr[i][0]=-1;
					arr[i][1]=-1;
				}
			}
			
		}
		return arr;
	}	

	public String toString(){
		String result ="";
		int x,y;
		x = this.getCoordinates()[0]; y = this.getCoordinates()[1];
		result += "This is the cube with number "+this.getNumber()+" at x="+x+" and y="+y+"\nNeighbors:\n";
		
		if (neighbors[0] != null){
			x = neighbors[0].getCoordinates()[0]; y = neighbors[0].getCoordinates()[1];
			result += "Neighbor above at x="+x+" and y="+y+" with number :"+ neighbors[0].getNumber() +" (it's free?)" +this.isFree()+ "\n";
		}else{
			result+= "No neighbor above! (it's free?)" +this.isFree()+ "\n";
		}

		if (neighbors[1] != null){
			x = neighbors[1].getCoordinates()[0]; y = neighbors[1].getCoordinates()[1];
			result += "Neighbor at the left at x="+x+" and y="+y+" with number :"+ neighbors[1].getNumber() +"\n";
		}else{
			result+= "No neighbor at the left!\n";
		}

		if (neighbors[2] != null){
			x = neighbors[2].getCoordinates()[0]; y = neighbors[2].getCoordinates()[1];
			result += "Neighbor at the right at x="+x+" and y="+y+" with number :"+ neighbors[2].getNumber() +"\n";
		}else{
			result+= "No neighbor at the right!\n";
		}
		
		if (neighbors[3] != null){
			x = neighbors[3].getCoordinates()[0]; y = neighbors[3].getCoordinates()[1];
			result += "Neighbor below at x="+x+" and y="+y+" with number :"+ neighbors[3].getNumber() +"\n";
		}else{
			result+= "No neighbor below! (sittin' on table)\n";
		}
		
		return result;
	}
	
	public boolean compare(Cube c){
		return (this.getCoordinates()[0] == c.getCoordinates()[0] && this.getCoordinates()[1] == c.getCoordinates()[1] );		
	}	
	
	
}