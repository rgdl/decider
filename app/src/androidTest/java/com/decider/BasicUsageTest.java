package com.decider;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class BasicUsageTest {
    @Test
    public void failingTest() {
        assertThat(1).isEqualTo(2);
    }

    @Test
    public void passingTest() {
        assertThat(1).isEqualTo(1);
    }
}
