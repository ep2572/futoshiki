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

### How to Run
 1. Clone futoshiki repository
 2. Using a terminal/command_line, navigate to the futoshiki directory
 3. Enter: `java Futoshiki.java "file"` where "file" is the desired input
 4. The output file is stored as in the main directory `Output#.txt` where `#` is the number of the input.

### Formatting input files
File names follow the convention `Input#.txt` where `#` is a positive integer  
Input files are plain text files with 3 clusters of data separated by empty lines. `0` is a placeholder when there is no rule given.
 1. The first cluster is an ***n rows*** x ***n columns*** grid, typically ***n*** is between 5 and 7. Values in the grid are integers from 1 to n separated by a space. Any non-zero value in the grid represents the value that must occupy the space. There must be no repeating numbers an any row or column.
 2. The second cluster is an ***n rows*** x ***n-1 columns*** grid, representing the horizontal rules. Any non-zero element must either be `<` or `>`.
 3. The third cluster is an ***n-1 rows*** x ***n columns*** grid, representing the vertical rules. Any non-zero element must either be `v` or `^`.

### Storing input files
Input an output files are stored in the main directory for ease of use.

## Improving the program
 1. Considering the small size of sample inputs, no considerations were made towards project organization. Storing the input/output files in a subdirectory would provide a more structured project layout.
 2. 



