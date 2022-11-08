package site.orangefield.tistory2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import site.orangefield.tistory2.domain.user.User;
import site.orangefield.tistory2.domain.user.UserRepository;
import site.orangefield.tistory2.domain.visit.Visit;
import site.orangefield.tistory2.domain.visit.VisitRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VisitRepository visitRepository;

    // Controller, Filter, Repository 무거우니까 가짜로 Mock
    // Util은 @Spy로 주입
    @Spy
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private UserService userService;

    // 테스트X
    public void 유저네임중복체크_테스트() {
        // given

        // stub

        // when

        // then

    }

    @Test
    public void 회원가입_테스트() {
        // given 1
        User givenUser = User.builder()
                .username("cate")
                .password("1234")
                .email("cate@naver.com")
                .build();

        // stub 1
        User mockUserEntity = User.builder()
                .id(1)
                .username("cate")
                .password("1234")
                .email("cate@naver.com")
                .profileImg(null)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        when(userRepository.save(givenUser)).thenReturn(mockUserEntity);

        // given 2
        Visit givenVisit = Visit.builder()
                .totalCount(0L)
                .user(mockUserEntity)
                .build();

        // stub 2
        Visit mockVisitEntity = Visit.builder()
                .id(1)
                .totalCount(0L)
                .user(mockUserEntity)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        when(visitRepository.save(givenVisit)).thenReturn(mockVisitEntity);

        // when
        User userEntity = userService.회원가입(givenUser);

        // then
        assertEquals(givenUser.getEmail(), userEntity.getEmail());
    }

    public void 프로파일이미지변경_테스트() {

    }

    public void 패스워드초기화_테스트() {

    }
}
