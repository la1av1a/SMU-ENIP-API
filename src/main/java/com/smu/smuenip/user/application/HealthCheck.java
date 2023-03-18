package com.smu.smuenip.user.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HealthCheck {


    @GetMapping("/health")
    private HttpStatus healthCheck() {
        return HttpStatus.OK;
    }
}
