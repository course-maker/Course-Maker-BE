package coursemaker.coursemaker.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateNicknameRequest {
    @Schema(description = "닉네임", example = "nickname12")
    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;
}
