package info.jayharris.othello.players;

import info.jayharris.othello.Board;
import info.jayharris.othello.BoardFactory;
import info.jayharris.othello.Othello;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@Deprecated
public class StableDiscsPlayerTest {

    StableDiscsPlayer player;
    Othello othello;

    static Field boardField;

    @BeforeAll
    static void init() throws Exception {
        boardField = Othello.class.getDeclaredField("board");
        boardField.setAccessible(true);
    }

    @BeforeEach
    void initTest() throws Exception {
        player = new StableDiscsPlayer(Color.BLACK);
        othello = new Othello(player, null);
    }


    @Test
    @DisplayName("chooses the move with more stable discs")
    void testGetMove() throws Exception {
        Board board = BoardFactory.getFactory().fromString(
                "   b    " +
                "  wwwww " +
                "   ww w " +
                "bwwww wb" +
                "bbwwwwwb" +
                "bbwbwbwb" +
                "bbwwbbb " +
                "b wbbb  "
        );
        boardField.set(othello, board);

        // scores:
        // e1: 5
        // f1: 5
        // g1: 5
        // a3: 6
        // b3: 5
        // c3: 5
        // f4: 5
        // b8: 10

        assertThat(player.getMove(othello)).isSameAs(board.getSquare('b', 8));
    }

//    @Test
//    @DisplayName("get all stable discs")
//    void testGetAllStableDiscs() throws Exception {
//        Board board = BoardFactory.getFactory().fromString(
//                "   www  " +
//                "  bwwb b" +
//                "wbwwwwbb" +
//                "wwwwbwbb" +
//                "wwwbwwbb" +
//                "wwwwwbbb" +
//                "  bbbbbb" +
//                " bbbbbbb"
//        );
//
//        Set<Square> stable = Stream.of(
//                board.getSquare('h', 2),
//                board.getSquare('g', 3),
//                board.getSquare('h', 3),
//                board.getSquare('g', 4),
//                board.getSquare('h', 4),
//                board.getSquare('g', 5),
//                board.getSquare('h', 5),
//                board.getSquare('d', 6),
//                board.getSquare('f', 6),
//                board.getSquare('g', 6),
//                board.getSquare('h', 6),
//                board.getSquare('c', 7),
//                board.getSquare('d', 7),
//                board.getSquare('e', 7),
//                board.getSquare('f', 7),
//                board.getSquare('g', 7),
//                board.getSquare('h', 7),
//                board.getSquare('b', 8),
//                board.getSquare('c', 8),
//                board.getSquare('d', 8),
//                board.getSquare('e', 8),
//                board.getSquare('f', 8),
//                board.getSquare('g', 8),
//                board.getSquare('h', 8)).collect(Collectors.toSet());
//
//        assertThat(player.getStableDiscs(board)).isEqualTo(stable);
//    }
}
