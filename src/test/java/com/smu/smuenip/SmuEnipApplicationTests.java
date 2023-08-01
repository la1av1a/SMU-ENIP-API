package com.smu.smuenip;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(S3MockConfig.class)
@SpringBootTest
class SmuEnipApplicationTests {

    @Test
    void contextLoads() {
    }

}
