package br.com.dbc.javamosdecolar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JavamosDecolarApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavamosDecolarApplication.class, args);
	}

}
