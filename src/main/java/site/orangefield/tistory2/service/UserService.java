package site.orangefield.tistory2.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.domain.user.UserRepository;
import site.orangefield.tistory2.domain.visit.Visit;
import site.orangefield.tistory2.domain.visit.VisitRepository;
import site.orangefield.tistory2.handler.ex.CustomException;
// import site.orangefield.tistory2.util.email.EmailUtil;
import site.orangefield.tistory2.web.dto.user.PasswordResetReqDto;

@RequiredArgsConstructor
@Service // IoC 등록
public class UserService {
    // DI
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // private final EmailUtil emailUtil;

    @Transactional
    public void 패스워드초기화(PasswordResetReqDto passwordResetReqDto) {
        // 1. username, email 이 같은 것이 있는지 체크 (DB)
        Optional<User> userOp = userRepository.findByUsernameAndEmail(
                passwordResetReqDto.getUsername(),
                passwordResetReqDto.getEmail());

        // 2. 같은 것이 있다면 DB password 초기화 - BCrypt 해시 - update 하기(DB)
        if (userOp.isPresent()) {
            User userEntity = userOp.get(); // 영속화
            String encPassword = bCryptPasswordEncoder.encode("9999");
            userEntity.setPassword(encPassword);
        } else {
            throw new CustomException("해당 이메일이 존재하지 않습니다.");
        }

        // // 3. 초기화된 비밀번호 이메일로 전송
        // emailUtil.sendEmail("jsw_777@naver.com", "비밀번호 초기화", "초기화된 비밀번호 : 9999");

    } // 더티체킹 (update)

    @Transactional
    public void 회원가입(User user) {
        // 1. save 한 번
        String rawPassword = user.getPassword(); // 1234
        String encPassword = bCryptPasswordEncoder.encode(rawPassword); // 해시된 비번
        user.setPassword(encPassword);
        User userEntity = userRepository.save(user);

        // 2. save 두 번
        Visit visit = new Visit();
        visit.setTotalCount(0L);
        visit.setUser(userEntity);
        visitRepository.save(visit);
    }

    public boolean 유저네임중복체크(String username) {
        Optional<User> userOp = userRepository.findByUsername(username);

        if (userOp.isPresent()) {
            return false;
        } else {
            return true;
        }
    }
}
