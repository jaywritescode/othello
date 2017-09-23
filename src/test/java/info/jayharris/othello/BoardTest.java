package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static info.jayharris.othello.BoardAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BoardTest {

//    @Test
//    @DisplayName("makes a legal play")
//    void testLegalPlay() throws Exception {
//        Board board = Board.init();
//
//        Method method = Board.Square.class.getDeclaredMethod("setColor", Color.class);
//        method.setAccessible(true);
//
//        method.invoke(board.getSquare('c', 3), Color.WHITE);
//        method.invoke(board.getSquare('d', 3), Color.BLACK);
//        method.invoke(board.getSquare('c', 4), Color.WHITE);
//        method.invoke(board.getSquare('d', 4), Color.BLACK);
//        method.invoke(board.getSquare('e', 4), Color.BLACK);
//        method.invoke(board.getSquare('c', 5), Color.WHITE);
//        method.invoke(board.getSquare('d', 5), Color.BLACK);
//        method.invoke(board.getSquare('e', 5), Color.WHITE);
//        method.invoke(board.getSquare('d', 6), Color.BLACK);
//
//        board.setPiece(board.getSquare('e', 3), Color.WHITE);
//
//        Board expected = Board.init();
//
//        method.invoke(expected.getSquare('c', 3), Color.WHITE);
//        method.invoke(expected.getSquare('d', 3), Color.WHITE);
//        method.invoke(expected.getSquare('e', 3), Color.WHITE);
//        method.invoke(expected.getSquare('c', 4), Color.WHITE);
//        method.invoke(expected.getSquare('d', 4), Color.WHITE);
//        method.invoke(expected.getSquare('e', 4), Color.WHITE);
//        method.invoke(expected.getSquare('c', 5), Color.WHITE);
//        method.invoke(expected.getSquare('d', 5), Color.BLACK);
//        method.invoke(expected.getSquare('e', 5), Color.WHITE);
//        method.invoke(expected.getSquare('d', 6), Color.BLACK);
//
//        assertThat(board).matches(expected);
//    }

//    @Test
//    @DisplayName("ignores an illegal play")
//    void testIllegalMove() throws Exception {
//
//    }

    @Test
    @DisplayName("should tell if the player has any legal moves")
    void testHasMoveFor() throws Exception {
        Player white = mock(Player.class);
        when(white.getColor()).thenReturn(Color.WHITE);

        Player black = mock(Player.class);
        when(black.getColor()).thenReturn(Color.BLACK);

        Board board;
        board = BoardFactory.instance().fromString(
                "wwwwwwww" +
                "wwwwwwww" +
                "wwwwwwww" +
                "wwwwwww " +
                "wwwwww  " +
                "wwwwww b" +
                "wwwwwww " +
                "wwwwwwww"
        );

        assertFalse(board.hasMoveFor(white));
        assertFalse(board.hasMoveFor(black));

        board = BoardFactory.instance().fromString(
                "   www  " +
                "  bwwb b" +
                "wbwwwwbb" +
                "wwwwbwbb" +
                "wwwbwwbb" +
                "wwwwwbbb" +
                "  bbbbbb" +
                " bbbbbbb"
        );

        assertTrue(board.hasMoveFor(white));
        assertTrue(board.hasMoveFor(black));
    }

    @Test
    @DisplayName("should get the square's neighbors")
    void testGetNeighbors() throws Exception {
        Board board = BoardFactory.instance().newGame();

        Set<Square> expected = Stream.of(
                board.getSquare('a', 4),
                board.getSquare('b', 4),
                board.getSquare('b', 5),
                board.getSquare('b', 6),
                board.getSquare('a', 6)
        ).collect(Collectors.toSet());
        assertEquals(expected, board.getSquare('a', 5).getNeighbors());

        // return cached value
        assertEquals(expected, board.getSquare('a', 5).getNeighbors());
    }

    @Test
    @DisplayName("should tell if this move is legal")
    void testIsLegalMove() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "  wwww  " +
                "b bwbw  " +
                "bbbwbwww" +
                "bwwbbwww" +
                "bwbbwwww" +
                " wwwwwww" +
                "        " +
                "        "
        );

        System.out.println(board.pretty());

        // square is occupied
        assertFalse(board.getSquare('a', 2).isLegalMove(Color.BLACK));

        // neighboring square is null
        // neighboring square is empty
        // neighboring square is same color
        assertFalse(board.getSquare('h', 2).isLegalMove(Color.WHITE));

        // iterator hits an empty square
        assertFalse(board.getSquare('b', 1).isLegalMove(Color.BLACK));

        // iterator hits the end of the board
        assertFalse(board.getSquare('f', 7).isLegalMove(Color.WHITE));

        assertTrue(board.getSquare('a', 6).isLegalMove(Color.BLACK));
    }
}
