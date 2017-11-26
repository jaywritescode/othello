package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public class BoardFactory {

    private static BoardFactory factory;

    private static Method setColorMethod;
    private static Field occupiedField, potentialMovesField;

    private BoardFactory() throws Exception {
        setColorMethod = Square.class.getDeclaredMethod("setColor", Color.class);
        setColorMethod.setAccessible(true);

        occupiedField = Board.class.getDeclaredField("occupied");
        occupiedField.setAccessible(true);

        potentialMovesField = Board.class.getDeclaredField("potentialMoves");
        potentialMovesField.setAccessible(true);
    }

    public static BoardFactory getFactory() throws Exception {
        if (factory == null) {
            factory = new BoardFactory();
        }
        return factory;
    }

    public Board newGame() throws Exception {
        return Board.init();
    }

    public Board fromString(String string) throws Exception {
        Board board = Board.init();

        final Set<Square> occupied = (Set<Square>) occupiedField.get(board);

        Square square;
        int rank = 0, file = 0;

        for (char c : string.toCharArray()) {
            if (c == 'b' || c == 'w') {
                square = board.getSquare(rank, file);

                setColorMethod.invoke(square, c == 'b' ? Color.BLACK : Color.WHITE);
                occupied.add(square);
            }

            file = (file + 1) % Board.SIZE;
            if (file == 0) {
                ++rank;
            }
        }

        potentialMovesField.set(board, occupied.stream()
                .flatMap(sq -> sq.getNeighbors().stream())
                .filter(Square::isUnoccupied)
                .collect(Collectors.toSet()));
        return board;
    }
}
