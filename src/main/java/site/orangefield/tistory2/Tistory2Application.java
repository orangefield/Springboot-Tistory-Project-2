package site.orangefield.tistory2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Tistory2Application {

	public static void main(String[] args) {
		SpringApplication.run(Tistory2Application.class, args);
	}

}
