package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.BoardFactory;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MobilityHeuristicTest {

    @Test
    @DisplayName("returns the (negative of) the number of legal moves the opponent has")
    public void testMobilityHeuristic() throws Exception {
        Board board = BoardFactory.instance().fromString(
                " wwwwww " +
                "  wwww  " +
                "wwbwwbw " +
                "wwbwwww " +
                "wwbbbw  " +
                " wwwbw  " +
                "  wbww  " +
                "  ww w  "
        );

        assertThat(new MobilityHeuristic(Color.BLACK).apply(board)).isEqualTo(-3);
    }
}
