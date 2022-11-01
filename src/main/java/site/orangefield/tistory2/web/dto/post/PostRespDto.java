package site.orangefield.tistory2.web.dto.post;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.post.Post;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostRespDto {

    private List<Post> posts;
    private List<Category> categories;
}
