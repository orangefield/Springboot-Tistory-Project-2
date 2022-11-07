package site.orangefield.tistory2.web.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.orangefield.tistory2.domain.post.Post;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDetailRespDto {

    private Post post;
    private boolean isPageOwner; // getter는 isPageOwner(){}, setter는 setPageOwner(){}
    private boolean isLove; // 좋아요를 했으면 true
}
