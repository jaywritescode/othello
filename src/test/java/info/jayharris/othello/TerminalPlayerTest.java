package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TerminalPlayerTest {

    static Field readerField;

    @BeforeAll
    static void init() throws Exception {
        readerField = TerminalPlayer.class.getDeclaredField("reader");
        readerField.setAccessible(true);
    }

    @Test
    @DisplayName("gets the correct square")
    void getMove() throws Exception {
        Player player = new TerminalPlayer(Color.BLACK);

        Othello othello = new Othello(player, null);

        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn("e6");

        readerField.set(player, mockReader);

        assertThat(player.getMove(othello)).isSameAs(othello.getBoard().getSquare('e', 6));
    }

    @Test
    @DisplayName("given invalid algebraic notation")
    void getMoveInvalid() throws Exception {
        Player player = new TerminalPlayer(Color.BLACK);

        Othello othello = new Othello(player, null);

        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn("m9");

        readerField.set(player, mockReader);

        assertThat(player.getMove(othello)).isNull();
    }
}