package info.jayharris.othello;

import info.jayharris.othello.Othello.*;

public abstract class Player {

    public final Color color;

    public Player(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract Board.Square getMove(Othello othello);
}
