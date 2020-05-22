package com.stephengware.java.games.chess.bot;

import java.util.ArrayList;
import java.util.Iterator;
import com.stephengware.java.games.chess.bot.Bot;
import com.stephengware.java.games.chess.state.*;


/**
 * A chess bot which uses minimax algorithm with alpha beta pruning and a position evaluation heuristic along with material score.
 * 
 * @author Anish Chand achand 2541626
 */
public class MyBot extends Bot {

	private static int depth = 0;
	private final static int LIMIT = 4;
	private static ArrayList<GameTree> children = new ArrayList<>();
	/**
	 * Constructs a new chess bot named "Achand" 
	 */
	public MyBot() {
		super("Achand");
	}

	/**
     * Returns the next possible move by analyzing different scenarios using minimax 
     *
     *@param  root 	 the state of the game
     *@return State  the state of the game
	 */
	@Override
	protected State chooseMove(State root) {
		double value;
		if(root.player.other().equals(Player.BLACK)) {
			value = findMax(root, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth);
		}
		else {
			value = findMin(root, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth);
		}
			for(GameTree child : children) {
				if(child.getValue() == value) {
					children.clear();
					return child.getState();
				}
			}	
		return null;
	}
	
	/**
	 * Given a state, expand its node to find the highest minimum value
	 * 
	 * @param root   the state whose potential next moves need to be searched
	 * @param alpha  the highest value discovered so far in this branch
	 * @param beta   the lowest value discovered so far in this branch 
	 * @param depth  the depth up to which to search to evaluate value
	 * @return the utility value of the node with the highest minimum utility
	 */
	private double findMax(State root, double alpha, double beta, int depth) {
		//evaluate the given state if depth is equal to the set LIMIT 4
		if(depth == LIMIT) {
			return evaluate(root);
		}
		double max = Double.NEGATIVE_INFINITY;
		Iterator<State> iterator = root.next().iterator();
		//Search for the next possible moves until the depth limit of search limit for steps is reached
		while(depth < LIMIT && !root.searchLimitReached() && iterator.hasNext()) {
			State nextState = iterator.next();
			//Create a new game tree for the next state
			GameTree nexttree = new GameTree(nextState);
			//if the depth is zero then we store the game tree in the array list to retrieve the best move after evaluation
			if (depth == 0)
				children.add(nexttree);
			// Find the least possible utility value the child node can have.
			nexttree.setValue(findMin(nextState, alpha, beta, depth + 1));
			max = Math.max(max, nexttree.getValue());
			if(max >= beta)
				return max;
			// Update alpha to be the lowest value discovered so far.
			alpha = Math.max(alpha, max);
		}
		return max;
	}
	
	/**
	 * For a state, expand its node to find the lowest maximum value
	 * 
	 * 
	 * @param root   the state whose potential next moves need to be searched
	 * @param alpha  the highest value discovered so far in this branch
	 * @param beta   the lowest value discovered so far in this branch 
	 * @param depth  the depth up to which to search to evaluate value
	 * @return the utility value of the node with the lowest maximum utility
	 */
	private double findMin(State root, double alpha, double beta, int depth) {
		//evaluate the given state if depth is equal to the set LIMIT 4
		if(depth == LIMIT) {
				return evaluate(root);
		}
		double min = Double.POSITIVE_INFINITY;
		Iterator<State> iterator = root.next().iterator();
		//Search for the next possible moves until the depth limit of search limit for steps is reached
		while(depth < LIMIT && !root.searchLimitReached() && iterator.hasNext()) {
			State nextState = iterator.next();
			//Create a new game tree for the next state
			GameTree nexttree = new GameTree(nextState);
			//if the depth is zero then we store the game tree in the array list to retrieve the best move after evaluation
			if (depth == 0)
				children.add(nexttree);
			// Find the highest possible utility value the child node can have.
			nexttree.setValue(findMax(nextState, alpha, beta, depth + 1));
			min = Math.min(min, nexttree.getValue());
			if(min <= alpha)
				return min;
			//Update beta to e the lowest value so far
			beta = Math.min(beta, min);
		}
		return min;
	}
	
	/**
	 * For a state, calculate the utility score based on the position and pieces score
	 * 
	 * 
	 * @param current   the state whose utility score needs to be calculated
	 * @return the utility score of the state 
	 */
	private double evaluate(State current) {
		double totalValue = 0.0;
		double whiteScore = 0.0;
		double blackScore = 0.0;
		//Positional Values for a Pawn piece
		double[][] pawn = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			    		   {5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
			    		   {1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0},
			    		   {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
			    		   {0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
			    		   {0.5,-0.5,-1.0, 0.0, 0.0,-1.0,-0.5, 0.5},
			    		   {0.5, 1.0, 1.0,-2.0,-2.0, 1.0, 1.0, 0.5},
			    		   {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}};
		
		//Positional Values for a Rook piece
		double[][] rook = {{ 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,  0.0},
			    		   { 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,  0.5},
			    		   {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			    		   {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			    		   {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			    		   {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			    		   {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
			    		   { 0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0,  0.0}};
		
		////Positional Values for a Bishop piece
		double[][] bishop = {{-2.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0, -2.0},
			    			 {-1.0, 0.0, 0.0, 0.0, 0.0,   0, 0.0, -1.0},
			    			 {-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0},
			    			 {-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0},
			    			 {-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0},
			    			 {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0},
			    			 {-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
			    			 {-2.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0, -2.0}};
		
		//Positional Values for a Knight piece
		double[][] knight = {{-5.0, -4.0,-3.0,-3.0,-3.0,-3.0,-4.0, -5.0},
			    			 {-4.0, -2.0, 0.0, 0.0, 0.0, 0.0,-2.0, -4.0},
			    			 {-3.0,  0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0},
			    			 {-3.0,  0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0},
			    			 {-3.0,  0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0},
			    			 {-3.0,  0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0},
			    			 {-4.0, -2.0, 0.0, 0.5, 0.5, 0.0,-2.0, -4.0},
			    			 {-5.0, -4.0,-3.0,-3.0,-3.0,-3.0,-4.0, -5.0}};
		
		////Positional Values for a Queen piece
		double[][] queen = {{-2.0,-1.0,-1.0,-0.5,-0.5,-1.0,-1.0, -2.0},
			    		    {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
			    		    {-1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
			    		    {-0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
			    		    { 0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
			    		    {-1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -0.1},
			    		    {-1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0},
			    		    {-2.0,-1.0,-1.0,-0.5,-0.5,-1.0,-1.0, -2.0}};
		
		//Search for all the pieces in the board and calculate the score for white player and black player
		Iterator<Piece> iterator = current.board.iterator();
		//While there are pieces on the board recognize the piece and assign them the score
		//Pawn is worth 8 pts
		//Bishop is worth 3.25 pawns
		//Rook is worth 5 pawns
		//Knight is worth 3.25 pawns
		//Queen is worth 9.75pawns
		//THE SCORES WERE SET AFTER A LOT OF TESTING 
		while(iterator.hasNext()){
			Piece piece = iterator.next();
			if(piece.player.equals(Player.WHITE) && (piece instanceof Pawn)) {
					whiteScore = whiteScore + 8.0 + pawn[piece.rank][piece.file];
			}
			else if(piece.player.equals(Player.WHITE) && (piece instanceof Queen)) {
					whiteScore = whiteScore +  78.0 + queen[piece.rank][piece.file];
			}				
			else if(piece.player.equals(Player.WHITE) && (piece instanceof Bishop)) {
					whiteScore = whiteScore + 26.0 + bishop[piece.rank][piece.file];
			}				
			else if(piece.player.equals(Player.WHITE) && (piece instanceof Rook)) {
					whiteScore = whiteScore +  40.0 + rook[piece.rank][piece.file];
			}				
			else if(piece.player.equals(Player.WHITE) &&(piece instanceof Knight)) {
					whiteScore = whiteScore +  26.0 + knight[piece.rank][piece.file];
			}		
			else if(piece.player.equals(Player.BLACK) && (piece instanceof Queen)) {
					blackScore = blackScore +  78.0 + queen[piece.rank][7 - piece.file];
			}
			else if(piece.player.equals(Player.BLACK) && (piece instanceof Pawn)) {
					blackScore = blackScore +  8.0 + pawn[piece.rank][7 - piece.file];
			}
			else if(piece.player.equals(Player.BLACK) && (piece instanceof Knight)) {
					blackScore = blackScore +  26.0 + knight[piece.rank][7 - piece.file];
			}
			else if(piece.player.equals(Player.BLACK) && (piece instanceof Bishop)) {
					blackScore = blackScore +  26.0 + bishop[piece.rank][7 - piece.file];
			}				
			else if(piece.player.equals(Player.BLACK) && (piece instanceof Rook)) {
					blackScore = blackScore +  40.0 + rook[piece.rank][7 - piece.file];
			}
		}		
		totalValue = whiteScore - blackScore;
		return totalValue;
	}
	/**
	 * A game tree is a representation of all the possible states along with 
	 * their values that could occur in a game 
	 * 
	 */
	private class GameTree{
		/** The current state of the game */
		private State state;
		/**The value of the state*/
		private double value;
		
		/**
		 * Constructs a new game tree with state as the root.
		 * 
		 * @param state the initial state of the game
		 */
		public GameTree(State state) {
			this.state = state;
		}
		
		/**
		 * Returns the state of represented by the node.
		 * 
		 * @return the state of the node
		 */
		public State getState() {
			return this.state;
		}
		
		/**
		 * Returns the value of represented by the state in the node.
		 * 
		 * @return the value of the state of the node
		 */
		public double getValue() {
			return this.value;
		}
		
		/**
		 * Sets the state of given to the node.
		 * 
		 */
		public void setValue(double value) {
			this.value = value;
		}
	}

}
