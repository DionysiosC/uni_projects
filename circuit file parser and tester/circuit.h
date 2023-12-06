// Only the declarations of each function and structure and the inclusions of each library

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>

#ifndef CIRCUIT_H
#define CIRCUIT_H

struct Element {
    char *type;
    int *inputs;
    int outputIndex;
    int numberOfInputs;
};
typedef struct Element Element;

double sp2AND(double input1sp, double input2sp);
double sp2NOT(double input1sp);
double sp2OR(double input1sp, double input2sp);
double sp2XOR(double input1sp, double input2sp);
double sp2NAND(double input1sp, double input2sp);
double sp2NOR(double input1sp, double input2sp);

void process(Element *E, double **SignalsTable);

char *isGate(char *s);

void GiveInputs(int *TopInputs, double **SignalsTable, int TopInputsSize, double *InputValues);

double ESW(double pout);

Element **circuitSort(Element **ElementsTable, int *TopInputs, int numElements, int TopInputsSize, int SignalsTableSize);

void testbench(double *SignalsTable[], Element *ElementsTable[], int TopInputs[], int TopInputsSize, int SignalsTableSize, int numElements);

Element **readFile(char *fileName, int **TopInputs, double **SignalsTable, int *numberOfElements, int *TopInputsSize, int *SignalsTableSize);

#endif