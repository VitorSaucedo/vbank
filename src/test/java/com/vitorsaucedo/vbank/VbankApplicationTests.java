package com.vitorsaucedo.vbank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource(properties = {
		"api.security.token.secret=test-secret-key-12345678901234567890123456789012",
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driverClassName=org.h2.Driver"
})
class VbankApplicationTests {

	@Test
	void contextLoads() {
	}
}
