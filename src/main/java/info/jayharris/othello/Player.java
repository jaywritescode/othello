package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.*;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class Player {

    public final Color color;

    public Player(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract Board.Square getMove(Othello othello);

    public Set<Square> getLegalMoves(Board board) {
        return board.getPotentialMoves().stream()
                .filter(it -> it.isLegalMove(color))
                .collect(Collectors.toSet());
    }
}
