
# Requirements and Documentation

1. [Introduction](#introduction)
2. [Final State](#final-state)
3. [Problem State Representation](#problem-state-representation)
4. [Validation Criteria for Problem State](#validation-criteria-for-problem-state)
5. [Cube Movement](#cube-movement)
6. [Cost of Cube Movement](#cost-of-cube-movement)
7. [Objective](#objective)
8. [Visualization of Path from AK to TK](#visualization-of-path-from-ak-to-tk)
9. [Heuristic Function](#heuristic-function)
10. [Conclusion and implementation details](#conclusion-and-implementation-details)

## Introduction
This is my project for the AI course of my uni. It was created and tested in May 2023.

Given **N=3*K** numbered cubes (where **K** is provided by the user of the program), we want to place them in three rows (one row on top of the other) with **K** cubes in each row. The position of each cube is described by coordinates **(x, y)** where:

- For **y=1** (the first row resting on a table), the possible values of **x** are {1, 2, ..., L}, where **L=4*K** (Equation 1).

- For **y=2** (the second row based on the first row) and **y=3** (the third row based on the second row), the possible values of **x** are {1, 2, ..., K} (Equation 2).

On the table (i.e., when **y=1**), there are more than **K** seats available, specifically **L=4*K**. The surplus **3*K** positions (from **K+1** to **4*K**) can be used to temporarily place some cubes outside the basic formation.

## Final State

The final state of the problem is defined as follows:

- The first row contains the cubes numbered **1 to K** (in ascending order from left to right) supported on the table, i.e., they are placed at positions **(1,1) to (K,1)**.

- The second row shall be the cubes numbered **K+1 to 2K** (in ascending order from left to right) resting on the cubes of the first row, i.e., placed in positions **(1,2) to (K,2)**.

- The third row contains the cubes numbered **2K+1 to N** (in ascending order from left to right) resting on the cubes of the second row, i.e., placed in positions **(1,3) to (K,3)**.

## Problem State Representation

The problem state can be viewed as a list of **N** locations where each location contains the coordinates **(x, y)** of the corresponding cube. For example, if **K=2** (so **N=6**), the final state is:

```markdown
[(1,1), (2,1), (1,2), (2,2), (1,3), (2,3)]
```

## Validation Criteria for Problem State

For a problem state to be valid:

a) Constraints:

   - Constraint (1): The position **(x, y)** of each cube must satisfy the condition:
     - For **y=1** (the first row resting on a table), the possible values of **x** are {1, 2, ..., L}, where **L=4*K**.
     - For **y=2** and **y=3**, the possible values of **x** are {1, 2, ..., K}.

   - Constraint (2): Each of the cubes must be either placed on the table or resting on another cube.

b) Each cube's position must adhere to the specified constraints, ensuring that it is either placed on the table or positioned on another cube.

These criteria ensure that the problem state is valid and complies with the defined constraints.

## Cube Movement

A cube at position **(x, y)** is considered free if there is no cube at position **(x, y+1)**. To determine if a given cube is free in a given state, a function that checks this condition is created.

Any free cube at position **(x, y)** can be moved to an empty position **(x', y')**, provided that the new state resulting from the move is valid. The possible moves for a free cube include:

- Moving to a permissible empty position on the table, where **y'=1**.
- Moving on another free cube of the first row, where **y'=2**.
- Moving on another free cube of the second row, where **y'=3**.

These conditions ensure that the movement of a free cube is within the constraints of a valid state and can be used to update the cube's position accordingly.

## Cost of Cube Movement

The cost of moving a cube from position **(x, y)** to position **(x', y')** is determined by the following rules:

- If the movement causes the cube to be lifted (i.e., **y' > y**), then the cost of energy is **y' - y**.

- If the cube is caused to descend (i.e., **y' < y**), then the cost of energy is **0.5 * (y - y')**.

- If the movement keeps the cube in the same row (i.e., **y' = y**), then the cost of energy is **0.75**.

These rules define the energy cost associated with different types of cube movements, taking into account whether the cube is lifted, descends, or stays in the same row.

## Objective

The objective is to determine the minimum cost sequence of actions to transition from an initial valid formation (AC) of **N** cubes to the final formation (FC) described above.


## Visualization of Path from AK to TK

During the execution of the program, the following information will be available:

a) **Path from AK to TK:**
   - The path represents the sequence of actions taken to transform the initial valid formation (AK) to the final formation (TK).
   - Each step in the path corresponds to a cube movement or action taken during the optimization process.

b) **Cost of the Path:**
   - The cost of the path indicates the total energy cost associated with the sequence of actions taken.
   - It considers the rules for cube movement and the specified energy costs for lifting, descending, or staying in the same row.

c) **Number of Extensions Made:**
   - The number of extensions made reflects the iterations or extensions performed during the search for the optimal path.

This information provides insights into the process of finding the minimum cost sequence of actions from the initial valid formation (AK) to the final formation (TK).

## Heuristic Function

The heuristic function for A* is defined as follows in the code:

```java
public double heuristic(Cube c) {
    int y1 = c.getCoordinates()[1];
    int y2 = c.getFinalPos()[1];
    int x1 = c.getCoordinates()[0];
    int x2 = c.getFinalPos()[0];

    if (y2 == y1 && x1 == x2) {
        return 0;
    } else {
        return (0.75 + 0.5 * Math.abs(y2 - y1));
    }
}
```
and is acceptable as at each step it returns an estimate of the distance to the final state less than or equal to the actual and never greater. In practice, it is a variant of the Manhattan distance .
If the cube is in its final position, then the value of the heuristic for this position is 0. However, if the cube is not there, the value obtained by the operation: 

```java
0.75 + 0.5*Math.abs(y2-y1) 
```
or 
``
0.5+0.5*|yf-yi|
``
 where yf is the end line number where the cube should be located and yi is the number of the starting line where the cube should be located is returned. The addition by a factor of 0,75 is due to the horizontal movement of the cube, which must have a cost of 0,75. Multiplying by 0,5 the difference in the formula refers to the case where the cube must descend a line and the cost of this movement is certainly less than the cost of lifting it to another line.

## Conclusion and implementation details
The program was developed using java 15.0.2 on Windows 10, using Notepad++.
To run after all the source code files are compiled, execute
``
java Main
``
and follow the instructions.