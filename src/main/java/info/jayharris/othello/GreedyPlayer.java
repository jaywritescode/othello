package info.jayharris.othello;

import info.jayharris.othello.BoardUtils.Direction;
import info.jayharris.othello.BoardUtils.DirectionalIterator;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class GreedyPlayer extends Player {

    public GreedyPlayer(Color color) {
        super(color);
    }

    @Override
    public Square getMove(Othello othello) {
        Collection<Square> legalMoves = othello.getBoard().getLegalMovesFor(this);

        return legalMoves.stream()
                .collect(Collectors.maxBy(Comparator.comparingInt(this::countFlippedSquares)))
                .get();
    }

    /**
     * Count how many discs we'll flip if we play at `square`.
     *
     * The caller is responsible for assuring that `square` actually belongs to the board that we care about.
     *
     * @param square the square
     * @return the number of discs that we'll flip by playing at square
     */
    private int countFlippedSquares(Square square) {
        return EnumSet.allOf(Direction.class).stream()
                .mapToInt(direction -> countFlippedSquaresInDirection(square, direction))
                .sum();
    }

    private int countFlippedSquaresInDirection(Square square, Direction direction) {
        Square neighbor = direction.go(square);

        if (neighbor == null || neighbor.getColor() != color.opposite()) {
            return 0;
        }

        int count = 0;
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
