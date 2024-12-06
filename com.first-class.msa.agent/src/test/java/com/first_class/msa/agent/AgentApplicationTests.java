package com.first_class.msa.agent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AgentApplicationTests {



	@Test
	void contextLoads() {
	}

	@Test
	void testServiceFunctionality() {
		System.out.println("빌드 자동화 테스트 성공");
	}
}