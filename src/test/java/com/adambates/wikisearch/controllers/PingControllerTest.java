package com.adambates.wikisearch.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PingControllerTest {

    private final PingController controller = new PingController();

    @Test
    void ping() {
        assertThat(controller.ping()).isEqualTo(PingController.HEALTHY_SERVICE_MESSAGE);
    }
}
