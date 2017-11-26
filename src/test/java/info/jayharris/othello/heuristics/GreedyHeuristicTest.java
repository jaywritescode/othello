package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.BoardFactory;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GreedyHeuristicTest {

    @Test
    @DisplayName("counts the number of our discs on the board")
    public void testGreedyHeuristic() throws Exception {
        Board board = BoardFactory.getFactory().fromString(
                "        " +
                "        " +
                "    w   " +
                "  bbbw  " +
                "  wwwww " +
                "    bbw " +
                "     b  " +
                "        "
        );

        assertThat(new GreedyHeuristic(Color.BLACK).apply(board)).isEqualTo(6);
    }
}
