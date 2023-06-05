package com.smu.smuenip;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EntityScan
@EnableJpaAuditing
@SpringBootApplication
@EnableBatchProcessing
public class SmuEnipApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SmuEnipApplication.class)
                .properties(
                        "spring.config.location=" +
                                "classpath:/application.properties" +
                                ", file:/etc/config/application-database.yml" +
                                ", file:/etc/config/application-amazon.yml" +
                                ", file:/etc/config/application-naver.yml"
                )
                .run(args);
    }

}
