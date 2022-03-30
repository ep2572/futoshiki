# Futoshiki
An AI puzzle solver for Futoshiki number puzzles.  
Written and compiled with Java SE 11 & JDK 11 in Apache NetBeans IDE 12.2

### Java Environment
openjdk version "17.0.2" 2022-01-18 LTS  
OpenJDK Runtime Environment Microsoft-30338 (build 17.0.2+8-LTS)  
OpenJDK 64-Bit Server VM Microsoft-30338 (build 17.0.2+8-LTS, mixed mode, sharing)

Environment download can be found here: https://docs.microsoft.com/en-us/java/openjdk/download

Created as an AI project for a university course. All of the code is my own.

## What is a Futoshiki Puzzle?
A futoshiki puzzle is a number puzzle similar to sudoku where a square grid --typically 5x5, 6x6 or 7x7-- is seeded with  numbers and rules. The objective is to fill each column and row with the numbers ***1 to n*** --where n is the length of the grid. No repeated numbers are allowed in a given row or column.

Between the numbers are arrows which represent ***greater-than/less-than relationships***, wherein the relationship must hold for each pair of numbers that directly bookend the arrows. Simply put, ***the arrow must always point to the smaller of the two adjacent numbers***.

Not all arrangements of numbers and rules will have a solution. 

## The program
The purpose of the futoshiki solver is to find the fastest potential answer or identify that the problem is unsolvable.

### How it works
 1. Given the name of an input file in the local directory the program scans the files and extracts the initial state of the puzzle.
     - A state is a 2D Array of Integer Lists. 
     - Any index where the initial state is known contains a list of one element, all other indices contain a list of integers from 1 to n, representing the possible values
 2. The vertical and horizontal rules are analyzed and adjustments to the lists of possible values are inferred.
     - Even if no known value exists the upper or lower bound of an index can be eliminated due to its proximity to a rule.
 3. The initial state is then placed into a GameBoard class. The the gameboard class initialized with the initial state will then check each index and for any index that contains only one integer it will perform an inference update. The inference update eliminates that value from all lists in the same row or column. ***If this results in any list length of 0 then the solution is known to be impossible.***
     - The initial state is the root of a tree representing the searched paths.
 4. The program uses a Backtracking Algorithm to search for a solution. In the Backtracking algorithm each iteration performs the following:
     1. If the task is complete or invalid then return the GameBoard.
     2. Otherwise, find the first index with the smallest size.
     3. Continue to the next level of the search using a GameBoard with the value selected for the chosen index.
     4. Update the inferences.
     5. If a failure is returned continue to iterate through the list of possible values until a solution is retuned.
     6. If no solution is returned then return a failure.
 5. If a solution is found with a completed state then build an output file in the local directory containing the solution.

### How to Run
 1. Clone the futoshiki repository
 2. Using a terminal/command_line, navigate to the futoshiki directory
 3. Enter: `java Futoshiki.java "file"` where "file" is the desired input
 4. The output file is stored as in the main directory `Output#.txt` where `#` is the number of the input.

### Formatting input files
File names follow the convention `Input#.txt` where `#` is a positive integer  
Input files are plain text files with 3 clusters of data separated by empty lines. `0` is a placeholder when there is no rule given.
 1. The first cluster is an ***n rows*** x ***n columns*** grid, typically ***n*** is between 5 and 7. Values in the grid are integers from 1 to n separated by a space. Any non-zero value in the grid represents the value that must occupy the space. There must be no repeating numbers an any row or column.
 2. The second cluster is an ***n rows*** x ***n-1 columns*** grid, representing the horizontal rules. Any non-zero element must either be `<` or `>`.
 3. The third cluster is an ***n-1 rows*** x ***n columns*** grid, representing the vertical rules. Any non-zero element must either be `v` or `^`.
