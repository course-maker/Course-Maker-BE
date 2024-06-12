package coursemaker.coursemaker.domain.member.dto;

import coursemaker.coursemaker.domain.member.exception.IllegalUserArgumentException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRequest {
//    @Schema(description = "유저 ID", example = "1")
//    @NotNull(message = "유저 ID가 비어있습니다.")
//    private Long userId; //TODO: 쿠키에서 가져오기

    @Schema(description = "이름", example = "홍길동")
    @Pattern(regexp = "[a-zA-Z가-힣]{2,10}", message = "이름은 2~10자의 영문자, 한글로 구성되어야 합니다.")
    private String name;

    @Schema(description = "닉네임", example = "nickname12")
    @Pattern(regexp = "[a-z0-9가-힣]{2,10}", message = "닉네임은 2~10자의 영문 소문자, 숫자, 한글로 구성되어야 합니다.")
    private String nickname;

    @Schema(description = "비밀번호", example = "your_password")
    @Size(min = 4, max = 15, message = "비밀번호는 최소 4자 이상 15자 이하이어야 합니다.")
    private String password;

    @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
    @Pattern(regexp = "^(http|https)://.*$", message = "유효하지 않은 프로필 이미지 URL입니다.")
    private String profileImgUrl;

    @Schema(description = "프로필 설명", example = "이것은 프로필 설명입니다.")
    private String profileDescription;

    public void validate() {
//        if (userId == null) {
//            throw new IllegalUserArgumentException("유저 ID가 비어있습니다.", "userId is empty");
//        }
        if (name != null && !name.isBlank() && !name.matches("[a-zA-Z가-힣]{2,10}")) {
            throw new IllegalUserArgumentException("이름은 2~10자의 영문자, 한글로 구성되어야 합니다.", "invalid name format");
        }
        if (nickname != null && !nickname.isBlank() && !nickname.matches("[a-z0-9가-힣]{2,10}")) {
            throw new IllegalUserArgumentException("닉네임은 2~10자의 영문 소문자, 숫자, 한글로 구성되어야 합니다.", "invalid nickname format");
        }
        if (password != null && !password.isBlank() && (password.length() < 4 || password.length() > 15)) {
            throw new IllegalUserArgumentException("비밀번호는 최소 4자 이상 15자 이하이어야 합니다.", "password is too short");
        }
        if (profileImgUrl != null && !profileImgUrl.isBlank() && !profileImgUrl.matches("^(http|https)://.*$")) {
            throw new IllegalUserArgumentException("유효하지 않은 프로필 이미지 URL입니다.", "invalid profile image URL format");
        }
    }
}

