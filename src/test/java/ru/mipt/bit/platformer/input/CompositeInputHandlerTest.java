package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompositeInputHandlerTest {

    @Test
    void testAllHandlersInvoked() {
        CompositeInputHandler composite = new CompositeInputHandler();

        final boolean[] called = {false, false};
        composite.addHandler(() -> called[0] = true);
        composite.addHandler(() -> called[1] = true);

        composite.handleInput();

        assertTrue(called[0]);
        assertTrue(called[1]);
    }
}
