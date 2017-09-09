package info.jayharris.othello;

import java.util.*;
import java.util.function.Function;

import info.jayharris.othello.Othello.*;

public class Board {

    public static final int SIZE = 8;

    private final Square[][] squares;
    private final Set<Square> occupied;


    private Board() {
        squares = new Square[SIZE][SIZE];
        for (int rank = 0; rank < SIZE; ++rank) {
            for (int file = 0; file < SIZE; ++file) {
                squares[rank][file] = new Square(rank, file);
            }
        }

        occupied = new HashSet<>();
    }

    public boolean setPiece(Square square, Color color) {
        if (square.isOccupied()) {
            return false;
        }

        int numFlipped = flipInAllDirections(square, color);
        if (numFlipped == 0) {
            return false;
        }

        square.setColor(color);
        return true;
    }

    private int flipInAllDirections(Square square, Color color) {
        return 0;
    }

    private int flipInDirection(Square square, Color color, Function<Square, Optional<Square>> direction) {
        int count = 0;
        Optional<Square> nextOpt;
        Square next;

        while (true) {
            nextOpt = direction.apply(square);
            if (!nextOpt.isPresent()) {
                return count;
            }
            next = nextOpt.get();
            if (!next.color.isPresent()) {
                return count;
            }
            if (next.color.get() == color) {
                return count;
            }
            else {
                next.flip();
                ++count;
            }
        }
    }

    public static Board init() {
        Board board = new Board();

        int n = SIZE / 2 - 1;
        int w = SIZE / 2 - 1;
        int s = SIZE / 2;
        int e = SIZE / 2;

        board.squares[n][w].setColor(Color.WHITE);
        board.squares[n][e].setColor(Color.BLACK);
        board.squares[s][w].setColor(Color.BLACK);
        board.squares[s][e].setColor(Color.WHITE);

        board.occupied.addAll(Arrays.asList(
                board.squares[n][w], board.squares[n][e], board.squares[s][w], board.squares[s][e]));

        return board;
    }

    public String pretty() {
        final StringBuilder sb = new StringBuilder();

        sb.append("\n  abcdefgh\n");
        for (int rank = 0; rank < SIZE; ++rank) {
            sb.append("\n").append(rank + 1).append(" ");
            for (int file = 0; file < SIZE; ++file) {
                sb.append(squares[rank][file].pretty());
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Board{");
        sb.append("occupied=").append(occupied);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.equals(squares, board.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(squares);
    }

    class Square {

        public final int RANK, FILE;
        private Optional<Color> color;

        private Square nw, n, ne, e, se, s, sw, w;

        public Square(int rank, int file) {
            this.RANK = rank;
            this.FILE = file;
            this.color = Optional.empty();
        }

        private void setColor(Color color) {
            this.color = Optional.of(color);
        }

        private void flip() {
            color.ifPresent(c -> setColor(c.opposite()));
        }

        public boolean isOccupied() {
            return color.isPresent();
        }

        public Square getNW() {
            if (nw == null) {
                nw = (RANK - 1 >= 0 && FILE - 1 >= 0 ? squares[RANK - 1][FILE - 1] : null);
            }
            return nw;
        }

        public Square getN() {
            if (n == null) {
                n = (RANK - 1 >= 0 ? squares[RANK - 1][FILE] : null);
            }
            return n;
        }

        public Square getNE() {
            if (ne == null) {
                ne = (RANK - 1 >= 0 && FILE + 1 < SIZE ? squares[RANK - 1][FILE + 1] : null);
            }
            return ne;
        }

        public Square getE() {
            if (e == null) {
                e = (FILE + 1 < SIZE ? squares[RANK][FILE + 1] : null);
            }
            return e;
        }

        public Square getSE() {
            if (se == null) {
                se = (RANK + 1 < SIZE && FILE + 1 < SIZE ? squares[RANK + 1][FILE + 1] : null);
            }
            return se;
        }

        public Square getS() {
            if (s == null) {
                s = (RANK + 1 < SIZE ? squares[RANK + 1][FILE] : null);
            }
            return s;
        }

        public Square getSW() {
            if (sw == null) {
                sw = (RANK + 1 < SIZE && FILE - 1 >= 0 ? squares[RANK + 1][FILE - 1] : null);
            }
            return sw;
        }

        public Square getW() {
            if (w == null) {
                w = (FILE - 1 >= 0 ? squares[RANK][FILE - 1] : null);
            }
            return w;
        }

        public String pretty() {
            return color.map(c -> c == Color.WHITE ? "\u25cb" : "\u25cf").orElse(" ");
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Square{");
            sb.append("RANK=").append(RANK);
            sb.append(", FILE=").append(FILE);
            sb.append(", color=").append(color);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Square square = (Square) o;
            return RANK == square.RANK &&
                           FILE == square.FILE;
        }

        @Override
        public int hashCode() {
            return Objects.hash(RANK, FILE);
        }
    }

    class DirectionalIterator implements Iterator<Square> {

        Square current;
        Function<Square, Square> direction;

        DirectionalIterator(Square start, Direction direction) {
            this.current = start;
            this.direction = direction.fn;
        }

        @Override
        public boolean hasNext() {
            return Objects.nonNull(direction.apply(current));
        }

        @Override
        public Square next() {
            return direction.apply(current);
        }
    }

    enum Direction {
        NW(Square::getNW),
        N(Square::getN),
        NE(Square::getNE),
        E(Square::getE),
        SE(Square::getSE),
        S(Square::getS),
        SW(Square::getSW),
        W(Square::getW);

        public final Function<Square, Square> fn;

        Direction(Function<Square, Square> fn) {
            this.fn = fn;
        }
    }

    public static void main(String... args) {
        Board board = Board.init();

        System.out.println(board);

        board.setPiece(board.getSquare(3,2), Color.BLACK);

        System.out.println(board);
    }
}
