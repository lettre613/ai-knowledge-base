package com.lettre.knowledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lettre.knowledge")
public class KnowledgeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeBackendApplication.class, args);
	}

}
