package edu.colostate.cs.cs414.method_men.jungle.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiverTest {

    @Test
    void getAttribute() {
        River river = new River();
        assertTrue(river.getAttribute()=='~');
    }
}
