package site.orangefield.tistory2.web.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.orangefield.tistory2.domain.category.Category;
import site.orangefield.tistory2.domain.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryWriteReqDto {

    @Size(min = 1, max = 60)
    @NotBlank
    private String title;

    public Category toEntity(User principal) {
        Category category = new Category();
        category.setTitle(title);
        category.setUser(principal);

        return category;
    }
}
