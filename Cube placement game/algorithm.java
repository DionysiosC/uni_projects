/*
	This is the class where the core of the game is being managed. The "while-loops" of the
	game are written below.
	
	An important fact is that a PriorityQueue is used to keep the frontier ordered according the sum of this distance and the value of the 
	heuristic function of each node. 
		
	The getPath() function is used to create and return an ArrayList of the nodes that 
	the resulting path contains.
	
	Useful links:
	https://www.geeksforgeeks.org/double-compare-method-in-java-with-examples/
	https://www.geeksforgeeks.org/implement-priorityqueue-comparator-java/
*/
import java.util.*;

public class algorithm {
	private PriorityQueue<Node> frontier;
	private ArrayList <Node> visited;

	private Node start; // the root node
	private Node current; // the current node of the runtime, where the final node will be stored
	private Node final_node; // the target node, which holds the desired final state of cubes
	private int mode; // 0 for ucs, 1 for A star
	
	public algorithm(ArrayList <Cube> state, int mode){
		this.mode=mode;
		this.current = null;
		
		// priority queue data struct: frontier.poll() to extract and frontier.add(Node n) to insert node ! The node with the lowest cost or h(n) is the first one!!!
		frontier = null;

		this.start = new Node(state);
		
		if (!start.isValid()){
			System.out.println("Root not valid");
			System.exit(0);
		}

		// 2. The visited nodes list (and the list that holds the path) is initialised
		visited = null;
		
		// Create final node
		final_node = start.createFinalState();
		
		
		for (int i=0; i<start.getStateSize(); i++){
			start.getCube(i).setFinalPos(final_node.getCube(i).getCoordinates());
		}
		
		
		if (mode==1){
			start.computeH(final_node);
			System.out.println("Heuristic value of root (Estimation of distance between root & goal state):" + start.getH());
		}
		
		System.out.println("Final Node: "+final_node);
	}

	public void run_astar(){
		ArrayList <Node> newNodes; 
		double mindist=-1;
		int flag = 1;
		visited = new ArrayList<Node> ();
		
		frontier =  new PriorityQueue<Node>(new Comparator<Node>() {
												public int compare(Node n1, Node n2) {
													return Double.compare(n1.getE(), n2.getE());
												}
											});			
			
		start.computeH(final_node);
		frontier.add(start);	
		int test_c=0;
		while (!frontier.isEmpty()){
			
			//test_c ++;
			//System.out.println("It. no."+test_c);
			
			do {	
				
					// 3. Get the first in line node from the frontier (the frontier is ordered so we skip step 9)
				current = frontier.poll();
					
				//System.out.println(frontier);
				//System.out.println(newNodes);
				//System.out.println("Current node:\n"+current);
				
				// 4. If there is a node in the visited list that has the same placement of cubes as the current node has
				
				flag = 0;
				for (Node j: visited){
					if (j.equals(current)){
						flag=1;
						break;
					}
				}
				
			}while (flag==1);
				
			mindist = current.getDist();
			// 5. If current node is final
			if (current.equals(final_node)){
				System.out.println("Solution found! Distance from root: "+mindist);
				break;
			}
			
			// 6. Expand the current node 
			newNodes = current.expandNode();			
			
			// 7. Calculate the heuristic value (and the distance between a child and the root)
			for (Node i: newNodes){
				i.computeH(final_node);
			}
			
			//8. Add the children to the frontier + 9. Reorder the frontier so the node with minimum g(n) + h(n) is first
			for (Node i: newNodes){
				frontier.add(i);
			}			
			
			// 10. Add the parent node to the visited list
			visited.add(current);
			
		}
	}
	
	public ArrayList <Node> getPath(){
		ArrayList <Node> result = new ArrayList <Node>();
		while (current.getParent()!=null){
			result.add(current);
			current = current.getParent();
		}
		result.add(start);
		return result;
		
	}
	
 
}