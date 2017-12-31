package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.util.Comparator;

public class GreedyHeuristic extends HeuristicFunction {

    public GreedyHeuristic(Color color) {
        super(color, OptimizingReducers.MAXIMIZE_HEURISTIC_VALUE);
    }

    @Override
    public long apply(Board board) {
        return board.getOccupied().stream()
                .filter(square -> square.getColor() == color)
                .count();
    }
}
