package site.orangefield.tistory2.config.auth;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import site.orangefield.tistory2.domain.user.User;

@Profile("test")
@Service
public class TestUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = User.builder()
                .id(1)
                .username("ares")
                .password("1234")
                .email("ares@naver.com")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        return new LoginUser(user);
    }
}
