// Implementations of the functions

#include "circuit.h"

double sp2AND(double input1sp, double input2sp){
	return (double)input1sp*input2sp;
}

double sp2NOT(double input1sp){
	return (1.0 - input1sp);
}

double sp2OR(double input1sp, double input2sp){
	return (double)(1.0-(1.0-input1sp)*(1.0-input2sp));
}

double sp2XOR(double input1sp, double input2sp){
    int a, b;
    a = (1.0 - input1sp) * input2sp;
    b = input1sp * (1.0 - input2sp);
    return 1.0 - (1.0-(1.0-a)*b)*(1.0-a*(1.0-b));
}


double sp2NAND(double input1sp, double input2sp){
	return (double)(1-input1sp*input2sp);
}

double sp2NOR(double input1sp, double input2sp){
	return (double)((1-input1sp)*(1-input2sp));
}
// Gate functions descriptions end

// The process function
void process(Element *E, double **SignalsTable){
	if (strcmp(E->type, "AND")==0){
		(*SignalsTable)[E->outputIndex] = sp2AND((*SignalsTable)[E->inputs[0]], (*SignalsTable)[E->inputs[1]]);
		
	}else if (strcmp(E->type, "NOT")==0){
		(*SignalsTable)[E->outputIndex] = sp2NOT((*SignalsTable)[E->inputs[0]]);
		
	}else if (strcmp(E->type, "OR")==0){
		(*SignalsTable)[E->outputIndex] = sp2OR((*SignalsTable)[E->inputs[0]], (*SignalsTable)[E->inputs[1]]);
		
	}else if (strcmp(E->type, "XOR")==0){
		(*SignalsTable)[E->outputIndex] = sp2XOR((*SignalsTable)[E->inputs[0]], (*SignalsTable)[E->inputs[1]]);
		
	}else if (strcmp(E->type, "NOR")==0){
		(*SignalsTable)[E->outputIndex] = sp2NOR((*SignalsTable)[E->inputs[0]], (*SignalsTable)[E->inputs[1]]);
	
	}else if (strcmp(E->type, "NAND")==0){
		(*SignalsTable)[E->outputIndex] = sp2NAND((*SignalsTable)[E->inputs[0]], (*SignalsTable)[E->inputs[1]]);
	}

}

// returns the type of the gate (string of chars) or NULL if its not a gate (i.e. a signal)
char * isGate(char *s){
	if ( strcmp(s,"NOT")==0 || strcmp(s,"AND")==0 || strcmp(s,"OR")==0 || strcmp(s,"NAND")==0 || strcmp(s,"XOR")==0 || strcmp(s,"NOR")==0 )
		return s;
	return NULL;
}

// The giveinputs function that takes as input an array of the topinput values and the size of this array, apart from the standard inputs
void GiveInputs(int *TopInputs, double **SignalsTable, int TopInputsSize, double *InputValues){	
	for (int i=0; i<TopInputsSize;i++)
	    (*SignalsTable)[TopInputs[i]] = InputValues[i];	
	
}

// Find ESW
double ESW(double pout){
    return 2*pout*(1-pout);
}

// The sorting function needed for the 3.3 exercise
     /* sorting steps
        1. iterates the signals table
        2. marks the top inputs
        3. marks the elements that have these marked input signals as inputs
        4. marks their outputs too and puts the marked element to the final table
        5. marks the elements that have the outputs of step 4 as inputs
        6. marks their outputs too and puts the element in the final table
        7. 5-6 until no unmarked element left ie all the elements have been checked   
     */ 
Element ** circuitSort(Element **ElementsTable, int  *TopInputs, int numElements, int TopInputsSize, int SignalsTableSize){
    puts("Sorting the circuit elements...");
    
    Element **sortedElementsTable = (Element **)malloc(numElements*sizeof(Element*));
    if (numElements == 0 || SignalsTableSize ==0)
        return NULL;
        
    int *SignalsFlags = (int*)calloc (SignalsTableSize, sizeof(int));
    int *elementFlags = (int*)calloc (numElements, sizeof(int));

    int i;
    for (i=0; i<numElements; i++)
        sortedElementsTable[i] = (Element*)malloc(sizeof(Element));  
    
    for (i=0; i<SignalsTableSize;i++){
        for (int j=0; j<TopInputsSize;j++){
            //printf("TopInputs[j]: %d i: %d\n", TopInputs[j], i);
            if (i == TopInputs[j]){
                SignalsFlags[i]=1;
                
            }   
        }   
    }
    /*
    for (i=0;i<6;i++){
        printf("Signals flag: %d\n",SignalsFlags[i]);
    }
    */
    int currentElement = 0;
    
    for (i=0; i<numElements; i++){
        int size = ElementsTable[i]->numberOfInputs; // flag = 0 if some of the inputs are not top inputs
        int flag = 1;  
        for (int j=0;j<size;j++){
            int currentIndex = ElementsTable[i]->inputs[j];
            flag &= SignalsFlags[currentIndex];                       
        }
        //printf("flag is %d\n", flag);
        
        // in case all the inputs are marked, we mark the element and place it in the final table
        if (flag){
            elementFlags[i]=1;
            
            // deep copy to the final table
            sortedElementsTable[currentElement]->outputIndex = ElementsTable[i]->outputIndex;
            sortedElementsTable[currentElement]->numberOfInputs = ElementsTable[i]->numberOfInputs;
            sortedElementsTable[currentElement]->type = strdup(ElementsTable[i]->type);
            
            sortedElementsTable[currentElement]->inputs = (int *)malloc(ElementsTable[i]->numberOfInputs * sizeof(int));
            for (int j=0;j<size;j++)
                sortedElementsTable[currentElement]->inputs[j] = ElementsTable[i]->inputs[j];
            
            
            // mark output index
            SignalsFlags[ElementsTable[i]->outputIndex] = 1;
            currentElement++;
            
        }
       
    }   
    printf("Current element: %d\n", currentElement);
    
    
    while (currentElement<numElements){
            
        for (i=0; i<numElements; i++){
            if (!elementFlags[i]){
            
                    int allInputsMarked = 1;
                    int size = ElementsTable[i]->numberOfInputs;
                    
                    for (int j=0; j<size;j++){
                        allInputsMarked &= SignalsFlags[ElementsTable[i]->inputs[j]];
                    }
                    
                    if (allInputsMarked){
                        elementFlags[i]=1;
                        
                        // deep copy to the final table
                        sortedElementsTable[currentElement]->outputIndex = ElementsTable[i]->outputIndex;
                        sortedElementsTable[currentElement]->numberOfInputs = ElementsTable[i]->numberOfInputs;
                        sortedElementsTable[currentElement]->type = strdup(ElementsTable[i]->type);
                                                                        
                        sortedElementsTable[currentElement]->inputs = (int *)malloc(ElementsTable[i]->numberOfInputs * sizeof(int));
                        for (int j=0;j<size;j++)
                            sortedElementsTable[currentElement]->inputs[j] = ElementsTable[i]->inputs[j];
                            
                                              
                        // mark output index
                        SignalsFlags[ElementsTable[i]->outputIndex] = 1;                                        
                        currentElement++;
                        
                }
                    
            }
        }
 
    }
        
    printf("Current element: %d\n", currentElement);    
     
    puts("Done sorting the elements");
    free(SignalsFlags);
    free(elementFlags);
    return sortedElementsTable;
}


// The testbench function that after calculating all the possible inputs of the circuit, calls the process function once for each input
void testbench(double *SignalsTable[], Element *ElementsTable [], int TopInputs[], int TopInputsSize, int SignalsTableSize, int numElements){
	// The size of the TopInputs table is the number of input bits! 
	int end = (1<<TopInputsSize); // 2^(TopInputSize)
	
	int current;
	puts("Testbench start!");
	for (int i=0; i<end; i++){
        // Calculate the input for the current line of the truth table and save it in the Signals Table
        //printf(" Current number: %d/%d\n", i, end-1);
        
        double *InputValues = (double*)malloc(TopInputsSize*sizeof(double)); 
        current = i;
           
        for (int j = 0; j < TopInputsSize; j++) {
        
            int bit = current & 1;

            InputValues[TopInputsSize - 1 - j] = (int)bit;  // Save LSB at the last position

            current = current >> 1;
            
        }
        /*
        for (int j=0;j<TopInputsSize;j++)
            printf("%d", InputValues[j] );
        puts("");
        */
        //  call the process for the current input
        GiveInputs(TopInputs, SignalsTable, TopInputsSize, InputValues);
        for (int j=0; j<numElements; j++)
            process(ElementsTable[j], SignalsTable);
        
        
        for (int j=0; j<SignalsTableSize; j++){
            printf(" %f ", (*SignalsTable)[j]);
            
        }puts("");
	    free(InputValues);
	    
	}puts("");
    puts("Testbench end!");
}

// The readfile function that extracts the information from the circuit file
Element ** readFile(char *fileName, int **TopInputs, double **SignalsTable, int *numberOfElements, int *TopInputsSize, int *SignalsTableSize){
    
	Element **ElementsTable=NULL;
	char **SignalsNames=NULL, **TopInputNames = NULL;
	char buf[82]; int i = 0; int numberOfGates = 0; int numberOfSignals = 0; int numberOfInputs = 0;
	char **readElements = NULL; int readElementsSize = 6; char c;

	FILE *fp = fopen(fileName, "r");
	
	if (fp == NULL){
		perror("ERROR: ");
		exit(errno);
	}else
		printf("File opened successfully\n");
	
	// read the file and save its content
	readElements = (char **)malloc(readElementsSize*sizeof(char*));	
	while (fscanf(fp, "%s", buf) != EOF && (c = getc(fp)) != EOF) {
		if (c=='\n')
			numberOfGates++;
		
		readElements[i++] = strdup(buf);		
		//printf("%s \n",buf);
		if (i>readElementsSize){
		    readElementsSize += sizeof(char*);
		    readElements = (char **)realloc(readElements, readElementsSize*sizeof(char*));
		}
		
	}   	
   	readElementsSize = i;
	printf("File parsed successfully\n");
	fclose(fp);
	
	
	
	for (i=0; i<readElementsSize;i++){
		printf(" (%d,%s), ",i,readElements[i]);
	}
	printf("\n");	
	
	// find top inputs, calculate the size of the signals table and initialize it
	i=0;
	SignalsNames = (char**) malloc (readElementsSize*sizeof(char*));
	if (strcmp(readElements[i], "top_inputs")==0){
		numberOfGates--;
				
		printf("Top Inputs are defined in the file\n");
		
		i=1;
		while (isGate(readElements[++i])==NULL)
			;
			
		numberOfInputs = i-1;
		*TopInputs = (int*)malloc(numberOfInputs*sizeof(int));
		TopInputNames = (char **) malloc(numberOfInputs*sizeof(char*));
		
		for (int j=0; j<numberOfInputs;j++){
		    TopInputNames[j] = strdup(readElements[j+1]);
		    puts(TopInputNames[j]);
		}
		
        //printf("Start i: %d\n",i);
        
        for (;i<readElementsSize; i++){
            if (!isGate(readElements[i])){
                int isInTheList=0;
                
                for (int j=0; j<numberOfSignals;j++){
                    if (strcmp(readElements[i],SignalsNames[j])==0){
                        isInTheList = 1;
                        break;
                    }
                }
                //printf("Signals: %d Getting signal %c in the table?:%d\n",numberOfSignals, readElements[i][0], isInTheList);
                if (!isInTheList){
                    SignalsNames[numberOfSignals++] = strdup(readElements[i]);  
                           
                }
            }
        }
	    for (i=0; i<numberOfInputs; i++){
	        for (int j=0;j<numberOfSignals; j++){
	            
	            if (!strcmp(TopInputNames[i], SignalsNames[j])){
	                (*TopInputs)[i] = j;
	                //puts("Hii");
	            }
	        }
	    }             
		
	}else{
	    // if there are no top inputs
	    printf("Top Inputs are not defined in the file\n");
	    
	    char **outputs = (char**) malloc (readElementsSize*sizeof(char*));
	    int outputsNumber = 0; // how many outputs
	    
	    TopInputNames = (char**)malloc(readElementsSize*sizeof(char*));
	    
	    for (i=0;i<readElementsSize;i++){
	        if (isGate(readElements[i])){
	            outputs[outputsNumber++] = strdup(readElements[++i]);
	            //printf("current output: %c\n", outputs[c-1]);
	        }
	    }
	    
	    outputs = (char**) realloc (outputs,outputsNumber *sizeof(char*));
	    
        for (i=0;i<readElementsSize;i++){
            //printf("--Current index: %d\n",i);
            if (!isGate(readElements[i])){
                int isInTheSignals = 0;
                int isInTheOutputs = 0;
                int isInTheInputs = 0;
                
                for (int j=0; j<numberOfSignals;j++){
                    if (strcmp(SignalsNames[j], readElements[i])==0){
                        isInTheSignals = 1;
                        break;
                    }
                }
                //printf("Is '%s' in the signals: %d Signals: %d\n", readElements[i], isInTheSignals, numberOfSignals);
                if(!isInTheSignals)
                    SignalsNames[numberOfSignals++] = strdup(readElements[i]);
                //printf("current: %s\n",SignalsNames[numberOfSignals-1]);
            
               for (int j=0; j<outputsNumber;j++){
                    if (strcmp(outputs[j],readElements[i])==0){
                        isInTheOutputs  = 1;
                        break;
                    }
                }
                
                for (int j=0; j<numberOfInputs;j++){
                    if (strcmp(TopInputNames[j],readElements[i])==0){
                        isInTheInputs  = 1;
                        break;
                    }
                }
                
                if(!isInTheOutputs && !isInTheInputs)
                    TopInputNames[numberOfInputs++] = readElements[i];            

            }
        
        }    
        free(outputs);
	}
	
    SignalsNames = (char**) realloc (SignalsNames, numberOfSignals*(sizeof(char*)));
    *TopInputs = (int*) realloc (*TopInputs, numberOfInputs*sizeof(int));
	for (i=0; i<numberOfInputs; i++){
	    for (int j=0;j<numberOfSignals; j++){
	        if (!strcmp(TopInputNames[i], SignalsNames[j])){
	           (*TopInputs)[i] = j;
	            //puts("Hii");
	        }
	    }
	}  
	
	*SignalsTable = (double*) calloc (numberOfSignals,sizeof(double));
	
	
	puts("Top Input names:");
	for (int j=0; j<numberOfInputs;j++)
		printf("Input %d: %s at %d\n",j, SignalsNames[(*TopInputs)[j]], (*TopInputs)[j]);
	
	//puts("SignalsNames: ");
	// should check for double appearences of a signal name!
	puts("Signal Names: (The order they appear at the table)");
	for (int j=0; j<numberOfSignals; j++){
	    printf(" %s ", SignalsNames[j]);
	}puts("");
    
	// find gates and in/out signals and initialise the Element table	
	c=0; // c is char so up to 256 gates
	
	*numberOfElements = numberOfGates;
	ElementsTable = (Element**) malloc (numberOfGates*sizeof(Element*));
	//printf("Gates: %d\n", numberOfGates);
	
	
	for (i=0;i<readElementsSize;i++){
	
		char *s = isGate(readElements[i]);
		//puts(readElements[i]);
		
		if (s !=NULL){ // if the current element in the array is a gate
			ElementsTable[c] = (Element*)malloc(sizeof(Element));
			
			ElementsTable[c]->type = strdup(s);
			
			// calculate the output index of the current gate
			for (int j=0; j<numberOfSignals; j++){
			    if (strcmp(SignalsNames[j], readElements[i+1])==0){
    			    ElementsTable[c]->outputIndex = j;
    			    //printf("Gate: %s Output name: %s Index at signals table: %d\n",ElementsTable[c]->type, readElements[i+1], ElementsTable[c]->outputIndex );
    			    break;		
    			}
			}			
			
			// calculate the input indexes of the gate
			if (strcmp(ElementsTable[c]->type, "NOT") == 0){
			    //puts("NOT found");
			    ElementsTable[c]->inputs = (int*)malloc(sizeof(int));
			    ElementsTable[c]->numberOfInputs = 1;
			    
			    for (int j=0; j<numberOfSignals; j++){
			        if (strcmp(SignalsNames[j], readElements[i+2])==0){
        			    ElementsTable[c]->inputs[0] = j;
        			    break;		
        			}
			    
			   } 
						   
			}else{
			  	
			    int start, end, inputs;
			    i+=2;
			    
			    start = i; // 1st input	
			    for (;i<readElementsSize;i++){
			    	if (isGate(readElements[i]) != NULL)
			    		break;
			    }			   	
			    end = i-1;
			    inputs = end-start+1;
			    
			    ElementsTable[c]->inputs = (int*)malloc(inputs*sizeof(int));
			    ElementsTable[c]->numberOfInputs = inputs;
			    
			    //printf("START: %d END: %d INPUTS: %d\n", start, end, inputs);
			    
			    for (; end>=start; end--){
			    	for (int j=0; j<numberOfSignals;j++){
			    		if (strcmp (readElements[end], SignalsNames[j])==0){
			    			ElementsTable[c]->inputs[end-start] = j;
			    			break;		
			    		}
			    	}
			    	
			    	//printf("written:(%d,%d)\n",end-start,end);
			    }
			    /*
			    for (int j=0; j<inputs; j++)
			    	printf("%d %d\n",j, ElementsTable[c]->inputs[j]);
			    */
			    i--;
                
			}
			c++;		
		}
	}
	
	free(SignalsNames);
	free(readElements);
	
	*SignalsTableSize=numberOfSignals;
	*TopInputsSize=numberOfInputs;
	
	puts("Parser end\n");
	return ElementsTable;
}