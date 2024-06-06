package coursemaker.coursemaker.domain.course.dto;


import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;


@Schema(description = "코스 추가 DTO")
@Data
public class AddTravelCourseRequest {

    @Schema(description = "코스 타이틀", example = "Course Title1")
    private String title;

    @Schema(description = "코스 내용", example = "Course Content1")
    private String content;

    @Schema(description = "여행 기간", example = "3")
    @Min(value = 1, message = "Duration should not be less than 1")
    @Max(value = 3)
    private Integer duration;

    @Schema(description = "여행 인원", example = "5")
    @Min(value = 1, message = "Traveler count should not be less than 1")
    private Integer travelerCount;

    @Schema(description = "여행 타입", example = "0")
    @Min(value = 0, message = "Travel type should not be negative")
    private Integer travelType;

    @Schema(description = "코스 대표 이미지 주소", example = "http://example.com/course1.jpg")
    @NotBlank(message = "Picture link is mandatory")
    private String pictureLink;

    @Schema(description = "코스 여행지 목록")
    private List<AddCourseDestinationRequest> courseDestinations;

    @Schema(description = "유저 닉네임", example = "nickname1")
    @NotNull(message = "Member is mandatory")
    private String nickname;// 유저 닉네임
    
    // 태그를 빼먹어여?! 대가리 박고있죠ㅇㅇ
    @Schema(description = "코스 태그 목록")
    private List<TagResponseDto> tags;

}