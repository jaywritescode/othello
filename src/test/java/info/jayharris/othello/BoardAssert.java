package info.jayharris.othello;

import org.assertj.core.api.AbstractAssert;

public class BoardAssert extends AbstractAssert<BoardAssert, Board> {

    public BoardAssert(Board board) {
        super(board, BoardAssert.class);
    }

    public static BoardAssert assertThat(Board actual) {
        return new BoardAssert(actual);
    }

    public BoardAssert matches(Board expected) {
        isNotNull();

        for (int rank = 0; rank < Board.SIZE; ++rank) {
            for (int file = 0; file < Board.SIZE; ++file) {
                if (expected.getSquare(rank, file).getColor() != actual.getSquare(rank, file).getColor()) {
                    failWithMessage("Expected <%s>\nbut got <%s>", expected.pretty(), actual.pretty());
                }
            }
        }

        return this;
    }
}
