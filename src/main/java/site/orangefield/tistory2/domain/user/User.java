package site.orangefield.tistory2.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    // 1234 -> SHA256(해시 알고리즘) -> AB4539GDUF3AE -> 이렇게 안하면 시큐리티 거부
    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 60, nullable = false)
    private String email;

    @Column(nullable = true)
    private String profileImg;

    @CreatedDate // insert 할 때 동작
    private LocalDateTime createDate;
    @LastModifiedDate // update 할 때 동작
    private LocalDateTime updateDate;

    @Builder
    public User(Integer id, String username, String password, String email, String profileImg, LocalDateTime createDate,
            LocalDateTime updateDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImg = profileImg;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
