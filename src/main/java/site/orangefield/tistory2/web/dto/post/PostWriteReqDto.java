package site.orangefield.tistory2.web.dto.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.post.Post;
import site.orangefield.tistory2.domain.user.User;

@NoArgsConstructor
@Data
public class PostWriteReqDto {

    @NotBlank
    private Integer categoryId;

    @Size(min = 1, max = 60)
    @NotBlank
    private String title;

    private MultipartFile thumbnailFile; // 섬네일은 null 허용

    @NotNull
    private String content; // 컨텐트 null 허용X

    @Builder
    public PostWriteReqDto(@NotBlank Integer categoryId, @Size(min = 1, max = 60) @NotBlank String title,
            MultipartFile thumbnailFile, @NotNull String content) {
        this.categoryId = categoryId;
        this.title = title;
        this.thumbnailFile = thumbnailFile;
        this.content = content;
    }

    public Post toEntity(String thumbnail, User principal, Category category) {
        Post post = new Post();

        post.setTitle(title);
        post.setContent(content);
        post.setThumbnail(thumbnail);
        post.setUser(principal);
        post.setCategory(category);

        return post;
    }
}
