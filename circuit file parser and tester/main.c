#include "circuit.h"

int main(int argc, char **argv){
    //"circuit_ordered.txt"
    
	int numElements, TopInputsSize, SignalsTableSize;
	
	
	Element **ElementsTable;
	double *SignalsTable; // a b c d e f
	int *TopInputs;	
		
	printf("Starting\n");
	ElementsTable = readFile(argv[1],&TopInputs, &SignalsTable, &numElements, &TopInputsSize, &SignalsTableSize);
	printf("Top Inputs:\n");
	for (int j=0; j<3; j++)
	    printf("input %d\n", TopInputs[j]);	
	if (argc>2){
	    if (strcmp(argv[2], "unordered")==0)
        	ElementsTable = circuitSort(ElementsTable, TopInputs, numElements, TopInputsSize, SignalsTableSize);
    }
	/*

    
	printf("Signals\n");
    for (int j=0;j<6;j++)
        printf("2-signal %d\n", SignalsTable[j]);
	*/
    testbench(&SignalsTable, ElementsTable, TopInputs, TopInputsSize, SignalsTableSize, numElements);
    
    
    // Calculate avg. ESW
    double *InputValues = (double*)malloc(TopInputsSize*sizeof(int));
    for (int i=0; i<TopInputsSize;i++)
        InputValues[i] = 0.5;
    
    GiveInputs(TopInputs, &SignalsTable, TopInputsSize, InputValues);    
    puts("Average ESW (i.e. All inputs set to 0.5):");
    for (int i=0; i<numElements; i++){
        process(ElementsTable[i], &SignalsTable);
        double output = SignalsTable[ElementsTable[i]->outputIndex];
        //printf("Input %f Output %f\n", SignalsTable[ElementsTable[i]->inputs[0]], output);
        printf("- Gate no. %d:%s -> ESW: %f\n", i, ElementsTable[i]->type, ESW(output) );
    }
    
    // Calculate ESW for example sigProb = 0.484
    InputValues = (double*)malloc(TopInputsSize*sizeof(int));
    for (int i=0; i<TopInputsSize;i++)
        InputValues[i] = 0.4840;   
    GiveInputs(TopInputs, &SignalsTable, TopInputsSize, InputValues);   
     
    puts("Switching activity for signal probability 0.484:");
    for (int i=0; i<numElements; i++){
        process(ElementsTable[i], &SignalsTable);
        double output = SignalsTable[ElementsTable[i]->outputIndex];
        //printf("Input %f Output %f\n", SignalsTable[ElementsTable[i]->inputs[0]], output);
        printf("- Gate no. %d:%s -> ESW: %f\n", i, ElementsTable[i]->type, ESW(output) );
    }
    
	printf("Number of gates: %d\n", numElements);
    for (int i = 0; i < numElements; i++) {
            printf("Element %d:\n", i);
            printf("Type: %s\n", ElementsTable[i]->type);
            
            // Print inputs if available
            if (strcmp(ElementsTable[i]->type,"NOT")==0) {
                printf("Inputs indexes: %d", ElementsTable[i]->inputs[0]);
                
                
            }else{
                printf("Inputs indexes: ");
                for (int j=0;j<ElementsTable[i]->numberOfInputs;j++)
                   printf(" %d ", ElementsTable[i]->inputs[j]);
                
                
            }

            printf("\nOutput Index: %d\n", ElementsTable[i]->outputIndex);

            
            // Add a newline to separate elements
            printf("\n");
            
            
            
     }
    free(ElementsTable);
    free(SignalsTable);
    free(TopInputs);
    
	return 0;
}