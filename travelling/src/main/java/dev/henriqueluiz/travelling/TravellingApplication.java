package dev.henriqueluiz.travelling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import dev.henriqueluiz.travelling.security.RSAProperties;

@SpringBootApplication
@EnableConfigurationProperties({RSAProperties.class})
public class TravellingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravellingApplication.class, args);
	}

}
