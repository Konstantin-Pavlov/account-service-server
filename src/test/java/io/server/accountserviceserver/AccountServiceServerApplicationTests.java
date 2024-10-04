package io.server.accountserviceserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountServiceServerApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures that the Spring application context loads successfully.
    }

    @Test
    void mainMethodTest() {
        // This test ensures that the main method runs without throwing any exceptions.
        AccountServiceServerApplication.main(new String[] {});
    }

}
