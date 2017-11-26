package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.BoardFactory;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PositionalHeuristicTest {

    @Test
    @DisplayName("it adds the values of the squares we occupy minus the squares our opponent occupies")
    public void testPositionalHeuristic() throws Exception {
        Board board = BoardFactory.getFactory().fromString(
                "        " +
                "        " +
                "        " +
                "   wb   " +
                "   wbb  " +
                "   www  " +
                "    bw  " +
                "        "
        );

        assertThat(new PositionalHeuristic(Color.BLACK).apply(board)).isEqualTo(-10);
    }
}
