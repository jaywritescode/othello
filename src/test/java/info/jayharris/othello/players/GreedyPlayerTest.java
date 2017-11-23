package info.jayharris.othello.players;

import info.jayharris.othello.*;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@Deprecated
public class GreedyPlayerTest {

    Player player;
    Othello othello;

    static Field boardField;

    @BeforeAll
    static void init() throws Exception {
        boardField = Othello.class.getDeclaredField("board");
        boardField.setAccessible(true);
    }

    @BeforeEach
    void initTest() throws Exception {
        player = new GreedyPlayer(Color.BLACK);
        othello = new Othello(player, null);
    }

    @Test
    @DisplayName("chooses the move that flips the most pieces")
    void testGetMove() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "        " +
                "        " +
                "    w   " +
                "  bbbb  " +
                "  wbbb  " +
                "   wwww " +
                "     b  " +
                "        "
        );
        boardField.set(othello, board);

        assertThat(player.getMove(othello)).isSameAs(othello.getBoard().getSquare('d', 7));
    }
}
