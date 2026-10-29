package com.hospital.hms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class HospitalManagementSystemApplicationTests {

    @Test
    void contextLoads() {
        // Basic check ensuring entire spring context dependency injection resolves perfectly.
    }
}
