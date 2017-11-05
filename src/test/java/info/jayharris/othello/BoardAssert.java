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

        if (!BoardUtils.matches(expected, actual)) {
            failWithMessage("Expected <%s>\nbut got <%s>", expected.pretty(), actual.pretty());
        }

        return this;
    }
}
