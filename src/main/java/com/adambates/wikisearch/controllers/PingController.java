package com.adambates.wikisearch.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PingController {

    static final String HEALTHY_SERVICE_MESSAGE = "Service is healthy.";

    @GetMapping({"/", "ping"})
    String ping() {
        return HEALTHY_SERVICE_MESSAGE;
    }
}
