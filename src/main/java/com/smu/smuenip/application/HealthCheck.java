package com.smu.smuenip.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin
@RestController
public class HealthCheck {


    @GetMapping("/health")
    private HttpStatus healthCheck() {
        return HttpStatus.OK;
    }
}
