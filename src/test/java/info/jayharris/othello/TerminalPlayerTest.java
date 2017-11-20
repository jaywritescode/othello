package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.players.TerminalPlayer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestInstance(Lifecycle.PER_CLASS)
class TerminalPlayerTest {

    static Field readerField, printStreamField;

    Player player;
    Othello othello;

    @Mock BufferedReader mockReader;

    @BeforeAll
    void init() throws Exception {
        readerField = TerminalPlayer.class.getDeclaredField("reader");
        readerField.setAccessible(true);
    }

    @BeforeEach
    void initTest() throws Exception {
        MockitoAnnotations.initMocks(this);

        player = new TerminalPlayer(Color.BLACK);
        othello = new Othello(player, null);

        readerField.set(player, mockReader);
    }

    @Nested
    @DisplayName("#getMove")
    class GetMove {

        @Test
        @DisplayName("given valid algebraic notation")
        void testValidAlgebraicNotation() throws Exception {
            when(mockReader.readLine()).thenReturn("a4");
            assertThat(player.getMove(othello)).isSameAs(othello.getBoard().getSquare('a', 4));
        }

        @Test
        @DisplayName("given invalid algebraic notation")
        void testInvalidAlgebraicNotation() throws Exception {
            when(mockReader.readLine()).thenReturn("m9", "c6");

            assertThat(player.getMove(othello)).isSameAs(othello.getBoard().getSquare('c', 6));
            verify(mockReader, times(2)).readLine();
        }
    }
}