package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Espaço Therapy Kids API",
                version = "v1",
                description = "API REST do projeto (Pacientes, Sessões, Auth)"
        )
)

@SpringBootApplication
public class EspacoTherapyKidsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EspacoTherapyKidsApplication.class, args);
    }
}
