
# Requirements and Documentation

1. [Introduction](#introduction)
2. [File Format](#file-format)
3. [Objective](#objective)
4. [Details](#details)
5. [Implementation details](#implementation-details)
6. [How to run the application](#how-to-run-the-application)

## Introduction
This is a simple program written in C language, in the context of the microcontrollers course of my uni, in 2023.

It loads into the simulation model the circuit from the files found on the slides. It is assumed that the logical elements in the file may not be sorted by
their correct order of processing.

## File Format
The file (.txt) that is entered as input describes a circuit composed of OR, AND, XOR, NAND, NOR and NOT gates. The current version supports only two inputs for each gate.
The contents of each circuit description file are written in the format shown below:
```
<gate_A> <output_A> <input1> <input2> <...>
<gate_B> <output_B> <input3> <input4> <...>
...
```
Here is an example:
```
AND e a b
NOT f c
AND d e f
```

The other format that is supported is this:
```
top_inputs <input1> <input2> <...>
<gate_A> <output_A> <input1> <input2> <...>
<gate_B> <output_B> <input3> <input4> <...>
...
```
where the top inputs or consequently the inputs of the circuit are explicitly defined.

Here is an example:
```
top_inputs a b c
AND f a b
AND e c a
OR  z f e
```
Warning: The size of the input/output variable names is irrelevant, but each one has to be unique i.e. no nodes are permitted(yet).

For example the circuit descripted below:
```
AND f a b
NOT e a
OR  z f e
```
will lead the testbench in producing wrong results, as the testbench code is not yet optimized to work correctly when two gates share some input wires.

## Objective
The application parses the circuit description file and runs a testbench and the scenario is to test the circuit for every possible input (0 or 1) of the circuit.

The application prints the results, like the resulting truth table of the circuit.

## Details
The basic idea is described below:
- The application starts by parsing the file by calling the readFile function and if it is done successfully, it prints a corresponding message at the terminal. If not, error information is printed on the terminal instead.

- Then, the testbench function is used and the extracted information and elements found in the description file is being processed. The algorithm finds out which "nodes" are inputs and which of them are outputs.

- Finally, results like average switching activity and the truth table are printed on the terminal. 

Be aware that the program is created in such fashion that the inputs of the gates can have a double value i.e. signal probability value.

## Implementation details
The code was compiled and tested using gcc v11.2.0, in Unix environment.

## How to run the application
To run the application after all the source code files have been compiled with the **make** file, the user has to use this format at the terminal:
```
main <your_file_name.txt>
```
The file has to be located in the same directory as the code base.

If the elements in the circuit description files are not sorted, use:
```
main <your_file_name.txt> unordered
```
This is an interim solution, the automatic order detection of the elements is in the "todo" list.