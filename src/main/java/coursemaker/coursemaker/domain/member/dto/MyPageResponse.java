package coursemaker.coursemaker.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyPageResponse {
    @Schema(description = "닉네임", example = "nickname12")
    String nickname;

    @Schema(description = "이름", example = "홍길동")
    String name;

    @Schema(description = "프로필 설명", example = "이것은 프로필 설명입니다.")
    String profileDescription;

    @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
    String profileImgUrl;
}
