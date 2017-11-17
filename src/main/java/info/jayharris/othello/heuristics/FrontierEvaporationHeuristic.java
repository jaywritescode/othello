package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

/**
 * This prefers moves that minimize the number of {@code color} discs
 * adjacent to one or more unoccupied squares.
 */
public class FrontierEvaporationHeuristic extends HeuristicFunction {

    public FrontierEvaporationHeuristic(Color color) {
        super(color);
    }

    @Override
    public long apply(Board board) {
        long frontierSquares = board.getOccupied().stream()
                .filter(it -> it.getColor() == color)
                .filter(it -> it.getNeighbors().stream().anyMatch(Square::isUnoccupied))
                .count();

        return -frontierSquares;
    }
}
