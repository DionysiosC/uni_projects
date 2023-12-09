# Graphic sequence detection and graph creation
## Introduction
Here is a simple program that detects if a random degree sequence is graphic (or graphical) i.e. for which the degree sequence problem has a solution or a graph.
If the sequence does represent a graph, it also gives a possible solution as well or a possible connection of its vertices according to the degree of each vertex.
Related wikipedia article section about this problem [here](https://en.wikipedia.org/wiki/Degree_(graph_theory)#Degree_sequence "Degree Sequence").
The resulting graph creation algorithm was inspired by [this](https://math.stackexchange.com/questions/527530/constructing-a-graph-from-a-degree-sequence "Constructing a graph from a degree sequence") article.

## Details
Java 15.0.2 was used for the development of this code.
To compile, run
```
javac main.java
```
To use the resulting class executable, do
```
java main
```
and follow the instructions on the cmd.

The user is prompted to insert the size of the degree sequence (number of vertices) and then a random sequence is created.
If the resulting sequence is graphic, a solution is also printed on the screen. 

This program was created in 2023, in the context of the graph theory course of my uni.