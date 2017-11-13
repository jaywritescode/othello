package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("chooses the move with the highest score")
    void testGetMove() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "  w     " +
                "  wwww  " +
                "bbbbwwww" +
                " wbwbbww" +
                "wwbbwbww" +
                "w bbbww " +
                "  bbbbw " +
                "   bbb  "
        );
        boardField.set(othello, board);

        assertThat(player.getMove(othello)).isSameAs(board.getSquare('h', 8));
    }

    @Test
    @DisplayName("get all stable discs")
    void testGetAllStableDiscs() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "   www  " +
                "  bwwb b" +
                "wbwwwwbb" +
                "wwwwbwbb" +
                "wwwbwwbb" +
                "wwwwwbbb" +
                "  bbbbbb" +
                " bbbbbbb"
        );

        Set<Square> stable = Stream.of(
                board.getSquare('h', 2),
                board.getSquare('g', 3),
                board.getSquare('h', 3),
                board.getSquare('g', 4),
                board.getSquare('h', 4),
                board.getSquare('g', 5),
                board.getSquare('h', 5),
                board.getSquare('d', 6),
                board.getSquare('f', 6),
                board.getSquare('g', 6),
                board.getSquare('h', 6),
                board.getSquare('c', 7),
                board.getSquare('d', 7),
                board.getSquare('e', 7),
                board.getSquare('f', 7),
                board.getSquare('g', 7),
                board.getSquare('h', 7),
                board.getSquare('b', 8),
                board.getSquare('c', 8),
                board.getSquare('d', 8),
                board.getSquare('e', 8),
                board.getSquare('f', 8),
                board.getSquare('g', 8),
                board.getSquare('h', 8)).collect(Collectors.toSet());

        assertThat(player.getStableDiscs(board)).isEqualTo(stable);
    }
}
