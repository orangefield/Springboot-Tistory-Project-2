package site.orangefield.tistory2.domain.visit;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.orangefield.tistory2.domain.user.User;

@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Visit {
    // User 회원가입시 방문자 카운트를 0으로 초기화 해두기

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long totalCount;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @CreatedDate // insert 할 때 동작
    private LocalDateTime createDate;
    @LastModifiedDate // update 할 때 동작
    private LocalDateTime updateDate;

    @Builder
    public Visit(Integer id, Long totalCount, User user, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.totalCount = totalCount;
        this.user = user;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
