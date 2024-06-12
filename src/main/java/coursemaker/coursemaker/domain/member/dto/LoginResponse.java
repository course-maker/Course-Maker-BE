package coursemaker.coursemaker.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    @Schema(description = "액세스 토큰", example = "access_token_example")
    String accessToken;

    @Schema(description = "리프레시 토큰", example = "refresh_token_example")
    String refreshToken;

    @Schema(description = "유저 닉네임", example = "nickname12")
    String nickname;
}

