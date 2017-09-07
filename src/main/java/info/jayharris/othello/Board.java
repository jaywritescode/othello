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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("\n  abcdefgh\n");
        for (int rank = 0; rank < SIZE; ++rank) {
            sb.append("\n").append(rank + 1).append(" ");
            for (int file = 0; file < SIZE; ++file) {
                sb.append(squares[rank][file].toString());
            }
        }

        return sb.toString();
    }

    class Square {

        public final int RANK, FILE;
        private Optional<Color> color;

        private Optional<Square> nw, n, ne, e, se, s, sw, w;

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

        public Optional<Square> getNW() {
            if (nw == null) {
                nw = (RANK - 1 >= 0 && FILE - 1 >= 0 ? Optional.of(squares[RANK - 1][FILE - 1]) : Optional.empty());
            }
            return nw;
        }

        public Optional<Square> getN() {
            if (n == null) {
                n = (RANK - 1 >= 0 ? Optional.of(squares[RANK - 1][FILE]) : Optional.empty());
            }
            return nw;
        }

        public Optional<Square> getNE() {
            if (ne == null) {
                ne = (RANK - 1 >= 0 && FILE + 1 < SIZE ? Optional.of(squares[RANK - 1][FILE + 1]) : Optional.empty());
            }
            return ne;
        }

        public Optional<Square> getE() {
            if (e == null) {
                e = (FILE + 1 < SIZE ? Optional.of(squares[RANK][FILE + 1]) : Optional.empty());
            }
            return e;
        }

        public Optional<Square> getSE() {
            if (se == null) {
                se = (RANK + 1 < SIZE && FILE + 1 < SIZE ? Optional.of(squares[RANK + 1][FILE + 1]) : Optional.empty());
            }
            return se;
        }

        public Optional<Square> getS() {
            if (s == null) {
                s = (RANK + 1 < SIZE ? Optional.of(squares[RANK + 1][FILE]) : Optional.empty());
            }
            return s;
        }

        public Optional<Square> getSW() {
            if (sw == null) {
                sw = (RANK + 1 < SIZE && FILE - 1 >= 0 ? Optional.of(squares[RANK + 1][FILE - 1]) : Optional.empty());
            }
            return sw;
        }

        public Optional<Square> getW() {
            if (w == null) {
                w = (FILE - 1 >= 0 ? Optional.of(squares[RANK][FILE - 1]) : Optional.empty());
            }
            return w;
        }

        @Override
        public String toString() {
            return color.map(c -> c == Color.WHITE ? "\u25cb" : "\u25cf").orElse(" ");
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

    public static void main(String... args) {
        Board board = Board.init();

        System.out.println(board.squares[3][3]);
        System.out.println(board.squares[3][4]);
        System.out.println(board);
    }
}
