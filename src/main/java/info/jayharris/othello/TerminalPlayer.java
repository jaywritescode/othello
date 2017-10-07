package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class TerminalPlayer extends Player {

    BufferedReader reader;
    final static Pattern pattern = Pattern.compile("^[a-h][1-8]$", Pattern.CASE_INSENSITIVE);

    public TerminalPlayer(Color color) {
        super(color);
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Square getMove(Othello othello) {
        System.out.println(othello.getBoard().pretty());

        String line;
        try {
            if (pattern.matcher(line = reader.readLine()).matches()) {
                return othello.getBoard().getSquare(line.charAt(0), Integer.parseInt(line.substring(1)));
            }
        }
        catch (IOException e) { }
        return null;
    };
}
