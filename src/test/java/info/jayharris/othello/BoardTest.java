package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static info.jayharris.othello.BoardAssert.assertThat;


public class BoardTest {

    @Test
    @DisplayName("should put the piece on the board and flip the correct discs")
    void testLegalPlay() throws Exception {
        Board board = Board.init();

        Method method = Board.Square.class.getDeclaredMethod("setColor", Color.class);
        method.setAccessible(true);

        method.invoke(board.getSquare('c', 3), Color.WHITE);
        method.invoke(board.getSquare('d', 3), Color.BLACK);
        method.invoke(board.getSquare('c', 4), Color.WHITE);
        method.invoke(board.getSquare('d', 4), Color.BLACK);
        method.invoke(board.getSquare('e', 4), Color.BLACK);
        method.invoke(board.getSquare('c', 5), Color.WHITE);
        method.invoke(board.getSquare('d', 5), Color.BLACK);
        method.invoke(board.getSquare('e', 5), Color.WHITE);
        method.invoke(board.getSquare('d', 6), Color.BLACK);

        board.setPiece(board.getSquare('e', 3), Color.WHITE);

        Board expected = Board.init();

        method.invoke(expected.getSquare('c', 3), Color.WHITE);
        method.invoke(expected.getSquare('d', 3), Color.WHITE);
        method.invoke(expected.getSquare('e', 3), Color.WHITE);
        method.invoke(expected.getSquare('c', 4), Color.WHITE);
        method.invoke(expected.getSquare('d', 4), Color.WHITE);
        method.invoke(expected.getSquare('e', 4), Color.WHITE);
        method.invoke(expected.getSquare('c', 5), Color.WHITE);
        method.invoke(expected.getSquare('d', 5), Color.BLACK);
        method.invoke(expected.getSquare('e', 5), Color.WHITE);
        method.invoke(expected.getSquare('d', 6), Color.BLACK);

        assertThat(board).matches(expected);
    }
}
