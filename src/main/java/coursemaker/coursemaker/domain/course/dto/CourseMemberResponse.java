package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "멤버에서 코스로 닉네임 가져오는 DTO")
@Data
public class CourseMemberResponse {

    @Schema(description = "코스를 만든 유저의 닉네임", example = "nickname1")
    private String nickname;

    public CourseMemberResponse(Member member) {
        this.nickname = member.getNickname();
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .build();
    }
}