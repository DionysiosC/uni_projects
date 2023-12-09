import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class main{
	public static void main(String [] args){
		Scanner in = new Scanner(System.in);
		int sequence[];
		boolean cond;
		ArrayList <int[]> graph;
		System.out.println("Please give me an order (how many edges):  "); int num = in.nextInt();
		System.out.println("Chosen order: "+num);
		
		// create sequence phase
		sequence = createSeq(num);
		
		// -- Some known sequences
		// sequence = new int[]{6,5,5,5,4,4,2,1}; //graphic
		// sequence = new int[]{7,6,5,5,5,4,4,2}; // graphic
		// sequence = new int[]{8,7,6,6,5,3,2,2,2,1}; //not graphic
		// sequence = new int[]{4,4,4,3,3,2}; //graphic
		// sequence = new int[]{4,3,2,1,1,1}; // graphic
		
		/* -- Test the order2 method
		int test [] = new int[]{0,3,4,0,0,2} ;
		int l = 3;
		order2(test);
		System.out.println("Test: ");
		for (Integer p: test){
			System.out.print(" "+p);
		}System.out.println();
		*/
		
		System.out.print("The sequence is: ");
		for (Integer i: sequence){
			System.out.print(i+" ");
		}System.out.println();

		// check if graphic phase
		cond = isGraphical(sequence);
		System.out.println("Is it graphic? : "+ cond);
		
		// design phase
		if (cond){			
			graph = design(sequence);
			System.out.print("The edges are: ");
			for (int [] p: graph){
				System.out.print(" ("+p[0]+", "+p[1]+") "); 
				
			}System.out.println();
			
		}
		
	}



public static int [] createSeq(int r){

	int [] arr = new int [r];
	int sum = 0; // the sum of all the elements should be even or the cardinality of the odd values should be even
	while (true){
		
		//create the sequence
		for (int i = 0; i<r; i++){
			
			arr[i] = new Random().nextInt(r);
			sum += arr[i];
			
		}
	
		//bubble-sort (the order should be non-increasing)
		arr = order(arr);
		
		if (sum%2 == 0){ 
			break;
		}else{
			sum = 0;
		}
		
	}
	return arr;
}

// check if the sequence is graphic
public static boolean isGraphical(int [] arr){
	int len = arr.length, sum = 0;
	
	for (Integer i: arr){
		if (i > (len-1) || i<0){
			return false;
		}
		sum += i;
	}
	
	if (sum == 0){
		return true;
	}
	arr = update(arr);
	return isGraphical(arr);
	
	
}

// check if there is non-increasing order and if not, call the order routine
public static void checkOrder(int [] arr){
	int len = arr.length;
	for (int i = 1; i<len; i++){
		if (arr[i-1]<arr[i]){
			arr = order(arr);
			break;
		}
	}	
}

//bubble-sort method (for non-increasing order)
public static int[] order(int [] arr){
	int temp, len = arr.length;
    for(int i=0; i < len; i++){  
		for(int j=1; j < (len-i); j++){  
            if(arr[j-1] < arr[j]){  
                temp = arr[j-1];  
                arr[j-1] = arr[j];  
				arr[j] = temp;  
			}
        }  
    }  
	return arr;
}

// special order function (based on the one above)
public static int[] order2(int [] arr){
	int temp, p = -1, i, len = arr.length;

    for(i=0; i < len; i++){
		if (arr[i]!=0){		
			for(int j= (i+1); j < len; j++){
				if(arr[i] < arr[j] && arr[j] != 0){  
					temp = arr[i];  
					arr[i] = arr[j];  
					arr[j] = temp;
				}
			}  
		}
    }  
	return arr;
}

// remove the first element, let d1, and -1 the next |d1| elements.
public static int[] update(int [] arr){
	int len = arr.length;
	int res[] = new int[len-1]; 
	
	for (int i = 1; i<len; i++){
		res[i-1] = arr[i];
	}
	
	if (arr[0]<len){
		len = arr[0];
	}
	for (int i = 0; i<len; i++){
		res[i]--;
		
	}
	checkOrder(res);
	
	return res;
}

// create a graph 
public static ArrayList <int[]> design(int []arr){
	int i ,j , len = arr.length, temp [], n=0, d_sum = 0;	
	
	ArrayList <int[]> edges = new ArrayList <int[]>();
	
	for (i=0; i<len; i++){
		if (arr[i]>0){
			n++;
		}
	}
	
	while (n>1){
		for ( i=0; i<len; i++){
			if (arr[i]>0){
				for (j=(i+1); j<len; j++){
					if (arr[j]>0){
						arr[j]--;
						arr[i]--;
						edges.add(new int[] {i,j});
						n--;
					}
					if (arr[j]==0){
						n--;
					}
					if (arr[i]==0){
						n--;
						break;
					}					
				}
				
			}
			/*
			for (Integer p: arr){
				System.out.print(" "+p);
			}System.out.println();
			*/
			order2(arr);
		}		
	}
	
	
	System.out.println("How many edges: "+edges.size());
	return edges;
}

}