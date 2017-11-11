package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.BoardUtils.Direction;
import info.jayharris.othello.BoardUtils.DirectionalIterator;
import info.jayharris.othello.Othello.Color;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A player that tries to maximize the number of stable discs they have on the board.
 */
public class StableDiscsPlayer extends Player {

    public StableDiscsPlayer(Color color) {
        super(color);
    }

    @Override
    public Square getMove(Othello othello) {
        return null;
    }

    public Set<Square> getStableDiscs(Board board) {
        Set<Square> stable = new HashSet<>();

        class Corner {
            final Square square;
            final Collection<Direction> directions;

            Corner(final Square square, final Collection<Direction> directions) {
                this.square = square;
                this.directions = directions;
            }

            boolean isUnoccupied() {
                return square.isUnoccupied();
            }

            Square getSquare() {
                return square;
            }

            Color getColor() {
                return square.getColor();
            }
        }

        Stream<Corner> corners = Stream.of(
                new Corner(board.getSquare(0, 0), Arrays.asList(Direction.E, Direction.S)),
                new Corner(board.getSquare(0, Board.SIZE - 1), Arrays.asList(Direction.W, Direction.S)),
                new Corner(board.getSquare(Board.SIZE - 1, 0), Arrays.asList(Direction.E, Direction.N)),
                new Corner(board.getSquare(Board.SIZE - 1, Board.SIZE - 1), Arrays.asList(Direction.W, Direction.N)));

        corners.forEach(corner -> {
            if (corner.isUnoccupied()) {
                return;
            }

            Color color = corner.getColor();

            Square current, neighbor;
            Deque<Square> queue = new LinkedList<>(Collections.singletonList(corner.getSquare()));
            while (!queue.isEmpty()) {
                current = queue.pop();
                stable.add(current);
                for (Direction dir : corner.directions) {
                    neighbor = dir.go(current);
                    if (neighbor != null && neighbor.getColor() == color) {
                        queue.add(neighbor);
                    }
                }
            }
        });

        board.getOccupied().stream()
                .filter(f -> !stable.contains(f))
                .filter(f -> EnumSet.allOf(Direction.class).stream().noneMatch(dir -> BoardUtils.directionalIterator(f, dir).hasUnoccupiedSquare()))
                .forEach(f -> stable.add(f));

        return stable;
    }
}
