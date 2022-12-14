package site.orangefield.tistory2.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.domain.user.UserRepository;
import site.orangefield.tistory2.domain.visit.Visit;
import site.orangefield.tistory2.domain.visit.VisitRepository;
import site.orangefield.tistory2.handler.ex.CustomApiException;
import site.orangefield.tistory2.handler.ex.CustomException;
import site.orangefield.tistory2.util.UtilFileUpload;
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

    @Value("${file.path}")
    private String uploadFolder;

    @Transactional
    public void 프로파일이미지변경(User principal, MultipartFile profileImgFile, HttpSession session) {
        // 1. 파일을 upload 폴더에 저장
        String profileImg = UtilFileUpload.write(uploadFolder, profileImgFile);

        // 2. 해당 경로를 User 테이블에 update
        Optional<User> userOp = userRepository.findById(principal.getId());
        if (userOp.isPresent()) {
            User userEntity = userOp.get();
            userEntity.setProfileImg(profileImg);

            // 세션값 변경
            session.setAttribute("principal", userEntity);
        } else {
            throw new CustomApiException("해당 유저를 찾을 수 없습니다.");
        }
    } // 영속화된 userEntity 변경 후 더티체킹

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
    public User 회원가입(User user) {
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

        return userEntity;
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
