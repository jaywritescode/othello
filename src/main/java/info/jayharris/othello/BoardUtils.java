package info.jayharris.othello;

import info.jayharris.othello.Board.Square;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class BoardUtils {

    public static DirectionalIterator directionalIterator(Square start, Direction direction) {
        return new DirectionalIterator(start, direction);
    }

    public enum Direction {
        NW(Square::getNW),
        N(Square::getN),
        NE(Square::getNE),
        E(Square::getE),
        SE(Square::getSE),
        S(Square::getS),
        SW(Square::getSW),
        W(Square::getW);

        private final Function<Square, Square> fn;

        Direction(Function<Square, Square> fn) {
            this.fn = fn;
        }

        public Square go(Square from) {
            return fn.apply(from);
        }
    }

    /**
     * An iterator that traverses the board in a given direction and stops
     * when it passes the edge of the board.
     */
    public static class DirectionalIterator implements Iterator<Square> {

        Square current;
        final Direction direction;

        DirectionalIterator(Square start, Direction direction) {
            this.current = start;
            this.direction = direction;
        }

        @Override
        public boolean hasNext() {
            return Objects.nonNull(direction.go(current));
        }

        @Override
        public Square next() {
            return this.current = direction.go(current);
        }

        public boolean hasUnoccupiedSquare() {
            while (hasNext()) {
                if (next().isUnoccupied()) {
                    return true;
                }
            }
            return false;
        }
    }

    public static BoardIterator boardIterator(Board board) {
        return new BoardIterator(board);
    }

    public static class BoardIterator implements Iterator<Square> {

        Board board;
        int rank, file;

        BoardIterator(Board board) {
            this.board = board;
            this.rank = 0;
            this.file = 0;
        }

        @Override
        public boolean hasNext() {
            return rank <= Board.SIZE && file <= Board.SIZE;
        }

        @Override
        public Square next() {
            Square square = board.getSquare(rank, file);

            rank = (rank + 1) % Board.SIZE;
            if (rank == 0) {
                ++file;
            }

            return square;
        }
    }
}
