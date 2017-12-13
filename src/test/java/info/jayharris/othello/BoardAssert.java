package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import org.apache.commons.collections4.iterators.ZippingIterator;
import org.assertj.core.api.AbstractAssert;

import java.util.Objects;

public class BoardAssert extends AbstractAssert<BoardAssert, Board> {

    public BoardAssert(Board board) {
        super(board, BoardAssert.class);
    }

    public static BoardAssert assertThat(Board actual) {
        return new BoardAssert(actual);
    }

    public BoardAssert matches(Board expected) {
        isNotNull();

        ZippingIterator<Square> iter = new ZippingIterator<>(
                BoardUtils.boardIterator(expected), BoardUtils.boardIterator(actual));

        while (iter.hasNext()) {
            if (!Objects.equals(iter.next().getColor(), iter.next().getColor())) {
                failWithMessage("Expected <%s>\nbut got <%s>", expected.pretty(), actual.pretty());
            }
        }

        return this;
    }
}
