/*
	This is the class that describes the characteristics and behavior of a node of the tree.
	
	The fields here are a reference to the parent node (if it's null,then the node is the root node),
	an ArrayList that stores a placement of the cubes, the distance from the root node according to the
	previous moves and the rules and the value of the heuristic function for this node.
	
	The most basic functions here are the expandNode() that returns a list of the children that this node
	creates according to the rules, the calculateDist() that calculates the distance of the node from
	its parent node only (to get the total distance from the root we use the addCost(...)) and the isValid()
	function that checks if a node is valid according to the logic of the game i.e if the cubes are placed 
	between the borders	of the table, if no cube is "hovering" etc.
	
	The constructor is overloaded to be used when we want to define nodes that have a parent node.
*/
import java.util.*;

public class Node {
	private Node parent;
	private ArrayList <Cube> cubes;
	private double dist, h;
	
	//constructor used mainly for the root node
	public Node(ArrayList <Cube> state){
		this.parent = null;
		this.dist = 0;	
		this.h=0;
		cubes = new ArrayList <Cube> ();
		
		for (Cube i:state){
			cubes.add(i);
		}
		
		
	}
	
	//general use constructor
	public Node(ArrayList <Cube> state, Node parent){
		this.parent=parent;
		this.dist = parent.getDist();
		this.h=0;
		cubes = new ArrayList <Cube> ();
		
		for (Cube i:state){
			cubes.add(i);
			
		}
		
	}
	
	public int getStateSize(){
		return cubes.size();
	}
	
	public double getH(){
		return h;
	}
	
	public void setH(double c){
		this.h=c;
	}
	
	public Cube getCube(int i){
		return cubes.get(i);
	}
	
	public Node getParent(){
		return parent;
	}	
	
	public double getDist(){
		return dist;
	}
	
	public void setDist(double c){
		dist=c;
	}
	
	public double getE(){
		return dist+h;
	}
	
	public Node createFinalState(){
		int n = cubes.size(), i;
		int k = n/3;
		ArrayList <Cube> result = new ArrayList <Cube>();
		
		for (i=1; i<=k; i++){
			result.add(new Cube(i,1,i));
			
		}
		
		for (i=1; i<=k; i++){
			result.add(new Cube(i,2,i+k));
			
		}		

		for (i=1; i<=k; i++){
			result.add(new Cube(i,3,i+2*k));
			
		}	

		for (Cube x:result){
			x.checkForNeighbors(result);
			
		}	

		Node final_node = new Node(result);
		
		
		return final_node;		
		
		
	}
	
	public boolean isValid(){
		int k = cubes.size()/3;
		//System.out.println("isvalid got k="+k);
		for (Cube i: cubes){
			if (i.getCoordinates()[0]<0 || i.getCoordinates()[1]<0){
				return false;
			}
			
			if (i.getCoordinates()[0]>(4*k) && i.getCoordinates()[1]==1){
				//System.out.println("Not valid node (out of table bounds, lvl. 1, bc x = "+i.getCoordinates()[0]+" and y = "+i.getCoordinates()[1]+" for cube no."+i.getNumber() );
				return false;
			
			}
			
			if (i.getCoordinates()[0]>k && i.getCoordinates()[1]>1){
				//System.out.println("Not valid node (out of table bounds, lvl. 2 or 3, bc x = "+i.getCoordinates()[0]+" and y = "+i.getCoordinates()[1]+" for cube no."+i.getNumber() );
				return false;
			
			}
			
			if(i.getCoordinates()[1]>1){
				if (i.getNeighbor(3)==null || (i.getNeighbor(3).getNeighbor(3)==null && i.getCoordinates()[1]==3) ){
					//System.out.println("Not valid node (hovering) , bc x = "+i.getCoordinates()[0]+" and y = "+i.getCoordinates()[1]+" for cube no."+i.getNumber() );
					return false;
					
				}
			}
			
			for (Cube j: cubes){
			//System.out.println("False cause i same as j? "+ i.equals(j) + " and their numbers are different? " +  (i.getNumber() != j.getNumber()) + "\nCube i:\n"+i+"\nCube j:\n"+j);
				if (i.compare(j) && i.getNumber() != j.getNumber()){
					//System.out.println("Not valid node (duplicate cubes):\n"+i+"\n"+j+"\n");
					return false;
				}
				
			}
			
		}
		
		
		
		return true;
		
	}
	
	public ArrayList <Node> expandNode(){
		// for each FREE cube of the node (1) find every possible move (2) for every VALID possible move (a) calculate dist (and the value of the heuristic func if needed)
		// (b) create a new node and (c) add it to the result array
		Node final_state = this.createFinalState();
		
		ArrayList <Node> result = new ArrayList <Node> ();
		ArrayList <Cube> temp;
		int free_spots [][], l = (cubes.size()/3)*4;
		Node child;
		Cube current;
		double newDist;
		
		for (Cube a:cubes){
			if (a.isFree()){
				free_spots = a.findFreeTops(cubes);
				/*
				System.out.println("Free spots for cube no."+a.getNumber());
				for (int i=0; i<free_spots.length; i++){
					System.out.println("["+free_spots[i][0]+","+free_spots[i][1]+"]\n");
				
				}
				*/
				
				// For every possible move, create a child and if valid, add it to result
				for (int i=0; i<l; i++){
					if (free_spots[i][0]>0){
						
						temp = new ArrayList <Cube> ();
						for (Cube b:cubes){
							temp.add(new Cube(b.getCoordinates()[0], b.getCoordinates()[1], b.getNumber()));
						}
						
						temp.get(a.getNumber()-1).setCoordinates(free_spots[i][0], free_spots[i][1]);
						
						for (Cube b: temp){
							b.checkForNeighbors(temp);
						}
						
						child = new Node(temp, this);
						
						if (child.isValid()){
							child.setDist(this.calculateDist(a.getCoordinates()[1], free_spots[i][1], this)); // New distance is being calculated anyway since we need for UCS and A* as well
	
							result.add(child);
						}
					}
					
				}
				
			}
		}
		
		return result;
	}
		
	public boolean equals(Node n){
		for (int i=0; i<cubes.size();i++){
			if ( !cubes.get(i).compare(n.getCube(i)) ) 
				return false;
		}return true;		
	}
	
	private double calculateDist( int y1, int y2, Node parent){
		
		if (y2>=y1)
			return 0.75 + y2-y1 + parent.getDist();
		else
			return 0.75+ 0.5*(y1-y2) + parent.getDist();
		
		
	}
		
	public double heuristic(Cube c){
		
		int y1 = c.getCoordinates()[1];
		int y2 = c.getFinalPos()[1];
		
		int x1 = c.getCoordinates()[0];
		int x2 = c.getFinalPos()[0];
		
		if (y2==y1 && x1==x2)
			return 0;
		else
			return ( 0.75 + 0.5*Math.abs(y2-y1) );
		
	}
	
	public void computeH(Node f){		
		double sum=0;
		for (Cube i:cubes){
			
			i.setFinalPos(f.getCube(i.getNumber()-1).getCoordinates());
			sum += this.heuristic(i);
			
		}
		this.setH(sum);
	}
	
	public String toString(){
		String result = "	<-- Distance from root: "+this.getDist()+" and heuristic function value: "+ this.getH() +" and total cost: "+this.getE();
		
		result += " and { Current state of cubes of this node: ";
		for (Cube i: cubes){
			result += "["+i.getCoordinates()[0]+","+ i.getCoordinates()[1]+"] ";
			
		}result += "} -->\n ";
		//result = "Am I visited? "+visited;
		
		return result;
	}

}