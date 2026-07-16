package com.lettre.knowledge;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.lettre.knowledge.mapper")
public class KnowledgeBackendApplication {


    public static void main(String[] args) {

        SpringApplication.run(
            KnowledgeBackendApplication.class,
            args
        );

    }

}