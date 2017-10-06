package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
class TerminalPlayerTest {

    static Field readerField;

    Player player;
    Othello othello;

    @Mock BufferedReader mockReader;

    @BeforeAll
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        readerField = TerminalPlayer.class.getDeclaredField("reader");
        readerField.setAccessible(true);
    }

    @BeforeEach
    void initTest() throws Exception {
        player = new TerminalPlayer(Color.BLACK);
        othello = new Othello(player, null);

        readerField.set(player, mockReader);
    }

    @Test
    @DisplayName("gets the correct square")
    void getMove() throws Exception {
        when(mockReader.readLine()).thenReturn("e6");
        assertThat(player.getMove(othello)).isSameAs(othello.getBoard().getSquare('e', 6));
    }

    @Test
    @DisplayName("given invalid algebraic notation")
    void getMoveInvalid() throws Exception {
        when(mockReader.readLine()).thenReturn("m9");
        assertThat(player.getMove(othello)).isNull();
    }
}