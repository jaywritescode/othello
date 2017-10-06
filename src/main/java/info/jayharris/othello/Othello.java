package info.jayharris.othello;

public class Othello {

    public enum Color {
        BLACK, WHITE;

        public Color opposite() {
            return this == Color.WHITE ? Color.BLACK : Color.WHITE;
        }
    };

    private final Board board;
    private final Player black, white;
    private Player current;

    public Othello(Player black, Player white) {
        this.board = Board.init();

        this.black = this.current = black;
        this.white = white;
    }

    public Player nextPly() {
        Board.Square move;

        do {
            move = current.getMove(this);
        } while (!move.isLegalMove(current.getColor()));

        board.setPiece(move, current.getColor());

        return nextPlayer();
    }

    private Player nextPlayer() {
        Player next = (current == black ? white : black);

        if (board.hasMoveFor(next)) {
            return next;
        }
        if (board.hasMoveFor(current)) {
            return current;
        }
        return null;
    }

    public Board getBoard() {
        return board;
    }
}
