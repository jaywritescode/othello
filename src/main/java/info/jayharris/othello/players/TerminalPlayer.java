package info.jayharris.othello.players;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello;
import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.Pattern;

public class TerminalPlayer extends Player {

    enum MoveState {
        IDLE, ACTIVE
    };

    BufferedReader reader;
    MoveState state = MoveState.IDLE;

    final PrintStream out;
    final static Pattern pattern = Pattern.compile("^[a-h][1-8]$", Pattern.CASE_INSENSITIVE);

    final static String
            INVALID_MSG_TPL = "%s is invalid algebraic notation. Try again: ",
            ILLEGAL_MOVE_MSG = "Illegal move!",
            PLAYER_TO_MOVE_MSG_TPL = "Ply %s >> %s to move: ";

    public TerminalPlayer(Color color) {
        this(color, System.out);
    }

    public TerminalPlayer(Color color, PrintStream out) {
        super(color);
        this.out = out;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Square getMove(Othello othello) {
        out.print(String.format(PLAYER_TO_MOVE_MSG_TPL, othello.getTurnNumber(), getColor()));

        state = MoveState.ACTIVE;

        String line;
        char file;
        int rank;

        try {
            while (true) {
                if (pattern.matcher(line = reader.readLine()).matches()) {
                    file = line.charAt(0);
                    rank = Integer.parseInt(line.substring(1));

                    return othello.getBoard().getSquare(file, rank);
                }

                out.println(String.format(INVALID_MSG_TPL, line));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void begin(Othello othello) {
        out.println(state == MoveState.IDLE ? othello.getBoard().pretty() : ILLEGAL_MOVE_MSG);
    }

    @Override
    public void done(Othello othello) {
        state = MoveState.IDLE;
    }
}
