package site.orangefield.tistory2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.category.CategoryRepository;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.domain.user.UserRepository;

@Configuration
public class DBInitializer {

    @Profile("test")
    @Bean
    public CommandLineRunner demo(UserRepository userRepository, CategoryRepository categoryRepository) {

        return (args) -> {
            User principal = User.builder()
                    .username("ares")
                    .password("1234")
                    .email("ares@naver.com")
                    .build();

            userRepository.save(principal);

            Category category = Category.builder()
                    .title("바밤바")
                    .user(principal)
                    .build();
            categoryRepository.save(category);
        };
    }
}
