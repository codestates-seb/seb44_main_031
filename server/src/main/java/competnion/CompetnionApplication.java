package competnion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CompetnionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompetnionApplication.class, args);
	}

}
