package site.orangefield.tistory2.config.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.domain.user.UserRepository;

@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // System.out.println(1);
        Optional<User> userOp = userRepository.findByUsername(username);
        // System.out.println(2);
        if (userOp.isPresent()) {
            return new LoginUser(userOp.get());
        }
        // System.out.println(3); // 3번까지 안가네
        return null;
    }

}
