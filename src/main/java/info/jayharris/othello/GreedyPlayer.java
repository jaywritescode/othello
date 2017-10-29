package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.BoardUtils.Direction;
import info.jayharris.othello.BoardUtils.DirectionalIterator;
import info.jayharris.othello.Othello.Color;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GreedyPlayer extends Player {

    public GreedyPlayer(Color color) {
        super(color);
    }

    @Override
    public Square getMove(Othello othello) {
        return getLegalMoves(othello.getBoard()).stream()
                .max(Comparator.comparingInt(it -> discsFlippedByPlaying(it)))
                .orElseThrow(IllegalStateException::new);
    }

    private int discsFlippedByPlaying(Square square) {
        return EnumSet.allOf(Direction.class).stream().collect(
                Collectors.summingInt(it -> discsFlippedInDirection(square, it)));
    }

    private int discsFlippedInDirection(Square square, Direction direction) {
        Square neighbor = direction.go(square);
        if (neighbor == null || neighbor.getColor() == null || neighbor.getColor() == color) {
            return 0;
        }

        int count = 1;
        DirectionalIterator iter = BoardUtils.directionalIterator(neighbor, direction);
        while (iter.hasNext()) {
            neighbor = iter.next();
            if (neighbor.getColor() == null) {
                return 0;
            }
            if (neighbor.getColor() == color) {
                return count;
            }
            ++count;
        }
        return 0;
    }
}
