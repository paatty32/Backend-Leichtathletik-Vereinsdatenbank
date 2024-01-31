package de.boadu.leichtathletik.vereinsdatenbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
		@PropertySource("classpath:dao-application.properties")
})
public class LeichtathletikVereinsdatenbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeichtathletikVereinsdatenbankApplication.class, args);
	}

}
