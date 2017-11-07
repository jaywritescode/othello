package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.BoardUtils.Direction;
import info.jayharris.othello.BoardUtils.DirectionalIterator;
import info.jayharris.othello.Othello.Color;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class PositionalPlayer extends Player {

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

    public PositionalPlayer(Color color) {
        this(color, DEFAULT_VALUES);
    }

    public PositionalPlayer(Color color, int[][] squareValues) {
        super(color);
        this.squareValues = squareValues;
    }

    @Override
    public Square getMove(Othello othello) {
        return getLegalMoves(othello.getBoard()).stream()
                .max(Comparator.comparingInt(it -> valueOfFlippingDiscs(it)))
                .orElseThrow(IllegalStateException::new);
    }

    private int valueOfFlippingDiscs(Square square) {
        return EnumSet.allOf(Direction.class).stream()
                .collect(Collectors.summingInt(it -> valueOfFlippingDiscsInDirection(square, it)));
    }

    private int valueOfFlippingDiscsInDirection(Square square, Direction direction) {
        Square neighbor = direction.go(square);
        if (neighbor == null || neighbor.getColor() == null || neighbor.getColor() == color) {
            return 0;
        }

        int sum = 0;
        DirectionalIterator iter = BoardUtils.directionalIterator(neighbor, direction);
        while (iter.hasNext()) {
            neighbor = iter.next();
            if (neighbor.getColor() == null) {
                return 0;
            }
            if (neighbor.getColor() == color) {
                return sum;
            }

            sum += squareValues[neighbor.RANK][neighbor.FILE];
        }
        return 0;
    }
}
