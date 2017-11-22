package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Othello.Color;

public class GreedyHeuristic extends HeuristicFunction {

    public GreedyHeuristic(Color color) {
        super(color);
    }

    @Override
    public long apply(Board board) {
        return board.getOccupied().stream()
                .filter(square -> square.getColor() == color)
                .count();
    }
}
