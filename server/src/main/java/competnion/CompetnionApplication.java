package competnion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class CompetnionApplication {

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");  // 추가
	}

	public static void main(String[] args) {
		SpringApplication.run(CompetnionApplication.class, args);
	}

}
