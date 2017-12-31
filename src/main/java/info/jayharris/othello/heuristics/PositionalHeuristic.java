package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.util.stream.Collectors;

/**
 * A heuristic that is assigned a quality measure for each square on the board.
 * The overall quality of a particular position is the sum of the values of
 * the squares occupied by {@code color} less the sum of the values of the
 * squares occupied by {@code color.opposite()}.
 */
public class PositionalHeuristic extends HeuristicFunction {

    public final int[][] squareValues;

    static final int[][] DEFAULT_VALUES = {
            {99,  -8,  8,  6,  6,  8,  -8, 99},
            {-8, -24, -4, -3, -3, -4, -24, -8},
            { 8,  -4,  7,  4,  4,  7,  -4,  8},
            { 6,  -3,  4,  0,  0,  4,  -3,  6},
            { 6,  -3,  4,  0,  0,  4,  -3,  6},
            { 8,  -4,  7,  4,  4,  7,  -4,  8},
            {-8, -24, -4, -3, -3, -4, -24, -8},
            {99,  -8,  8,  6,  6,  8,  -8, 99},
    };

    public PositionalHeuristic(Color color) {
        this(color, DEFAULT_VALUES);
    }

    public PositionalHeuristic(Color color, int[][] squareValues) {
        super(color, OptimizingReducers.MAXIMIZE_HEURISTIC_VALUE);
        this.squareValues = squareValues;
    }

    @Override
    public long apply(Board board) {
        return board.getOccupied().stream()
                .collect(Collectors.summingLong(square -> getSquareValue(square)));
    }

    private long getSquareValue(Square square) {
        long v = squareValues[square.RANK][square.FILE];
        return v * (square.getColor() == color ? 1 : -1);
    }
}
