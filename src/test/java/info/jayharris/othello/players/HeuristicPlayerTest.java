package info.jayharris.othello.players;

import info.jayharris.othello.Board;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.BoardFactory;
import info.jayharris.othello.Othello;
import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.Player;
import info.jayharris.othello.heuristics.HeuristicFunction;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(Lifecycle.PER_CLASS)
public class HeuristicPlayerTest {

    HeuristicFunction maximizingFunction, minimizingFunction;

    Board board;
    Player player;
    Othello othello;

    static Field boardField;

    class TestHeuristicFunction extends HeuristicFunction {
        Map<Board, Long> values;

        public TestHeuristicFunction(Color color, Map<Board, Long> values) {
            super(color);
            this.values = values;
        }

        @Override
        public long apply(Board board) {
            return values.get(board);
        }
    }

    @BeforeAll
    void init() throws Exception {
        boardField = Othello.class.getDeclaredField("board");
        boardField.setAccessible(true);

        board = BoardFactory.getFactory().fromString(
                "        " +
                "  bw    " +
                "  bb    " +
                " bbbbw  " +
                "  bbbb  " +
                "  wwb   " +
                "     b  " +
                "        "
        );

        maximizingFunction = new TestHeuristicFunction(Color.WHITE, createMappingForTestFunction(board));
        minimizingFunction = new TestHeuristicFunction(Color.WHITE, createMappingForTestFunction(board)) {
            @Override
            public Comparator<Square> comparator(Board board) {
                return super.comparator(board).reversed();
            }
        };
    }

    private Map<Board, Long> createMappingForTestFunction(Board board) {
        Map<Board, Long> mockHeuristicValues = new HashMap<>();
        Stream.of(
                ImmutablePair.of(board.getSquare('c', 1), 5L),
                ImmutablePair.of(board.getSquare('b', 2), -17L),
                ImmutablePair.of(board.getSquare('a', 3), 6L),
                ImmutablePair.of(board.getSquare('f', 3), 7L),
                ImmutablePair.of(board.getSquare('a', 4), -1L),
                ImmutablePair.of(board.getSquare('a', 5), 3L),
                ImmutablePair.of(board.getSquare('f', 6), 13L)
        ).forEach(pair -> {
            Board copy = Board.deepCopy(board);
            copy.setPiece(copy.getSquare(pair.getLeft()), Color.WHITE);
            mockHeuristicValues.put(copy, pair.getRight());
        });
        return mockHeuristicValues;
    }

    @Test
    @DisplayName("it chooses the move with the greatest heuristic value")
    void testMaximizingHeuristic() throws Exception {
        player = new HeuristicPlayer(Color.WHITE, maximizingFunction);
        othello = new Othello(null, player);
        boardField.set(othello, board);

        assertThat(player.getMove(othello)).isEqualTo(othello.getBoard().getSquare('f', 6));
    }

    @Test
    @DisplayName("it chooses the move with the smallest heuristic value")
    void testMinimizingHeuristic() throws Exception {
        player = new HeuristicPlayer(Color.WHITE, minimizingFunction);
        othello = new Othello(null, player);
        boardField.set(othello, board);

        assertThat(player.getMove(othello)).isEqualTo(othello.getBoard().getSquare('b', 2));
    }
}
