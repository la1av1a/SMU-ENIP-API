package com.smu.smuenip.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class SwaggerController {

    @GetMapping("/swagger")
    public String redirectToSwagger() {
        return "redirect:https://www.smu-enip.site/swagger-ui/";
    }
}
