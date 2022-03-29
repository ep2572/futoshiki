// Evan Petersen -- Project 2: Futoshiki Puzzle -- CS-UY 4613 Artificial Intelligence -- Professor E. K. Wong
package futoshiki;

import java.util.*;
import java.io.*;

public class Futoshiki {
    static int SIZE = 6;    // Currently set for a 6x6 grid
    static char[][] HRULES = new char[SIZE][SIZE-1];
    static char[][] VRULES = new char[SIZE-1][SIZE];
    
// PULLS THE STATE FROM THE INPUT FILE INTO initState
    static ArrayList<Integer>[][] extractState(Scanner input){
	int val=0;
	ArrayList<Integer>[][] state = new ArrayList[SIZE][SIZE];
	for(int i=0; i<SIZE; i++)
	    for(int j=0; j<SIZE; j++){
		val = (int)(input.next().charAt(0)-48);
		state[i][j] = new ArrayList<>();
		if (val > 0)
		    state[i][j].add(val);
		else
		    for(int k=1; k<=SIZE; k++)
			state[i][j].add(k);		
	    }
	return state;
    }
    
// ESTABLISH HRULES, MAKE FIRST INFERENCES
    static void hInitialInference(ArrayList<Integer>[][] state, Scanner input){
	char hrule;
	int val, end;
	ArrayList<Integer> left, right;
	for(int i=0; i<SIZE; i++)
	    for(int j=0; j<SIZE-1; j++){
		hrule = (char)input.next().charAt(0);
		HRULES[i][j] = hrule;
		if(hrule=='>'){				// If a > rule is given
		    left = state[i][j];
		    right = state[i][j+1];
		    if (left.size() == 1){		// If left is given remove all elements of right which are >= left
			val = left.get(0);
			end = right.size()-1;
			while(right.get(end) >= val)
			    right.remove(end--);
		    }
		    else if (right.size() == 1){	// If right is given remove all elements of left which are <= right
			val = right.get(0);
			while(left.get(0) <= val)
			    left.remove(0);
		    }
		    else{				// Otherwise trim the bottom of left and the top of right
			left.remove(0);
			right.remove(right.size()-1);
		    }
		}
		else if(hrule=='<'){			// If a < rule is given
		    left = state[i][j];
		    right = state[i][j+1];
		    if (left.size() == 1){		// If left is given remove all elements of right which are <= left
			val = left.get(0);
			while(right.get(0) <= val)
			    right.remove(0);
		    }
		    else if (right.size() == 1){	// If right is given remove all elements of left which are >= right
			val = right.get(0);
			end = left.size()-1;
			while(left.get(end) >= val)
			    left.remove(end--);
		    }
		    else{				// Otherwise trim the bottom of right and the top of left
			left.remove(left.size()-1);
			right.remove(0);
		    }
		}
	    }
    }
    
// ESTABLISH VRULES, MAKE FIRST INFERENCES
    static void vInitialInference(ArrayList<Integer>[][] state, Scanner input){
	char vrule;
	int val, end;
	ArrayList<Integer> up, down;
	for(int i=0; i<SIZE-1; i++)
	    for(int j=0; j<SIZE; j++){
		vrule = (char)input.next().charAt(0);
		VRULES[i][j] = vrule;
		if(vrule=='v'){				// If a v rule is given
		    up = state[i][j];
		    down = state[i+1][j];
		    if (up.size() == 1){		// If up is given remove all elements of down which are >= up
			val = up.get(0);
			end = down.size()-1;
			while(down.get(end) >= val)
			    down.remove(end--);
		    }
		    else if (down.size() == 1){		// If down is given remove all elements of up which are <= down
			val = down.get(0);
			while(up.get(0) <= val)
			    up.remove(0);
		    }
		    else{				// Otherwise trim the bottom of up and the top of down
			up.remove(0);
			down.remove(down.size()-1);
		    }
		}
		else if(vrule=='^'){			// If a ^ rule is given
		    up = state[i][j];
		    down = state[i+1][j];
		    if (up.size() == 1){		// If up is given remove all elements of down which are <= up
			val = up.get(0);
			while(down.get(0) <= val)
			    down.remove(0);
		    }
		    else if (down.size() == 1){	// If down is found remove all elements of up which are >= down
			val = down.get(0);
			end = up.size()-1;
			while(up.get(end) >= val)
			    up.remove(end--);
		    }
		    else{				// Otherwise trim the bottom of down and the top of up
			up.remove(up.size()-1);
			down.remove(0);
		    }
		}
	    }
    }
    
// FINDS THE INDEX OF A VALUE IN THE GIVEN LIST, IF ONE EXISTS
// Used to improve the time complexity of inferenceUpdate
    static int binSearch(ArrayList<Integer> list, int val){
	int index, check, start=0, end=list.size()-1;
	if(list.isEmpty() || list.get(0)>val || list.get(end)<val)
	    return -1;
	index = end/2;
	while((check=list.get(index)) != val){
	    if(end-start == 1)
		return -1;
	    if(check > val){
		end = index;
		index -= (end-start-1)/2+1;
	    }
	    else{
		start = index;
		index += (end-start-1)/2+1;
	    }
	}
	return index;
    }
    
// CHECKS COMPLIANCE WITH HORIZONTAL AND VERTICAL RULES
// Different from inferenceUpdate, comply looks for a range instead of a specific value
// Only useful for elements adjacent to rules, operates similarly to hInitialInference and vInitialInference
    static boolean comply(ArrayList<Integer>[][] state, int i, int j, int val){	
	int size;
	boolean set;
	ArrayList<Integer> list;
	if(j>0){    // Check rule left of i,j
	    list = state[i][j-1];
	    set = list.size()==1;
	    if(set && list.get(0)==val)
		return false;
	    if(HRULES[i][j-1]=='<'){
		size = list.size();
		while(!list.isEmpty() && list.get(size-1) >= val)
		    list.remove(--size);
	    }
	    if(HRULES[i][j-1]=='>')
		while(!list.isEmpty() && list.get(0) <= val)
		    list.remove(0);
	    if (list.isEmpty())
		return false;
	    else if (!set && list.size() == 1)
		if(!inferenceUpdate(state, i, j-1, state[i][j-1].get(0)))
		    return false;
	}
	if(j<5){    // Check rule right of i,j
	    list = state[i][j+1];
	    set = list.size()==1;
	    if(set && list.get(0)==val)
		return false;
	    if(HRULES[i][j]=='>'){
		size = list.size();
		while(!list.isEmpty() && list.get(size-1) >= val)
		    list.remove(--size);
	    }
	    if(HRULES[i][j]=='<')
		while(!list.isEmpty() && list.get(0) <= val)
		    list.remove(0);
	    if (list.isEmpty())
		return false;
	    else if (!set && list.size() == 1)
		if(!inferenceUpdate(state, i, j+1, state[i][j+1].get(0)))
		    return false;
	}
	if(i>0){    // Check rule above i,j
	    list = state[i-1][j];
	    set = list.size()==1;
	    if(set && list.get(0)==val)
		return false;
	    if(VRULES[i-1][j]=='^'){
		size = list.size();
		while(!list.isEmpty() && list.get(size-1) >= val)
		    list.remove(--size);
	    }
	    if(VRULES[i-1][j]=='v')
		while(!list.isEmpty() && list.get(0) <= val)
		    list.remove(0);
	    if (list.isEmpty())
		return false;
	    else if (!set && list.size() == 1)
		if(!inferenceUpdate(state, i-1, j, state[i-1][j].get(0)))
		    return false;
	}
	if(i<5){    // Check rule below i,j
	    list = state[i+1][j];
	    set = list.size()==1;
	    if(set && list.get(0)==val)
		return false;
	    if(VRULES[i][j]=='v'){
		size = list.size();
		while(!list.isEmpty() && list.get(size-1) >= val)
		    list.remove(--size);
	    }
	    if(VRULES[i][j]=='^')
		while(!list.isEmpty() && list.get(0) <= val)
		    list.remove(0);
	    if (list.isEmpty())
		return false;
	    else if (!set && list.size() == 1)
		if(!inferenceUpdate(state, i+1, j, state[i+1][j].get(0)))
		    return false;
	}
	return true;
    }
    
// VALIDATES THE ROW AND COLUMN OF THE GIVEN LIST TO MAKE SURE IT FITS
// Uses binarySearch to improve time complexity
    static boolean inferenceUpdate(ArrayList<Integer>[][] state, int i, int j, int val){
	int index;
	boolean set;
	if(!comply(state, i, j, val))	// Check compliance with HRULES and VRULES
	    return false;
	for(int a=0; a<SIZE; a++){
	    if(a != i){	// Check column for like values
		set = state[a][j].size()==1;
		index = binSearch(state[a][j], val);	// Get index of val if one exists, returns -1 otherwise
		if(index != -1)
		    state[a][j].remove(index);
		if (state[a][j].isEmpty())
		    return false;
		else if(!set && state[a][j].size()==1)	// If a list has only one element check to make sure it fits
		    if(!inferenceUpdate(state, a, j, state[a][j].get(0)))
			return false;
	    }
	    if(a != j){	// Check row for like values
		set = state[i][a].size()==1;
		index = binSearch(state[i][a], val);	// Get index of val if one exists, returns -1 otherwise
		if(index != -1)
		    state[i][a].remove(index);
		if (state[i][a].isEmpty())
		    return false;
		else if(!set && state[i][a].size()==1)	// If a list has only one element check to make sure it fits
		    if(!inferenceUpdate(state, i, a, state[i][a].get(0)))
			return false;
	    }
	}
	return true;
    }   

// CHECKS IF WORK IS DONE
    static boolean checkState(ArrayList<Integer>[][] state){
	for(int i=0; i<SIZE; i++)
	    for(int j=0; j<SIZE; j++)
		if (state[i][j].size() != 1)
		    return false;
	return true;
    }
    
// PRIMARY SEARCH FUNCTION
    static GameBoard backtrackingSearch(GameBoard curr){
	if(!curr.valid || curr.complete)
	    return curr;
	GameBoard result;
	int size;
	int row=0, col=0, target=SIZE;
	
	// Find the first smallest remaining list
	for(int i=0; i<SIZE; i++){
	    for(int j=0; j<SIZE; j++){
		size = curr.state[i][j].size();
		if(size > 1 && size < target){
		    row=i;
		    col=j;
		    target=size;
		}
	    }
	}
	for(int val : curr.state[row][col]){
	    result = backtrackingSearch(new GameBoard(curr, curr.depth+1, row, col, val));
	    if(result.complete)
		return result;
	}
	if(curr.depth==0)
	    System.out.println("Failure");
	return curr;
    }
    
// DISPLAYS THE STATE AS A GRID OF DOMAINS IN THE EVENT OF A FAILURE
    static void printState(ArrayList<Integer>[][] state, PrintStream out){
	for(int i=0; i<SIZE; i++){
	    for(int j=0; j<SIZE; j++)
		out.print(state[i][j]+"\t");
	    out.print("\n");
	}
    }
    
    public static void main(String[] args) {	// Pulling data from arg[0]
	
	try{
// EXTRACT DATA FROM INPUT FILE
	    Scanner sin = new Scanner(new File(args[0]));
	    ArrayList<Integer>[][] initState = extractState(sin);
	    hInitialInference(initState, sin);	// Initial setup for HRULES
	    vInitialInference(initState, sin);	// Initial setup for VRULES
	    
// CONSTRUCT INITIAL GAME BOARD AND ENTER BACKTRACKING SEARCH
	    GameBoard ans = backtrackingSearch(new GameBoard(initState));
	    
// SEND FINAL OUTPUT TO FILE
	    PrintStream out = new PrintStream(new File("Output"+args[0].replaceAll("[^0-9]", "")+".txt"));
	    //printState(initState, out);
	    if(ans.depth == 0){
		out.println("Failure");
		printState(ans.state, out);
	    }
	    else{
		System.out.println("Success! -- Check the folder for the output file");
		for(int i=0; i<SIZE; i++){
		    for(int j=0; j<SIZE; j++)
			out.print(ans.state[i][j].get(0)+" ");
		    out.print("\n");
		}
	    }
	}
	catch(FileNotFoundException e){
// ERROR CHECKING FOR THE FILE
	    System.out.println("Bad Input: "+args[0]+" either could not be found or it is not a proper filename.");
	    System.out.println("Make sure that the input file is in the same directory as Futoshiki.java");
	}		 
    }
    
    static class GameBoard {
	GameBoard parent;
	ArrayList<Integer>[][] state;
	int depth;
	boolean valid, complete;

	GameBoard(ArrayList<Integer>[][] initialState){	// Initial state
	    parent = null;
	    state = initialState;
	    for(int i=0; i<SIZE; i++)
		for(int j=0; j<SIZE; j++)
		    if(state[i][j].size() == 1)
			valid = inferenceUpdate(state, i, j, state[i][j].get(0));   // Forward Checking
	    depth = 0;
	    complete = false;
	}
	GameBoard(GameBoard newparent, int newdepth, int row, int col, int val){    // Child states
	    parent = newparent;
	    state = new ArrayList[SIZE][SIZE];
	    for(int i=0; i<SIZE; i++){
		for(int j=0; j<SIZE; j++){
		    ArrayList<Integer> temp = new ArrayList<>();
		    for (var k : parent.state[i][j]) {
			temp.add(k);
		    }
		    state[i][j] = temp;
		}
	    }
	    ArrayList<Integer> temp = new ArrayList<>();    // Updating the child state
	    temp.add(val);
	    state[row][col] = temp;
	    valid = inferenceUpdate(state, row, col, val);
	    if(valid)
		complete = checkState(state);
	    else
		complete = false;
	    depth = newdepth;
	}
    }
}