package com.smu.smuenip;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EntityScan
@SpringBootApplication
public class SmuEnipApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SmuEnipApplication.class)
            .properties(
                "spring.config.location=" +
                    "classpath:/application.properties" +
                    ", file:/etc/config/application-database.properties"
            )
            .run(args);
    }

}
